package com.boleiros.bazart.util;

import android.content.Context;

import java.util.ArrayList;

public class ActivityStore {
    private static ActivityStore sActivityStore;
    private byte[] image;
    private boolean terminou = false;
    private ArrayList<String> frases;
    private Context mContext;

    private ActivityStore(Context ctx) {
        mContext = ctx;
    }

    public static ActivityStore getInstance(Context ctx) {
        if (sActivityStore == null) {
            sActivityStore = new ActivityStore(ctx.getApplicationContext());
        }
        return sActivityStore;
    }

    public ArrayList<String> getFrases() {
        if (frases == null) {
            System.out.println("teste121212");
            frases = new ArrayList<String>();
        }
        if (frases.size() == 0) {

            frases.add("Carregando");
        }
        return frases;
    }

    public void setFrases(ArrayList<String> frases) {
        this.frases = frases;
    }

    public boolean getEstado() {
        return terminou;
    }

    public byte[] getImage() {
        terminou = false;
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
        terminou = true;
    }
}