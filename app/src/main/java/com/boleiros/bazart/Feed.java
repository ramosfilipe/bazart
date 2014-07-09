package com.boleiros.bazart;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.boleiros.bazart.camera.CameraActivity;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


public class Feed extends Activity {

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
        if(ParseUser.getCurrentUser()==null){

            ParseLoginBuilder builder = new ParseLoginBuilder(Feed.this);
            startActivityForResult(builder.build(), 0);}
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_feed);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private final int PICK_IMAGE = 0;

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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

            ParseQuery<Produto> query  = ParseQuery.getQuery("Produto");

            query.findInBackground(new FindCallback<Produto>() {
                @Override
                public void done(List<Produto> parseObjects, com.parse.ParseException e) {
                    if (e==null){

                        Log.d("THE OBJECT", "" +parseObjects.size());


                        String name =  parseObjects.toString();
                        Log.d("THE QUERY ", "" + name);

                    } else {
                        Log.d("ERROR:", "" + e.getMessage());
                    }
                }
            });




            ImageButton camera = (ImageButton) rootView.findViewById(R.id.cameraButton);
            CardListView listaCards = (CardListView) rootView.findViewById(R.id.listaCards);
            Card teste = new Card(this.getActivity());
            CardHeader teste2 = new CardHeader(this.getActivity());
            teste2.setTitle("testee");
            CardThumbnail teste3 = new CardThumbnail(this.getActivity());
            teste3.setDrawableResource(R.drawable.cerveja);

            teste.addCardThumbnail(teste3);
            teste.addCardHeader(teste2);


           // novoTeste.setInnerLayout(R.layout.inside_card);

            ArrayList<Card> listaTeste = new ArrayList<Card>();
            listaTeste.add(teste);
            //listaTeste.add(novoTeste);
            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), listaTeste);
            if (listaCards != null) {
                listaCards.removeAllViewsInLayout();
                listaCards.setAdapter(mCardArrayAdapter);}

            camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                   // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent,PICK_IMAGE);
                    Intent intent = new Intent(getActivity(),CameraActivity.class);
                    startActivity(intent);
                }
            });
            return rootView;
        }
//        @Override
//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                ImageView imageView = (ImageView) this.getView().findViewById(R.id.imageView);
//                imageView.setImageBitmap(photo);
//            }
//        }
    }

}
