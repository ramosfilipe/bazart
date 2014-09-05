package com.boleiros.bazart.util;

import android.content.Context;

import com.parse.ParseUser;

import java.util.ArrayList;

public class ActivityStore {
    private static ActivityStore sActivityStore;
    private byte[] image;
    private boolean terminou = false;
    private ArrayList<String> frases;
    private Context mContext;
    private boolean removeu = false;
    private ParseUser user;
    private String qualFragm = "";

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

    public void setQualFragment(String frag) {
        this.qualFragm = frag;
    }

    public String getQualFragm() {
        return this.qualFragm;
    }

    public ParseUser getUser() {
        return this.user;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }

    public boolean getRemoveu() {
        return this.removeu;
    }

    public void setRemoveu(boolean bol) {
        this.removeu = bol;
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