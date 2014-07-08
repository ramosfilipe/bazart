package com.boleiros.bazart.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.boleiros.bazart.R;
import com.commonsware.cwac.camera.CameraView;

public class CameraFragment extends com.commonsware.cwac.camera.CameraFragment {
    private static final String TAG = "CameraFragment";
    private ImageButton takePictureButton;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.camera, container, false);
        CameraView cameraView = (CameraView) content.findViewById(R.id.camera);
        setCameraView(cameraView);
        ImageButton buttonphotook= (ImageButton) content.findViewById(R.id.buttonphotook);
        buttonphotook.setVisibility(View.GONE);
        ImageButton buttonphotorestart= (ImageButton) content.findViewById(R.id.buttonphotorestart);
        buttonphotorestart.setVisibility(View.GONE);
        takePictureButton = (ImageButton) content.findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                ImageButton buttonphotook= (ImageButton) getActivity().findViewById(R.id.buttonphotook);
                buttonphotook.setVisibility(View.VISIBLE);
                ImageButton buttonphotorestart= (ImageButton) getActivity().findViewById(R.id.buttonphotorestart);
                buttonphotorestart.setVisibility(View.VISIBLE);
                buttonphotorestart.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restartPreview();
                        ImageButton buttonphotook= (ImageButton) getActivity().findViewById(R.id.buttonphotook);
                        buttonphotook.setVisibility(View.GONE);
                        ImageButton buttonphotorestart= (ImageButton) getActivity().findViewById(R.id.buttonphotorestart);
                        buttonphotorestart.setVisibility(View.GONE);
                    }
                });
            }
        });
        return (content);
    }
}