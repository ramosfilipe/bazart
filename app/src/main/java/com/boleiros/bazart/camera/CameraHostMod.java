package com.boleiros.bazart.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import com.boleiros.bazart.util.ActivityStore;
import com.commonsware.cwac.camera.CameraUtils;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;

import java.io.ByteArrayOutputStream;

/**
 * Created by Filipe on 08/07/14.
 */
public class CameraHostMod extends SimpleCameraHost {
    Context context;
    public CameraHostMod(Context _ctxt) {

        super(_ctxt);
        context =_ctxt;
    }
    @Override
    public boolean useSingleShotMode(){
        return true;
    }
    @Override
    public boolean useFullBleedPreview(){
        return true;
    }

    @Override
    public void saveImage(PictureTransaction xact, byte[] image) {

        ActivityStore.getInstance(context).setImage(cropImage(image));
        //ActivityStore.getInstance(context).setImage(image);
    }


    public byte[] cropImage(byte[] image){

        Bitmap bit = BitmapFactory.decodeByteArray(image, 0, image.length);


        int initHeight = (int)(0.20833333*bit.getHeight());
        int endHeight = (int)(0.555555556*bit.getHeight());
        int endWidth = (int)(0.76388889*bit.getHeight())-initHeight;

        if(endWidth>bit.getWidth()){
            endWidth = bit.getWidth();
        }
        System.out.println("aqui"+initHeight+" "+endHeight);

        Bitmap cropped = Bitmap.createBitmap(bit,0,initHeight,endWidth,endHeight);
        Bitmap bit1 = Bitmap.createScaledBitmap(cropped,650,650,false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit1.compress(Bitmap.CompressFormat.JPEG, 100, bos);


        byte[] scaledData = bos.toByteArray();
        return  scaledData;
    }


    /*
         * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
         * they are saved. Since we never need a full-size image in our app, we'll
         * save a scaled one right away.
         */



//    private byte[] saveScaledPhoto(byte[] data) {
//
//        // Resize photo from camera byte array
//        Bitmap mealImage = BitmapFactory.decodeByteArray(data, 0, data.length);
//        Bitmap mealImageScaled = Bitmap.createScaledBitmap(mealImage, 200, 200
//                * mealImage.getHeight() / mealImage.getWidth(), false);
//
//        // Override Android default landscape orientation and save portrait
//        Matrix matrix = new Matrix();
//        matrix.postRotate(90);
//        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mealImageScaled, 0,
//                0, mealImageScaled.getWidth(), mealImageScaled.getHeight(),
//                matrix, true);
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//
//          byte[] scaledData = bos.toByteArray();
//        return  scaledData;
//    }
    @Override
    public Camera.Size getPictureSize(PictureTransaction xact,
                                      Camera.Parameters parameters) {

        return (CameraUtils.getLargestPictureSize(this,parameters));
        //return(CameraUtils.getSmallestPictureSize(parameters));
    }
    /*
     * Once the photo has saved successfully, we're ready to return to the
     * NewMealFragment. When we added the CameraFragment to the back stack, we
     * named it "NewMealFragment". Now we'll pop fragments off the back stack
     * until we reach that Fragment.
     */

}
