package com.boleiros.bazart.hashtags;

/**
* Created by diego on 7/15/14.
*/

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.ProdutoAdapter;
import com.boleiros.bazart.modelo.Produto;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.List;

public class HashtagFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_busca, container, false);
       consultaAoParse("diega");
        // Inflate the layout for this fragment
//        final ImageButton botaoVoltar = (ImageButton)v.findViewById(R.id.botaoVoltarFragmentBusca);
//        botaoVoltar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Feed.class);
//                startActivity(intent);
//            }
//        });
        return v;
    }

    public void consultaAoParse(String termo) {
        ParseQuery<Produto> query = ParseQuery.getQuery("Produto");
        query.include("author");

        query.whereEqualTo("tags", termo);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Produto>() {
            @Override
            public void done(List<Produto> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    ProdutoAdapter produtoAdapter = new ProdutoAdapter(getActivity(), parseObjects);
                    final ListView listaDeExibicao = (ListView) getActivity().findViewById(R.id.listaCardsBusca);
                    if (listaDeExibicao != null) {
                        System.out.println("aquuiuiu");
                        listaDeExibicao.setAdapter(produtoAdapter);
                    }
                } else {
                }
            }
        });
    }
}
