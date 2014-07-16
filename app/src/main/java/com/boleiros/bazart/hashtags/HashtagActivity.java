package com.boleiros.bazart.hashtags;

/**
 * Created by diego on 7/16/14.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.boleiros.bazart.R;
import com.boleiros.bazart.camera.InfoFragment.OnFragmentInteractionListener;

public class HashtagActivity extends Activity implements OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_busca);
//        HashtagFragment current=new HashtagFragment();
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container, current).commit();
    }

}

