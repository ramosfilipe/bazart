package com.boleiros.bazart.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import com.boleiros.bazart.util.ActivityStore;
import com.boleiros.bazart.util.ScalingUtilities;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Filipe on 08/07/14.
 */
public class CameraHostMod extends SimpleCameraHost {
    Context context;

    public CameraHostMod(Context _ctxt) {

        super(_ctxt);
        context = _ctxt;
    }

    @Override
    public boolean useSingleShotMode() {
        return true;
    }

    @Override
    public boolean useFullBleedPreview() {
        return true;
    }

    @Override
    public void saveImage(PictureTransaction xact, byte[] image) {
        // ActivityStore.getInstance(context).setImage(cropImage(image));
        //ActivityStore.getInstance(context).setImage(image);

        ActivityStore.getInstance(context).setImage(cropButtonPressed(image));

    }

    protected byte[] cropButtonPressed(byte[] image) {
//        Bitmap teste = BitmapFactory.decodeByteArray(image,0,image.length);
        //  System.out.println("tamanho :" + image.length );
//        System.out.println("crop width:"+teste.getWidth() );
//        System.out.println("crop heitgh:"+teste.getHeight() );

        // Part 1: Decode image
        Bitmap unscaledBitmap = ScalingUtilities.decodeResource(context.getResources(), image,
                650, 650, ScalingUtilities.ScalingLogic.CROP);

        // Part 2: Scale image
        Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 650,
                650, ScalingUtilities.ScalingLogic.CROP);
        unscaledBitmap.recycle();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] scaledData = bos.toByteArray();
        //System.out.println("tamanho 2 :" + scaledData.length );

        return scaledData;
    }

    public byte[] cropImage(byte[] image) {

        Bitmap bit = BitmapFactory.decodeByteArray(image, 0, image.length);


        int initHeight = (int) (0.20833333 * bit.getHeight());
        int endHeight = (int) (0.555555556 * bit.getHeight());
        int endWidth = (int) (0.76388889 * bit.getHeight()) - initHeight;

        if (endWidth > bit.getWidth()) {
            endWidth = bit.getWidth();
        }

        Bitmap cropped = Bitmap.createBitmap(bit, 0, initHeight, endWidth, endHeight);
        Bitmap bit1 = Bitmap.createScaledBitmap(cropped, 650, 650, false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit1.compress(Bitmap.CompressFormat.JPEG, 0, bos);


        byte[] scaledData = bos.toByteArray();
        return scaledData;
    }

    @Override
    public Camera.Size getPictureSize(PictureTransaction xact,
                                      Camera.Parameters parameters) {

        //return (CameraUtils.getLargestPictureSize(this,parameters));
        //return(CameraUtils.getSmallestPictureSize(parameters));
        List<Camera.Size> list = parameters.getSupportedPictureSizes();
        int targetWidth = 1944;
        int width;
        Camera.Size optimalSize = null;
        for (Camera.Size size : list) {
            width = size.width;
            if (width == targetWidth) {
                //System.out.println("IF1 :"+width );
                optimalSize = size;
                return optimalSize;
            } else if (width < targetWidth) {
                //System.out.println("IF2 :"+width );

                optimalSize = size;
                return optimalSize;
            } else if (width > targetWidth) {
                //  System.out.println("IF3 :"+width );

                optimalSize = size;
            }
        }

        return optimalSize;
    }
}
