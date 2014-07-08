package com.boleiros.bazart.camera;

import android.content.Context;

import com.commonsware.cwac.camera.SimpleCameraHost;

/**
 * Created by Filipe on 08/07/14.
 */
public class CameraHostMod extends SimpleCameraHost {

    public CameraHostMod(Context _ctxt) {
        super(_ctxt);
    }
    @Override
    public boolean useSingleShotMode(){
        return true;
    }
}
