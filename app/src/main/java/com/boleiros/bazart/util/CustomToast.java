package com.boleiros.bazart.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.boleiros.bazart.R;

/**
 * Created by Walter on 04/08/2014.
 */
public class CustomToast extends Toast {
    private static final String TAG = "MyToast";


    public CustomToast(Context context, CharSequence text, int duration) {
        super(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);


        View view = inflate.inflate(R.layout.my_toast, null);


        this.setView(view);
        this.setDuration(duration);
        this.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL,
                (int) convertDpToPixel(50, context));


    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        return new CustomToast(context, text, duration);
    }

    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    private static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

//    @Override
//    public void show() {
//        super.show();
//    }
}
