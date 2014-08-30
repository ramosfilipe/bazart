package com.boleiros.bazart.camera.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.boleiros.bazart.R;
import com.boleiros.bazart.camera.CameraFragment;
import com.boleiros.bazart.camera.InfoFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class EdicaoCamera extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edicao_camera);
        ImageView preview = (ImageView) findViewById(R.id.imageViewEdicao);
        ImageButton buttonEdicaoGo = (ImageButton) findViewById(R.id.buttonEdicaoGo);
        ImageButton buttonEdicaoBack = (ImageButton) findViewById(R.id
                .buttonBackEdicao);
        //loadBitmap(ActivityStore.getInstance(this).getImage(),preview);

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
                InfoFragment current = new InfoFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.editCameraId, current).commit();
            }
        });

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


}
