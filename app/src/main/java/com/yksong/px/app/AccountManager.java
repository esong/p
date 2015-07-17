package com.yksong.px.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.fivehundredpx.api.auth.AccessToken;

/**
 * Created by esong on 2015-05-25.
 */
public class AccountManager {
    private final Application mApp;
    private final SharedPreferences mPreference;
    private AccessToken mAccessToken = null;

    public AccountManager(Application app) {
        mApp = app;
        mPreference = app.getSharedPreferences(Preferences.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public AccessToken getAccessToken(){
        if (mAccessToken == null) {
            mAccessToken = new AccessToken(mPreference.getString(Preferences.PREF_ACCESS_TOKEN, "")
                    , mPreference.getString(Preferences.PREF_TOKEN_SECRET, ""));
        }

        return mAccessToken;
    }

    public void storeAccessToken(AccessToken token) {
        mAccessToken = token;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(Preferences.PREF_ACCESS_TOKEN, token.getToken());
        editor.putString(Preferences.PREF_TOKEN_SECRET, token.getTokenSecret());
        editor.commit();
    }
}
