package com.boleiros.bazart;

import android.app.Application;

import com.boleiros.bazart.modelo.Produto;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by Filipe Ramos on 03/07/14.
 */
public class Bazart extends Application {
    // Debugging tag for the application
    public static final String APPTAG = "AnyWall";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Produto.class);
        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id_GOOGLEPLAY),
                getString(R.string.parse_client_key_GOOGLEPLAY));

        //Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Optional - If you don't want to allow Facebook login, you can
        // remove this line (and other related ParseFacebookUtils calls)
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));


    }
}

