package com.boleiros.bazart.hashtags;

/**
 * Created by diego on 7/16/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.boleiros.bazart.R;
import com.boleiros.bazart.camera.InfoFragment.OnFragmentInteractionListener;
import com.boleiros.bazart.feed.ProdutoAdapter;
import com.boleiros.bazart.modelo.Produto;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.List;

public class HashtagActivity extends Activity implements OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buscahashtag, menu);


        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setIconifiedByDefault(false);
        Intent intent = getIntent();
        if (intent.hasExtra("busca")) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            String str = intent.getExtras().getString("busca");
            consultaAoParse(str);
            searchView.setQuery(str, false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (!query.contains("#")) {
                    query = "#" + query;
                }
                consultaAoParse(query.toLowerCase().replace(" ", ""));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //searchViewItem.setVisible(true);


        return true;
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

                    ProdutoAdapter produtoAdapter = new ProdutoAdapter(getBaseContext(), parseObjects);
                    final ListView listaDeExibicao = (ListView) findViewById(R.id.listaCardsBusca);
                    if (listaDeExibicao != null) {
                        listaDeExibicao.setAdapter(produtoAdapter);
                    }
                    if (parseObjects.size() == 0) {
                        Toast.makeText(getBaseContext(), "Não foi encontrado nenhum produto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
        });
    }

}

