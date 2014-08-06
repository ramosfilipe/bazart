package com.boleiros.bazart.util;

import android.content.Context;
import android.view.animation.Animation;
import android.widget.LinearLayout;

/**
 * Created by Walter on 05/08/2014.
 */
public class FrameAnimated extends LinearLayout {

    private Animation inAnimation;
    private Animation outAnimation;

    public FrameAnimated(Context context) {
        super(context);
    }

    public void setInAnimation(Animation inAnimation) {
        this.inAnimation = inAnimation;
    }

    public void setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            if (visibility == VISIBLE && inAnimation != null) {
                startAnimation(inAnimation);
            } else if ((((visibility == INVISIBLE) || (visibility == GONE)) && outAnimation !=
                    null)) {
                startAnimation(outAnimation);
            }
        }

        super.setVisibility(visibility);
    }
}
