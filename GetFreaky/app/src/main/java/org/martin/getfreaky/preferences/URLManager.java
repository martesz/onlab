package org.martin.getfreaky.preferences;

import android.content.SharedPreferences;

/**
 * Created by martin on 2016. 05. 07..
 */

    public class URLManager implements SharedPreferences.OnSharedPreferenceChangeListener {

        public static String BASE_URL;

        private String UrlKey;

        public URLManager(String UrlKey){
            this.UrlKey = UrlKey;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals(this.UrlKey)){
                BASE_URL = sharedPreferences.getString(key, BASE_URL);
            }
        }

    }

