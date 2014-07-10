package com.boleiros.bazart.util;

import android.content.Context;

public class ActivityStore {
    private static ActivityStore sActivityStore;
    private byte[] image ;
    private boolean terminou= false;
    private Context mContext;

    private ActivityStore(Context ctx) {
        mContext = ctx;
    }

    public static ActivityStore getInstance(Context ctx) {
        if(sActivityStore == null) {
            sActivityStore = new ActivityStore(ctx.getApplicationContext());
        }
        return sActivityStore;
    }
    public boolean getEstado(){
        return terminou;
    }
    public byte[] getImage() {
        terminou = false;
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
        terminou= true;
    }
}