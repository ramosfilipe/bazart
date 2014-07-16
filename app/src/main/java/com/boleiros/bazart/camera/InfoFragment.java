package com.boleiros.bazart.camera;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.util.Rfc822Tokenizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.Feed;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.util.ActivityStore;
import com.boleiros.bazart.util.CustomRecipients;
import com.boleiros.bazart.util.NumericRangeFilter;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;


public class InfoFragment extends Fragment {
// --Commented out by Inspection START (13/07/14 02:24):
// --Commented out by Inspection START (13/07/14 02:24):
////    // TODO: Rename parameter arguments, choose names that match
////    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
////    private static final String ARG_PARAM1 = "param1";
//// --Commented out by Inspection STOP (13/07/14 02:24)
//    private static final String ARG_PARAM2 = "param2";
// --Commented out by Inspection STOP (13/07/14 02:24)
    private static final InputFilter[] FILTERS = new InputFilter[] {new NumericRangeFilter(0.00, 999999.99)};
    // --Commented out by Inspection (13/07/14 02:21):private static final View.OnFocusChangeListener ON_FOCUS = new AmountOnFocusChangeListener();
// --Commented out by Inspection START (13/07/14 02:22):
// --Commented out by Inspection START (13/07/14 02:22):
////    // TODO: Rename and change types of parameters
////    private String mParam1;
//// --Commented out by Inspection STOP (13/07/14 02:22)
//    private String mParam2;
// --Commented out by Inspection STOP (13/07/14 02:22)

    // --Commented out by Inspection (13/07/14 02:24):private OnFragmentInteractionListener mListener;

// --Commented out by Inspection START (13/07/14 02:22):
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment InfoFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static InfoFragment newInstance(String param1, String param2) {
//        InfoFragment fragment = new InfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//
//    }
// --Commented out by Inspection STOP (13/07/14 02:22)
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        EditText telefone = (EditText) v.findViewById(R.id.editTextPhoneNumber);
        telefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        EditText preco = (EditText) v.findViewById(R.id.editTextPreco);

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
                if (hashtags.getSize() > 3) {
                    Toast.makeText(getActivity(),"Insira no máximo 3 hashtags",Toast.LENGTH_SHORT).show();
                } else {
                    botaoEnvia.setVisibility(View.GONE);
                    ParseFile photoFile = new ParseFile("fotoProduto.jpg", ActivityStore.getInstance(getActivity()).
                            getImage());
                    EditText preco1 = (EditText) getActivity().findViewById(R.id.editTextPreco);
                    EditText telefone1 = (EditText) getActivity().findViewById(R.id.editTextPhoneNumber);
//                String precoStr = preco.getText().toString();
//                if(precoStr.contains(".")){
//                    precoStr.replace(".",",");
//                    precoStr = "R$ "+precoStr;
//                } else {
//                    precoStr = "R$ " + precoStr + ",00";
//                }
                    //  EditText hashtags = (EditText)v.findViewById(R.id.editTextPreco);
                    Produto produto = new Produto();

                    produto.setAuthor(ParseUser.getCurrentUser());
                    produto.setPhotoFile(photoFile);
                    produto.setPhoneNumber(telefone1.getText().toString());
                    produto.setPrice(preco1.getText().toString());
                    produto.setHashTags(hashtags.getJSONArray());
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

// --Commented out by Inspection START (13/07/14 02:22):
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
// --Commented out by Inspection STOP (13/07/14 02:22)

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
// --Commented out by Inspection START (13/07/14 02:25):
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
// --Commented out by Inspection STOP (13/07/14 02:25)
    }
    void loadBitmap(byte[] foto, ImageView imageView) {
            final BitmapWorker task = new BitmapWorker(imageView);
            task.execute(foto);
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
