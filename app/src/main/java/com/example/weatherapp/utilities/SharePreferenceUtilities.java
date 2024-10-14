package com.example.weatherapp.utilities;

import android.content.SharedPreferences;

public class SharePreferenceUtilities {

        public void setSp( String key, String city, SharedPreferences sp) {
            set(key, city, sp.edit());
        }

        public void set( String key, String value,SharedPreferences.Editor editor) {
            editor.putString(key, value);
            editor.commit();
        }


        public String getSp(String key, SharedPreferences sp) {
            return sp.getString(key, null);
        }

}
