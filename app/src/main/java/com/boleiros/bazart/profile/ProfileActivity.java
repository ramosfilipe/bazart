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
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.sobre.Sobre;
import com.boleiros.bazart.util.ActivityStore;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

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


    public void changeActProfile(ParseUser user,String produto, final boolean bool) {
        final String product = produto;
        Intent intent = new Intent(this, ProfileActivity.class);
        if (user == null) {
            ParseQuery<Produto> query = ParseQuery.getQuery("Produto");
            query.include("author");
            query.whereEqualTo("objectId", produto);
            query.findInBackground(new FindCallback<Produto>() {
                @Override
                public void done(List<Produto> parseObjects, com.parse.ParseException e) {
                    if (e == null) {
                        ParseUser user1 = parseObjects.get(0).getAuthor();
                        changeActProfile(user1,product, bool);
                    }
                }
            });
        } else {
            intent.putExtra("name", user.getUsername());
            intent.putExtra("id", user.getObjectId());
            intent.putExtra("produto", produto);
            intent.putExtra("web",false);
            try {
                intent.putExtra("pic", user.getParseFile("profilePic").getData());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if(ActivityStore.getInstance(this).getQualFragm().equals("PROFILE")){
            Intent intent = new Intent(this, Feed.class);
            startActivity(intent);
        }
        if( ActivityStore.getInstance(this).getRemoveu()){
            ActivityStore.getInstance(this).setRemoveu(false);
            Intent intent = new Intent(this, Feed.class);
            startActivity(intent);
        } else {
            Boolean bool;
            try{
                bool = getIntent().getExtras().getBoolean("web");
            }catch (Exception e){
                bool = null;
            }
            if (bool == null) {
                super.onBackPressed();
            } else {
                if (getIntent().getExtras().getBoolean("web")) {
                    if (ActivityStore.getInstance(this).getUser() != null) {
                        changeActProfile(ActivityStore.getInstance(this).getUser(), "", false);
                    }
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
