package com.boleiros.bazart.profile;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.DialogPhoneOptions;
import com.boleiros.bazart.feed.Feed;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.util.ActivityStore;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProdutoVendido.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProdutoVendido#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ProdutoVendido extends Fragment {
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
     * @return A new instance of fragment ProdutoVendido.
     */
    // TODO: Rename and change types and number of parameters
    public static ProdutoVendido newInstance(String param1, String param2) {
        ProdutoVendido fragment = new ProdutoVendido();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ProdutoVendido() {
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
    public void showPhoneOptions(String phone){
        FragmentManager fm = getFragmentManager();
        DialogPhoneOptionsProfile dialogGrid = new DialogPhoneOptionsProfile();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        dialogGrid.setArguments(bundle);
        dialogGrid.show(fm, "fragment_dialog_phone_options_profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_produto_vendidoo, container, false);
        Bundle args = getArguments();
        ActivityStore.getInstance(getActivity()).setQualFragment("PRODUTO");

        ImageView imgProduto = (ImageView)v.findViewById(R.id.imageViewProdutoVendido);
        TextView precoProduto = (TextView)v.findViewById(R.id.textviewVendidoPor);
        TextView contatoProduto = (TextView)v.findViewById(R.id.textViewContatoVendido);
        TextView hashtagsProduto = (TextView)v.findViewById(R.id.textViewHashtagsVendido);
        byte[] img = args.getByteArray("pic");

        final String contato = args.getString("contato");
        SpannableString span = new SpannableString(contato);
        ClickableSpan clickPhone = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showPhoneOptions(contato);
            }
        };
        span.setSpan(clickPhone,0,span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        contatoProduto.setText(span);
        contatoProduto.setMovementMethod(LinkMovementMethod.getInstance());


        imgProduto.setImageBitmap(BitmapFactory.decodeByteArray(img,0,img.length));
        precoProduto.setText(args.getString("preco"));
        hashtagsProduto.setText(args.getString("hashtags"));

        return v ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
