package com.boleiros.bazart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boleiros.bazart.util.LruCacheUtil;
import com.parse.ParseException;
import com.parse.ParseFile;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by diego on 7/8/14.
 */
public class CustomCard extends Card {
    protected ImageView viewFoto;
    protected TextView viewPreco, viewContato, viewHashtags;
    String preco, contato;
    String indexFoto;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public CustomCard(Context context, String preco, String contato, String indexFoto) {
        this(context, R.layout.inside_card);
        this.preco = preco;
        this.contato = contato;
        this.indexFoto = indexFoto;
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public CustomCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //Retrieve elements
        viewPreco = (TextView) parent.findViewById(R.id.preco);
        if (viewPreco!=null)
            viewPreco.setText(preco);
        viewContato = (TextView)parent.findViewById(R.id.contato);
        if(viewContato!=null)
            viewContato.setText(contato);
        viewFoto = (ImageView)parent.findViewById(R.id.imageViewCard);
        if(viewFoto!=null)
            viewFoto.setImageBitmap(LruCacheUtil.getInstance(getContext()).getBitmap(indexFoto));
    }
}