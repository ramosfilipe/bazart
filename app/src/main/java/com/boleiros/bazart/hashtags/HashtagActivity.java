package com.boleiros.bazart.hashtags;

/**
 * Created by diego on 7/16/14.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.boleiros.bazart.R;
import com.boleiros.bazart.camera.InfoFragment.OnFragmentInteractionListener;

public class HashtagActivity extends Activity implements OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_busca);
//        HashtagFragment current=new HashtagFragment();
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container, current).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buscahashtag, menu);
        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setIconifiedByDefault(false);
        //searchViewItem.setVisible(true);

        return true;
    }
}

