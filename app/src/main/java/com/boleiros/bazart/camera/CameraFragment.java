package com.boleiros.bazart.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.boleiros.bazart.R;
import com.boleiros.bazart.util.ActivityStore;
import com.commonsware.cwac.camera.CameraView;

import java.io.ByteArrayOutputStream;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.ConservativeSmoothing;
import Catalano.Imaging.Filters.ContrastCorrection;
import Catalano.Imaging.Filters.Sepia;

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
        final Button buttonEdit = (Button) content.findViewById(R.id.buttonEdit);
        final Button buttonSepia = (Button) content.findViewById(R.id.buttonSepia);

        ImageButton buttonphotook = (ImageButton) content.findViewById(R.id.buttonphotook);
        buttonphotook.setVisibility(View.GONE);
        ImageButton buttonphotorestart = (ImageButton) content.findViewById(R.id
                .buttonphotorestart);
        buttonphotorestart.setVisibility(View.GONE);
        takePictureButton = (ImageButton) content.findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                takePictureButton.setVisibility(View.GONE);
                ImageButton buttonphotook = (ImageButton) getActivity().findViewById(R.id
                        .buttonphotook);
                buttonphotook.setVisibility(View.VISIBLE);
                ImageButton buttonphotorestart = (ImageButton) getActivity().findViewById(R.id
                        .buttonphotorestart);
                buttonphotorestart.setVisibility(View.VISIBLE);

                buttonphotorestart.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        restartPreview();
                        takePictureButton.setVisibility(View.VISIBLE);
                        ImageButton buttonphotook = (ImageButton) getActivity().findViewById(R.id
                                .buttonphotook);
                        buttonphotook.setVisibility(View.GONE);
                        ImageButton buttonphotorestart = (ImageButton) getActivity().findViewById
                                (R.id.buttonphotorestart);
                        buttonphotorestart.setVisibility(View.GONE);
                    }
                });
                buttonphotook.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InfoFragment current = new InfoFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, current).commit();
                    }
                });
                buttonEdit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bitmap bit = BitmapFactory.decodeByteArray(ActivityStore.getInstance(
                                getActivity()).getImage(), 0, ActivityStore.getInstance(
                                getActivity()).getImage().length);
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
                        ActivityStore.getInstance(getActivity()).setImage(scaledData);
                        InfoFragment current = new InfoFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, current).commit();
                    }
                });
                buttonSepia.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bitmap bit = BitmapFactory.decodeByteArray(ActivityStore.getInstance(
                                getActivity()).getImage(), 0, ActivityStore.getInstance(
                                getActivity()).getImage().length);
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
                        ActivityStore.getInstance(getActivity()).setImage(scaledData);
                        InfoFragment current = new InfoFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, current).commit();
                    }
                });
            }
        });
        return (content);
    }


}