package com.boleiros.bazart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by diego on 7/8/14.
 */
public class CustomCard extends Card {

    protected TextView viewPreco, viewContato, viewHashtags;
    Produto produto;
    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public CustomCard(Context context) {
        this(context, R.layout.inside_card);
        this.produto = produto;
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

//        //Retrieve elements
//        viewPreco = (TextView) parent.findViewById(R.id.preco);
//        if (viewPreco!=null)
//            viewPreco.setText(produto.get);
    }
}