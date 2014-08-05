package com.boleiros.bazart.profile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.boleiros.bazart.R;
import com.boleiros.bazart.modelo.Produto;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.boleiros.bazart.profile.Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.boleiros.bazart.profile.Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    ProfileAdapter adapt;
    private OnFragmentInteractionListener mListener;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void consultaAoParse() {
        ParseQuery<Produto> query = ParseQuery.getQuery("Produto");
        query.include("author");
        query.whereEqualTo("author", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Produto>() {
            @Override
            public void done(List<Produto> parseObjects, ParseException e) {
                if (e == null) {
                    ProfileAdapter profileAdapter = new ProfileAdapter(getActivity(), parseObjects);
                    adapt = profileAdapter;
                    final GridView gridView = (GridView) getActivity().findViewById(R.id.gridProfile);
                    if (gridView != null) {
                        gridView.setAdapter(profileAdapter);
                    }
                } else {
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = (TextView) v.findViewById(R.id.usernameProfileTextView);
        name.setText(ParseUser.getCurrentUser().getUsername());
        GridView gridView = (GridView) v.findViewById(R.id.gridProfile);
        consultaAoParse();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getFragmentManager();
                DialogGrid dialogGrid = new DialogGrid();
                Bundle bundle = new Bundle();
                Produto produto = ((Produto) adapt.getItem(position));
                if(produto.getVendido()){
                    bundle.putBoolean("vendido",true);
                } else{
                    bundle.putBoolean("vendido",false);
                }
                bundle.putString("id", produto.getObjectId());
                dialogGrid.setArguments(bundle);
                dialogGrid.show(fm, "fragment_grid_item");
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}