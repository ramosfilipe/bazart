package com.boleiros.bazart.camera;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.boleiros.bazart.R;
import com.boleiros.bazart.util.ActivityStore;

import java.io.ByteArrayOutputStream;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.ConservativeSmoothing;
import Catalano.Imaging.Filters.ContrastCorrection;
import Catalano.Imaging.Filters.Sepia;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CameraEdit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 *
 */
public class CameraEdit extends Fragment {

    private byte[] fotoOriginal=null;
    private byte[] fotoEditada=null;

    private OnFragmentInteractionListener mListener;
    private boolean fotoParaEnviarForEditada = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container,
                                Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edicao_camera, container, false);
        final Button buttonEdit = (Button) v.findViewById(R.id.buttonEdit);
        final Button buttonDesfazer = (Button) v.findViewById(R.id.buttonDesfazerEdicao);

        final Button buttonSepia = (Button) v.findViewById(R.id.buttonSepia);
        final ImageView preview = (ImageView) v.findViewById(R.id.imageViewEdicao);
        ImageButton buttonEdicaoGo = (ImageButton) v.findViewById(R.id.buttonEdicaoGo);
        ImageButton buttonEdicaoBack = (ImageButton) v.findViewById(R.id
                .buttonBackEdicao);
        fotoOriginal = ActivityStore.getInstance(this.getActivity()).getImage();
        loadBitmap(fotoOriginal,preview);

        buttonEdicaoBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                com.commonsware.cwac.camera.CameraFragment current = new CameraFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.editCameraId, current).commit();

            }
        });
        buttonEdicaoGo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(fotoParaEnviarForEditada){
                ActivityStore.getInstance(getActivity()).setImage(fotoEditada);}
                InfoFragment current = new InfoFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.editCameraId, current).commit();
            }
        });
        buttonDesfazer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadBitmap(fotoOriginal,preview);
                fotoParaEnviarForEditada= false;

            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bitmap bit = BitmapFactory.decodeByteArray(fotoOriginal, 0, fotoOriginal.length);
                FastBitmap fb = new FastBitmap(bit);
                //SaturationCorrection fk = new SaturationCorrection();
                // BrightnessCorrection fk1 = new BrightnessCorrection();
                ContrastCorrection fk2 = new ContrastCorrection();
                ConservativeSmoothing fk1 = new ConservativeSmoothing();
                // fk.applyInPlace(fb);
                fk1.applyInPlace(fb);
                fk2.applyInPlace(fb);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                fb.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] scaledData = bos.toByteArray();
                fotoEditada = scaledData;
                loadBitmap(scaledData,preview);
                fotoParaEnviarForEditada= true;


            }
        });
        buttonSepia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bitmap bit = BitmapFactory.decodeByteArray(fotoOriginal, 0,fotoOriginal.length);
                FastBitmap fb = new FastBitmap(bit);
                //SaturationCorrection fk = new SaturationCorrection();
                // BrightnessCorrection fk1 = new BrightnessCorrection();
                Sepia fk2 = new Sepia();

                // fk.applyInPlace(fb);
                //  fk1.applyInPlace(fb);
                fk2.applyInPlace(fb);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                fb.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] scaledData = bos.toByteArray();
                fotoEditada = scaledData;

                loadBitmap(scaledData,preview);
                fotoParaEnviarForEditada= true;

            }
        });
        return v;
    }

    void loadBitmap(byte[] foto, ImageView imageView) {
        final BitmapWorker task = new BitmapWorker(imageView);
        task.execute(foto);
    }

    class BitmapWorker extends AsyncTask<byte[], Void, Bitmap> {
        private ImageView imageViewReference;

        public BitmapWorker(ImageView imageView) {
            imageViewReference = imageView;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(byte[]... params) {

            return BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                imageViewReference.setImageBitmap(bitmap);
            }
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
