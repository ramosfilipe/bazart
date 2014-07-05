package com.boleiros.bazart;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.boleiros.bazart.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraDemo extends Activity {
    private static final String TAG = "CameraDemo";
    Camera camera;
    Preview preview;
    android.widget.Button buttonClick;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_demo);

        preview = new Preview(this);
        ((FrameLayout) findViewById(R.id.preview)).addView(preview);

        buttonClick = (Button) findViewById(R.id.buttonClick);
        buttonClick.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });

        Log.d(TAG, "onCreate'd");
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };

    /** Handles data for raw picture */
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    /** Handles data for jpeg picture */
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;

            long time = 0;
            try {
                // write to local sandbox file system
//                outStream = CameraDemo.this.openFileOutput(String.format("%d.jpg", System.currentTimeMillis()), 0);
                // Or write to sdcard
                time =  System.currentTimeMillis();
                outStream = new FileOutputStream(String.format("/sdcard/%d.jpg",time));
                outStream.write(data);
                outStream.close();
                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {



            }
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

}

