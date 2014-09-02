package com.boleiros.bazart.profile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boleiros.bazart.R;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.util.ActivityStore;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

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


    public void clicarNoProduto(int posicao){
        Produto produto = ((Produto) adapt.getItem(posicao));
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        ProdutoVendido produtoVendido = new ProdutoVendido();
        Bundle bundle = new Bundle();
        try{
            bundle.putByteArray("pic",produto.getPhotoFile().getData());
        }catch(Exception e){
            Toast.makeText(getActivity(),"Desculpe, ocorreu um erro.",Toast.LENGTH_SHORT).show();
        }
        bundle.putString("preco",produto.getPrice());
        bundle.putString("contato",produto.getPhoneNumber());
        String[] hashtags = produto.getArrayHashtags();
        String hasht = "";
        for (int i = 0; i < hashtags.length; i++) {
            hasht += hashtags[i] + " ";
        }
        bundle.putString("hashtags", hasht);
        produtoVendido.setArguments(bundle);
        fragmentTransaction.replace(R.id.profileActivityLayout,produtoVendido);
        fragmentTransaction.addToBackStack("Grid");
        fragmentTransaction.commit();
    }

    public void clicarNoProduto(byte[] photo,String preco, String phone, String[] hashs){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        ProdutoVendido produtoVendido = new ProdutoVendido();
        Bundle bundle = new Bundle();
        try{
            bundle.putByteArray("pic",photo);
        }catch(Exception e){
            Toast.makeText(getActivity(),"Desculpe, ocorreu um erro.",Toast.LENGTH_SHORT).show();
        }
        bundle.putString("preco",preco);
        bundle.putString("contato",phone);
        String[] hashtags = hashs;
        String hasht = "";
        for (int i = 0; i < hashtags.length; i++) {
            hasht += hashtags[i] + " ";
        }
        bundle.putString("hashtags", hasht);
        produtoVendido.setArguments(bundle);
        fragmentTransaction.replace(R.id.profileActivityLayout,produtoVendido);
        fragmentTransaction.addToBackStack("Grid");
        fragmentTransaction.commit();
    }


    public void consultaEClica(String idProduto){
        ParseQuery<Produto> query = ParseQuery.getQuery("Produto");
        query.include("author");
        query.whereEqualTo("objectId", idProduto);
        query.findInBackground(new FindCallback<Produto>() {
            @Override
            public void done(List<Produto> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    Produto produto = parseObjects.get(0);
                    try {
                        ActivityStore.getInstance(getActivity()).setUser(produto.getAuthor());
                        clicarNoProduto(produto.getPhotoFile().getData(),produto.getPrice(),produto.getPhoneNumber(),produto.getArrayHashtags());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String id;
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = (TextView) v.findViewById(R.id.usernameProfileTextView);
        ImageView profilePic = (ImageView) v.findViewById(R.id.profileProfilePic);
        ParseFile parseFile = ParseUser.getCurrentUser().getParseFile("profilePic");
        Bundle args = getArguments();
        ActivityStore.getInstance(getActivity()).setQualFragment("PROFILE");

        if (args!= null && !args.getString("produto").equals("")){
            String st = args.getString("produto");
                consultaEClica(st);
        }
        if (args != null && !args.getString("id").equals(ParseUser.getCurrentUser().getObjectId()
        )) {
            name.setText(getArguments().getString("name"));
            GridView gridView = (GridView)v.findViewById(R.id.gridProfile);
            try {
                byte[] pic = getArguments().getByteArray("pic");
                Bitmap bit = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                profilePic.setImageBitmap(bit);
            } catch (Exception e){
                profilePic.setImageResource(R.drawable.ic_launcher);
            }
            id = getArguments().getString("id");

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    clicarNoProduto(position);
                }
            });

        } else {
            GridView gridView = (GridView) v.findViewById(R.id.gridProfile);
            name.setText(ParseUser.getCurrentUser().getUsername());
            id = ParseUser.getCurrentUser().getObjectId();
            try {
                byte[] byteArray = parseFile.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                profilePic.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                profilePic.setImageResource(R.drawable.ic_launcher);
            }
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentManager fm = getFragmentManager();
                    DialogGrid dialogGrid = new DialogGrid();
                    Bundle bundle = new Bundle();
                    Produto produto = ((Produto) adapt.getItem(position));
                    if (produto.getVendido()) {
                        bundle.putBoolean("vendido", true);
                    } else {
                        bundle.putBoolean("vendido", false);
                    }
                    bundle.putString("id", produto.getObjectId());
                    dialogGrid.setArguments(bundle);
                    dialogGrid.show(fm, "fragment_grid_item");
                }
            });
        }
        new ConsultaAoParseTask(v.getContext(), id).execute();

        //consultaAoParse();


        return v;
    }

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

    public void consultaAoParse() {

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

    private class ConsultaAoParseTask extends AsyncTask<Void, Void, Void> {
        Context context;
        String userId;
        private ProgressDialog progressDialog;


        public ConsultaAoParseTask(Context ctx, String id) {
            context = ctx;
            userId = id;
        }

        @Override
        protected void onPreExecute() {
            int max = ActivityStore.getInstance(getActivity()).getFrases().size();
            Random r = new Random();
            int i1 = r.nextInt(max);
            progressDialog = ProgressDialog.show(getActivity(), null,
                    ActivityStore.getInstance(getActivity()).getFrases().get(i1));
        }



        @Override
        protected Void doInBackground(Void... params) {
            ParseQuery<Produto> query = ParseQuery.getQuery("Produto");
            query.include("author");
            query.whereEqualTo("authorStr", userId);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<Produto>() {
                public void done(List<Produto> objects, ParseException e) {
                    if (e == null) {
                        progressDialog.dismiss();
                        ProfileAdapter profileAdapter = new ProfileAdapter(getActivity(), objects);
                        adapt = profileAdapter;
                        final GridView gridView = (GridView) getActivity().findViewById(R.id
                                .gridProfile);
                        if (gridView != null) {
                            gridView.setAdapter(profileAdapter);
                        }
                    } else {
                        progressDialog.dismiss();
                        CharSequence text = "Verifique sua conexão com a internet";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            if (progressDialog != null && progressDialog.isShowing())
//                progressDialog.dismiss();
        }
    }

}