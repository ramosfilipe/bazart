package com.boleiros.bazart.camera;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.util.Rfc822Tokenizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.ex.chips.BaseRecipientAdapter;
import com.boleiros.bazart.Bazart;
import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.Feed;
import com.boleiros.bazart.hashtags.CustomRecipients;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.util.ActivityStore;
import com.boleiros.bazart.util.NumericRangeFilter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class InfoFragment extends Fragment implements LocationListener,GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    // Fields for helping process map and location changes
    private int mostRecentMapUpdate = 0;
    private boolean hasSetUpInitialLocation = false;
    private String selectedObjectId;
    private Location lastLocation = null;
    private Location currentLocation = null;
    private LocationRequest locationRequest;
    private LocationClient locationClient;
    private static final InputFilter[] FILTERS = new InputFilter[] {new NumericRangeFilter(0.00, 999999.99)};

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



    public InfoFragment() {
        // Required empty public constructor
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

//        if (getArguments() != null) {
//
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ImageView preview = (ImageView) v.findViewById(R.id.previewAntesDeEnviar);
        final EditText telefone = (EditText) v.findViewById(R.id.editTextPhoneNumber);
        telefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        final EditText preco = (EditText) v.findViewById(R.id.editTextPreco);

        final CustomRecipients hashtags = (CustomRecipients)v.findViewById(R.id.editTextHashtags);

        hashtags.setTokenizer(new Rfc822Tokenizer());
        BaseRecipientAdapter a = new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE, getActivity()) {
        };

        hashtags.setAdapter(new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE, getActivity()) {
        });


       /* hashtags.setChipListener(new RecipientEditTextView.IChipListener() {
            @Override
            public void onDataChanged() {

            }
        });*/

        String[] teste = {"casa","carro"};


        preco.setFilters(FILTERS);
       // preco.setOnFocusChangeListener(ON_FOCUS);
        loadBitmap(ActivityStore.getInstance(this.getActivity()).getImage(),preview);
        ImageButton botaoVoltar = (ImageButton)v.findViewById(R.id.imageButtonBack);
        botaoVoltar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                com.commonsware.cwac.camera.CameraFragment current=new CameraFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, current).commit();
            }
        });
        final ImageButton botaoEnvia = (ImageButton)v.findViewById(R.id.imageButtonUpload);
        botaoEnvia.setVisibility(View.VISIBLE);
        botaoEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preco.getText().length()<1) {
                    Toast.makeText(getActivity(),"Insira o preço do produto",Toast.LENGTH_SHORT).show();
                }
                else if(telefone.getText().length()<1){
                    Toast.makeText(getActivity(),"Insira o número para contato",Toast.LENGTH_SHORT).show();
                }
                else if(hashtags.getSize() == 0){
                    Toast.makeText(getActivity(),"Insira alguma hashtag",Toast.LENGTH_SHORT).show();
                }
                else if(hashtags.getSize() > 3 ){
                    Toast.makeText(getActivity(),"Insira no máximo 3 hashtags",Toast.LENGTH_SHORT).show();
                }
                else {
                    Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                    if (myLoc == null) {
                        Toast.makeText(getActivity(),
                                "Please try again after your location appears on the map.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
                    // 3

                    botaoEnvia.setVisibility(View.GONE);
                    ParseFile photoFile = new ParseFile("fotoProduto.jpg", ActivityStore.getInstance(getActivity()).
                            getImage());
                    EditText preco1 = (EditText) getActivity().findViewById(R.id.editTextPreco);
                    EditText telefone1 = (EditText) getActivity().findViewById(R.id.editTextPhoneNumber);
                    String precoStr = preco.getText().toString();
                    precoStr = "R$ " + precoStr;
                    Produto produto = new Produto();
                    produto.setLocation(myPoint);


                    produto.setAuthor(ParseUser.getCurrentUser());
                    produto.setPhotoFile(photoFile);
                    produto.setPhoneNumber(telefone1.getText().toString());
                    produto.setPrice(precoStr);
                    produto.setArrayHashtags(hashtags.getArrayOfText());
                    produto.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e != null) {
                                Toast.makeText(getActivity(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Produto anunciado!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), Feed.class);
                                startActivity(intent);
                            }
                        }


                    });
                }

            }
            });
        return v;
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
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());

        if (ConnectionResult.SUCCESS == resultCode) {

            return true;
        } else {
            System.out.println("testeeee");
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(), 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
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
                connectionResult.startResolutionForResult(this.getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
            }
        } else {
//            showErrorDialog(connectionResult.getErrorCode());
        }
    }


    public interface OnFragmentInteractionListener {
    }
    void loadBitmap(byte[] foto, ImageView imageView) {
            final BitmapWorker task = new BitmapWorker(imageView);
            task.execute(foto);
    }
    /*
     * Show a dialog returned by Google Play services for the connection error code
     */
    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        Dialog errorDialog =
                GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(this.getFragmentManager(), Bazart.APPTAG);
        }
    }

    /*
     * Define a DialogFragment to display the error dialog generated in showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /*
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}
class BitmapWorker extends AsyncTask<byte[], Void, Bitmap> {
    private  ImageView imageViewReference;

    public BitmapWorker(ImageView imageView) {
        imageViewReference = imageView;
    }
    // Decode image in background.
    @Override
    protected Bitmap doInBackground(byte[]... params) {
        return BitmapFactory.decodeByteArray(params[0], 0,params[0].length );
    }
    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
                imageViewReference.setImageBitmap(bitmap);
            }
    }
}
