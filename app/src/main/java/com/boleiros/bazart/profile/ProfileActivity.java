package com.boleiros.bazart.profile;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.Feed;
import com.boleiros.bazart.sobre.Sobre;
import com.boleiros.bazart.util.ActivityStore;

public class ProfileActivity extends Activity implements Profile.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Profile current = new Profile();
        current.setArguments(getIntent().getExtras());
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(R.id.profileActivityLayout, current).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sobre) {
            Intent intent = new Intent(this, Sobre.class);
            startActivity(intent);
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        System.out.println("OAI "+ActivityStore.getInstance(this).getRemoveu());
        if( ActivityStore.getInstance(this).getRemoveu()){
            ActivityStore.getInstance(this).setRemoveu(false);
            Intent intent = new Intent(this, Feed.class);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
