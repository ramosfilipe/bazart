package com.boleiros.bazart.camera;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.boleiros.bazart.camera.feed.Feed;
import com.boleiros.bazart.Produto;
import com.boleiros.bazart.R;
import com.boleiros.bazart.util.ActivityStore;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ImageView preview = (ImageView) v.findViewById(R.id.previewAntesDeEnviar);

        Bitmap bit = BitmapFactory.decodeByteArray((ActivityStore.getInstance(this.getActivity()).
                getImage()), 0, (ActivityStore.getInstance(this.getActivity()).
                getImage().length));
        preview.setImageBitmap(bit);
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
                botaoEnvia.setVisibility(View.GONE);
                ParseFile photoFile = new ParseFile("fotoProduto.jpg", ActivityStore.getInstance(getActivity()).
                        getImage());
                EditText preco = (EditText) getActivity().findViewById(R.id.editTextPreco);
                EditText telefoneDDD = (EditText)getActivity().findViewById(R.id.editTextTelefoneDDD);
                EditText telefonePrefixo = (EditText)getActivity().findViewById(R.id.editTextTelefoneEsquerda);
                EditText telefoneSufixo = (EditText)getActivity().findViewById(R.id.editTextTelefoneDireita);
                String telefone = "("+telefoneDDD.getText().toString()+") "+telefonePrefixo.getText().toString()+" - "+ telefoneSufixo.getText().toString();


                String precoStr = preco.getText().toString();
                if(precoStr.contains(".")){
                    precoStr.replace(".",",");
                    precoStr = "R$ "+precoStr;
                } else {
                    precoStr = "R$ " + precoStr + ",00";
                }

              //  EditText hashtags = (EditText)v.findViewById(R.id.editTextPreco);
                Produto produto = new Produto();
                produto.setAuthor(ParseUser.getCurrentUser());
                produto.setPhotoFile(photoFile);
                produto.setPhoneNumber(telefone);
                produto.setPrice(precoStr);
                produto.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(),"Produto anunciado!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(),Feed.class);
                            startActivity(intent);                        }
                    }



                });
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
