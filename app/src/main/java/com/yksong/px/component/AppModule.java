package com.yksong.px.component;

import android.app.Application;

import com.yksong.px.app.AccountManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by esong on 2015-05-25.
 */
@Module
public class AppModule {
    private final Application mApp;
    private final AccountManager mAccountManager;

    public AppModule(Application app) {
        mApp = app;
        mAccountManager = new AccountManager(mApp);
    }

    @Provides
    @Singleton
    AccountManager getAccountManager() {
        return mAccountManager;
    }
}
