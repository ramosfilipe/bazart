package com.boleiros.bazart.util;

import android.view.View;

/**
 * Created by Walter on 01/08/2014.
 */
public abstract class DoubleClickListener implements View.OnClickListener {

    private final long DOUBLE_CLICK_TIME_MILIS = 500;
    private long ultimoClick = 0;

    public void onClick(View v) {
        long tempoClick = System.currentTimeMillis();
        if (tempoClick - ultimoClick <= DOUBLE_CLICK_TIME_MILIS) {
            onDoubleClick(v);
        }
        ultimoClick = tempoClick;
    }

    public abstract void onDoubleClick(View v);
}
