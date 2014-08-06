package com.boleiros.bazart.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.boleiros.bazart.R;
import com.boleiros.bazart.camera.InfoFragment.OnFragmentInteractionListener;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;

public class CameraActivity extends Activity implements CameraHostProvider,
        OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        com.commonsware.cwac.camera.CameraFragment current = new CameraFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, current).commit();
    }

    @Override
    public CameraHost getCameraHost() {
        return (new CameraHostMod(this));
    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
}

