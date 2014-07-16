package com.boleiros.bazart.hashtags;

/**
 * Created by diego on 7/15/14.
 */

import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.boleiros.bazart.R;
import com.boleiros.bazart.camera.CameraActivity;
import com.boleiros.bazart.feed.Feed;

public class HashtagFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        final ImageButton botaoVoltar = (ImageButton)v.findViewById(R.id.botaoVoltarFragmentBusca);
//        botaoVoltar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Feed.class);
//                startActivity(intent);
//            }
//        });
        return inflater.inflate(R.layout.fragment_busca, container, false);
    }
}
