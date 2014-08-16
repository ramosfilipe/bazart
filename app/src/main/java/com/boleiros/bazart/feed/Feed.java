package com.boleiros.bazart.feed;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boleiros.bazart.Bazart;
import com.boleiros.bazart.R;
import com.boleiros.bazart.camera.CameraActivity;
import com.boleiros.bazart.camera.InfoFragment;
import com.boleiros.bazart.hashtags.HashtagActivity;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.profile.ProfileActivity;
import com.boleiros.bazart.util.ActivityStore;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;

public class Feed extends Activity {

    private static final int MAX_CHAR = 30;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() == null) {

            ParseLoginBuilder builder = new ParseLoginBuilder(Feed.this);
            startActivityForResult(builder.build(), 0);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_feed);


        final ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar);

        actionBar.setDisplayShowCustomEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        TextView actionBarText = (TextView) findViewById(R.id.textViewActionBar);
        actionBarText.setText("Feed");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            makeMeRequest();
        }


    }

    public void changeAct(String str) {
        Intent intent = new Intent(this, HashtagActivity.class);
        intent.putExtra("busca", str);
        startActivity(intent);

    }
    public void changeActProfile(ParseUser user){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("name",user.getUsername());
        intent.putExtra("id",user.getObjectId());
        try {
            intent.putExtra("pic",user.getParseFile("profilePic").getData());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }


    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            // Create a JSON object to hold the profile info
                            // Populate the JSON object
                            String nome = user.getName();
                            int maxLength = (nome.length() < MAX_CHAR) ? nome.length() : MAX_CHAR;
                            ParseUser.getCurrentUser().setUsername(user.getName().substring(0,
                                    maxLength));
                            ParseUser.getCurrentUser().saveEventually();
                        }
                    }
                }
        );
        request.executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements LocationListener,
            GooglePlayServicesClient.ConnectionCallbacks,
            GooglePlayServicesClient.OnConnectionFailedListener {
        /*
             * Define a request code to send to Google Play services This code is returned in
             * Activity.onActivityResult
             */
        private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
        /*
         * Constants for location update parameters
         */
        // Milliseconds per second
        private static final int MILLISECONDS_PER_SECOND = 1000;
        // The update interval
        private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
        // A fast interval ceiling
        private static final int FAST_CEILING_IN_SECONDS = 1;
        // Update interval in milliseconds
        private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
                * UPDATE_INTERVAL_IN_SECONDS;
        // A fast ceiling of update intervals, used when the app is visible
        private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
                * FAST_CEILING_IN_SECONDS;
        private static final String ARG_SECTION_NUMBER = "section_number";

        private static final int BOTAO_GPS_ATIVADO = 1;
        private static final int BOTAO_HOME_ATIVADO = 2;
        private static final int BOTAO_RECOMENDACAO_ATIVADO = 3;
        public static final String PRODUTO = "Produto";
        public static final String IS_SOLD = "isSold";


        private int botaoSelecionado = BOTAO_HOME_ATIVADO;
        private Location lastLocation = null;
        private Location currentLocation = null;
        private LocationRequest locationRequest;
        private LocationClient locationClient;
        private SwipeRefreshLayout swipeRefreshLayout;

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onStop() {
            super.onStop();
            locationClient.disconnect();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
            // Create a new location client, using the enclosing class to handle callbacks.
            locationClient = new LocationClient(this.getActivity(), this, this);
            locationClient.connect();
            consultaAoParse();


        }

        public void consultaAoParse() {
            ParseQuery<Produto> query = ParseQuery.getQuery(PRODUTO);
            query.include("author");
            query.orderByDescending("createdAt");
            query.whereEqualTo(IS_SOLD, false);
            //query.setLimit(10);
            query.findInBackground(new FindCallback<Produto>() {
                @Override
                public void done(List<Produto> parseObjects, com.parse.ParseException e) {
                    if (e == null) {
                        ProdutoAdapter produtoAdapter = new ProdutoAdapter(getActivity(),
                                parseObjects);
                        final ListView listaDeExibicao = (ListView) getActivity().findViewById(R
                                .id.listaCards);
                        if (listaDeExibicao != null) {
                            listaDeExibicao.setAdapter(produtoAdapter);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        ocorreuProblemaNaConsulta();
                    }
                }
            });
        }

        public void consultaAoParseComLocalizacao(ParseGeoPoint ponto) {
            ParseQuery<Produto> query = ParseQuery.getQuery(PRODUTO);
            query.include("author");
            query.whereEqualTo(IS_SOLD, false);
            query.whereNear("location", ponto);
            query.setLimit(7);
            // query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<Produto>() {
                @Override
                public void done(List<Produto> parseObjects, com.parse.ParseException e) {
                    if (e == null) {
                        ProdutoAdapter produtoAdapter = new ProdutoAdapter(getActivity(),
                                parseObjects);
                        final ListView listaDeExibicao = (ListView) getActivity().findViewById(R
                                .id.listaCards);
                        if (listaDeExibicao != null) {
                            listaDeExibicao.setAdapter(produtoAdapter);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        ocorreuProblemaNaConsulta();
                    }
                }
            });
        }

        public void consultaAoParseComRecomendacao() {
            ParseQuery<Produto> query = ParseQuery.getQuery(PRODUTO);
            query.include("author");
            query.whereEqualTo(IS_SOLD, false);
            query.orderByDescending("numLikes");
            query.setLimit(7);
            query.findInBackground(new FindCallback<Produto>() {
                @Override
                public void done(List<Produto> parseObjects, com.parse.ParseException e) {
                    if (e == null) {
                        ProdutoAdapter produtoAdapter = new ProdutoAdapter(getActivity(),
                                parseObjects);
                        final ListView listaDeExibicao = (ListView) getActivity().findViewById(R
                                .id.listaCards);
                        if (listaDeExibicao != null) {
                            listaDeExibicao.setAdapter(produtoAdapter);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        ocorreuProblemaNaConsulta();
                    }
                }
            });
        }

        private void geraFrasesDoSistema() {
            final ArrayList<String> frases = ActivityStore.getInstance(getActivity()).getFrases();
            ParseQuery<Produto> query = new ParseQuery(PRODUTO);
            query.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if (e == null) {
                        frases.add("Temos " + count + " produto(s) anunciados");
                        ActivityStore.getInstance(getActivity()).setFrases(frases);
                        ParseQuery<Produto> query3 = new ParseQuery(PRODUTO);
                        query3.whereEqualTo(IS_SOLD, true);
                        query3.countInBackground(new CountCallback() {
                            @Override
                            public void done(int count, ParseException e) {
                                if (e == null) {
                                    frases.add("Temos " + count + " produtos vendidos");
                                    ActivityStore.getInstance(getActivity()).setFrases(frases);
                                } else {
                                    Toast.makeText(getActivity(),
                                            "Ops... Verifique sua conexão com a Internet",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(),
                                "Ops... Verifique sua conexão com a Internet",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
//            ParseQuery<ParseObject> query2 = new ParseQuery("user");
//            query.countInBackground(new CountCallback() {
//                @Override
//                public void done(int count, ParseException e) {
//                    if (e == null) {
//                        frases.add("Temos "+ count+" usuários no bazar+");
//                        ActivityStore.getInstance(getActivity()).setFrases(frases);
//                    } else {
//                        Toast.makeText(getActivity(),
//                                "Ops... Verifique sua conexão com a Internet",
//                                Toast.LENGTH_LONG).show();
//                    }
//                }
//            });


        }

        private void ocorreuProblemaNaConsulta() {
            Toast.makeText(getActivity(),
                    "Ops... Verifique sua conexão com a Internet",
                    Toast.LENGTH_LONG).show();
        }


        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_feed,
                    container, false);
            swipeRefreshLayout.setRefreshing(true);
            geraFrasesDoSistema();
            ImageButton camera = (ImageButton) swipeRefreshLayout.findViewById(R.id.cameraButton);
            final ImageButton gps = (ImageButton) swipeRefreshLayout.findViewById(R.id.gpsButton);
            final ImageButton home = (ImageButton) swipeRefreshLayout.findViewById(R.id.homeButton);
            final ImageButton profile = (ImageButton) swipeRefreshLayout.findViewById(R.id
                    .profileImageButton);
            final ImageButton recomendacao = (ImageButton) swipeRefreshLayout.findViewById(R.id
                    .recomendacaoImageButton);
            locationClient.connect();

            final ListView listaDeExibicao = (ListView) swipeRefreshLayout.findViewById(R.id
                    .listaCards);

            final ImageButton busca = (ImageButton) getActivity().findViewById(R.id
                    .botaoBuscaActionBar);
            final TextView actionBarText = (TextView) getActivity().findViewById(R.id.textViewActionBar);

            gps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (botaoSelecionado != BOTAO_GPS_ATIVADO) {
                        swipeRefreshLayout.setRefreshing(true);
                        botaoSelecionado = BOTAO_GPS_ATIVADO;
                        actionBarText.setText("Localização");
                        gps.setImageResource(R.drawable.gpspressed);
                        home.setImageResource(R.drawable.homefeed);
                        recomendacao.setImageResource(R.drawable.recom);


                        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                        if (myLoc == null) {
                            Toast.makeText(getActivity(),
                                    "Please try again after your location appears on the map.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
                        consultaAoParseComLocalizacao(myPoint);
                        listaDeExibicao.smoothScrollToPosition(0);

                    }
                }

            });
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                }
            });

            recomendacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (botaoSelecionado != BOTAO_RECOMENDACAO_ATIVADO) {
                        actionBarText.setText("Recomendações");
                        swipeRefreshLayout.setRefreshing(true);
                        botaoSelecionado = BOTAO_RECOMENDACAO_ATIVADO;
                        gps.setImageResource(R.drawable.gps);
                        home.setImageResource(R.drawable.homefeed);
                        recomendacao.setImageResource(R.drawable.recompressed);
                        consultaAoParseComRecomendacao();
                        listaDeExibicao.smoothScrollToPosition(0);
                    }
                }
            });


            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (botaoSelecionado != BOTAO_HOME_ATIVADO) {
                        swipeRefreshLayout.setRefreshing(true);
                        actionBarText.setText("Feed");
                        botaoSelecionado = BOTAO_HOME_ATIVADO;
                        gps.setImageResource(R.drawable.gps);
                        home.setImageResource(R.drawable.homefeedpressed);
                        recomendacao.setImageResource(R.drawable.recom);
                        consultaAoParse();
                        listaDeExibicao.smoothScrollToPosition(0);

                    }
                }
            });


            busca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), HashtagActivity.class);
                    startActivity(intent);
                }
            });


            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    startActivity(intent);
                }
            });

            swipeRefreshLayout.setColorScheme(android.R.color.darker_gray,
                    android.R.color.holo_red_light,
                    android.R.color.holo_red_dark, android.R.color.background_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    Log.e(getClass().getSimpleName(), "refresh");
                    if (botaoSelecionado == BOTAO_HOME_ATIVADO) {
                        consultaAoParse();
                    }
                    if (botaoSelecionado == BOTAO_GPS_ATIVADO) {
                        consultaAoParseComLocalizacao(geoPointFromLocation(currentLocation));
                    }
                    if (botaoSelecionado == BOTAO_RECOMENDACAO_ATIVADO) {
                        consultaAoParseComRecomendacao();
                    }
                }
            });

            listaDeExibicao.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    boolean enable = false;
                    if (listaDeExibicao != null && listaDeExibicao.getChildCount() > 0) {
                        // check if the first item of the list is visible
                        boolean firstItemVisible = listaDeExibicao.getFirstVisiblePosition() == 0;
                        // check if the top of the first item is visible
                        boolean topOfFirstItemVisible = listaDeExibicao.getChildAt(0).getTop() == 0;
                        // enabling or disabling the refresh layout
                        enable = firstItemVisible && topOfFirstItemVisible;
                    }
                    swipeRefreshLayout.setEnabled(enable);
                }
            });
            return swipeRefreshLayout;
        }

        private void startPeriodicUpdates() {
            locationClient.requestLocationUpdates(locationRequest, this);
        }

        private void stopPeriodicUpdates() {
            locationClient.removeLocationUpdates(this);
        }

        private Location getLocation() {
            if (servicesConnected()) {
                return locationClient.getLastLocation();
            } else {
                return null;
            }
        }

        private boolean servicesConnected() {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this
                    .getActivity());

            if (ConnectionResult.SUCCESS == resultCode) {

                return true;
            } else {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
                        this.getActivity(), 0);
                if (dialog != null) {
                    InfoFragment.ErrorDialogFragment errorFragment = new InfoFragment
                            .ErrorDialogFragment();
                    errorFragment.setDialog(dialog);
                    errorFragment.show(getFragmentManager(), Bazart.APPTAG);
                }
                return false;
            }
        }

        /**
         * Called when the location has changed.
         * <p/>
         * <p> There are no restrictions on the use of the supplied Location object.
         *
         * @param location The new location, as a Location object.
         */
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            if (lastLocation != null
                    && geoPointFromLocation(location)
                    .distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01) {
                return;
            }
            lastLocation = location;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**
         * Called when the provider is enabled by the user.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         */
        public void onProviderEnabled(String provider) {

        }

        /**
         * Called when the provider is disabled by the user. If requestLocationUpdates
         * is called on an already disabled provider, this method is called
         * immediately.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         */

        public void onProviderDisabled(String provider) {

        }

        /*
          * Helper method to get the Parse GEO point representation of a location
          */
        private ParseGeoPoint geoPointFromLocation(Location loc) {
            return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
        }

        @Override
        public void onConnected(Bundle bundle) {
            currentLocation = getLocation();
            startPeriodicUpdates();
        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this.getActivity(),
                            CONNECTION_FAILURE_RESOLUTION_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    System.out.println("erro OnConnectionFailed");
                }
            }
        }
//        private class geraFrasesDoSistemaTask extends AsyncTask<Void, Void , String[]> {
//            Context context;
//
//
//            public geraFrasesDoSistemaTask(Context ctx) {
//                context = ctx;
//            }
//
//            @Override
//            protected void onPreExecute() {
//
//            }
//
//            @Override
//            protected String[] doInBackground(Void... params) {
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("MyClass");
//                query.countInBackground(new CountCallback() {
//                    public void done(int count, ParseException e) {
//                        if (e == null) {
//                            objectsWereCounted(count);
//                        } else {
//                            objectCountFailed();
//                        }
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String[] result) {
//                ActivityStore.getInstance(context).setFrases(result);
//
//            }
//        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(0);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

    }
}



