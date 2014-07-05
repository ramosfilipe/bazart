package com.boleiros.bazart;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
/**
 * Created by Filipe on 03/07/14.
 */
public class Bazart extends Application{
        @Override
        public void onCreate() {
            super.onCreate();

            // Required - Initialize the Parse SDK
            Parse.initialize(this, getString(R.string.parse_app_id),
                    getString(R.string.parse_client_key));

            Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

            // Optional - If you don't want to allow Facebook login, you can
            // remove this line (and other related ParseFacebookUtils calls)
            ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));


        }
    }

