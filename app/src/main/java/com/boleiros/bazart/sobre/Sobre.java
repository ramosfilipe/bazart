package com.boleiros.bazart.sobre;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.Feed;

import java.util.ArrayList;
import java.util.List;

public class Sobre extends Activity {

    private final String VERSAO = "1.2.0";
    private final String DESENVOLVEDORES = "Filipe Ramos, Diego Coelho, Walter Alves e " +
            "Emilio Farias";
    private final String LICENCA_COD_ABERTO = "1.0";
    private List<String[]> itens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ListView lvSobre = (ListView) findViewById(R.id.listView_sobre);
//        ArrayList<String> elementos = new ArrayList<String>();
//        elementos.add(VERSAO);
//        elementos.add(DESENVOLVEDORES);
//        elementos.add(LICENCA_COD_ABERTO);
//        ArrayAdapter<String> aaSobre = new ArrayAdapter<String>(this,
// android.R.layout.simple_list_item_1,elementos);
//        lvSobre.setAdapter(aaSobre);
        itens = new ArrayList<String[]>();
        String[] temp = {"Versão:", VERSAO};
        String[] temp1 = {"Produzido por:", DESENVOLVEDORES};

        itens.add(temp);
        itens.add(temp1);


        SobreAdapter sbAdapter = new SobreAdapter(this, itens);
        lvSobre.setAdapter(sbAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.sobre, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Feed.class);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, Feed.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
