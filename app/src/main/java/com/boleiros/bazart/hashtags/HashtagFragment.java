package com.boleiros.bazart.hashtags;

/**
 * Created by diego on 7/15/14.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boleiros.bazart.R;

public class HashtagFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_busca, container, false);
        // Inflate the layout for this fragment
//        final ImageButton botaoVoltar = (ImageButton)v.findViewById(R.id.botaoVoltarFragmentBusca);
//        botaoVoltar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Feed.class);
//                startActivity(intent);
//            }
//        });
        return v;
    }
}
