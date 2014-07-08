package com.boleiros.bazart.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.boleiros.bazart.R;
import com.commonsware.cwac.camera.CameraView;

public class CameraFragment extends com.commonsware.cwac.camera.CameraFragment {
    private static final String TAG = "CameraFragment";
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.camera, container, false);
        CameraView cameraView = (CameraView) content.findViewById(R.id.camera);

        setCameraView(cameraView);

        return (content);
    }
//    private Camera mCamera;
//    private SurfaceView mSurfaceView;
//
//    private CameraPreview mCameraPreview;
//    private View mProgressContainer;
//
//
//    @Override
//    @SuppressWarnings("deprecation")
//    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_camera, parent, false);
//
//        mProgressContainer = v.findViewById(R.id.camera_progressContainer);
//        mProgressContainer.setVisibility(View.INVISIBLE);
//        Button takePictureButton = (Button)v.findViewById(R.id.camera_takePictureButton);
//        takePictureButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
//
//        mSurfaceView = (SurfaceView)v.findViewById(R.id.camera_surfaceView);
//        SurfaceHolder holder = mSurfaceView.getHolder();
//        // deprecated, but required for pre-3.0 devices
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        holder.addCallback(new SurfaceHolder.Callback() {
//
//            public void surfaceCreated(SurfaceHolder holder) {
//                // tell the camera to use this surface as its preview area
//                try {
//                    if (mCamera != null) {
//                        mCamera.setDisplayOrientation(90);
//                        mCamera.setPreviewDisplay(holder);
//                    }
//                } catch (IOException exception) {
//                    Log.e(TAG, "Error setting up preview display", exception);
//                }
//            }
//
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                // we can no longer display on this surface, so stop the preview.
//                if (mCamera != null) {
//                    mCamera.stopPreview();
//                }
//            }
//
//            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//            	if (mCamera == null) return;
//
//                // the surface has changed size; update the camera preview size
//                Camera.Parameters parameters = mCamera.getParameters();
//                //Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
//                Size s =  getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), w, h);
//                parameters.setPreviewSize(s.width, s.height);
//                mCamera.setParameters(parameters);
//                try {
//                    mCamera.startPreview();
//                } catch (Exception e) {
//                    Log.e(TAG, "Could not start preview", e);
//                    mCamera.release();
//                    mCamera = null;
//                }
//            }
//
//        });
//
//        return v;
//    }
//
//    @TargetApi(9)
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            mCamera = Camera.open(0);
//        } else {
//            mCamera = Camera.open();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        if (mCamera != null) {
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//
//    /** a simple algorithm to get the largest size available. For a more
//     * robust version, see CameraPreview.java in the ApiDemos
//     * sample app from Android. */
//    private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
//        Size bestSize = sizes.get(0);
//        int largestArea = bestSize.width * bestSize.height;
//        for (Size s : sizes) {
//            int area = s.width * s.height;
//            if (area > largestArea) {
//                bestSize = s;
//                largestArea = area;
//            }
//        }
//        return bestSize;
//    }
//    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
//        final double ASPECT_TOLERANCE = 0.1;
//        double targetRatio = (double) h / w;
//
//        if (sizes == null)
//            return null;
//
//        Camera.Size optimalSize = null;
//        double minDiff = Double.MAX_VALUE;
//
//        int targetHeight = h;
//
//        for (Camera.Size size : sizes) {
//            double ratio = (double) size.height / size.width;
//            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
//                continue;
//
//            if (Math.abs(size.height - targetHeight) < minDiff) {
//                optimalSize = size;
//                minDiff = Math.abs(size.height - targetHeight);
//            }
//        }
//
//        if (optimalSize == null) {
//            minDiff = Double.MAX_VALUE;
//            for (Camera.Size size : sizes) {
//                if (Math.abs(size.height - targetHeight) < minDiff) {
//                    optimalSize = size;
//                    minDiff = Math.abs(size.height - targetHeight);
//                }
//            }
//        }
//
//        return optimalSize;
//    }
}
