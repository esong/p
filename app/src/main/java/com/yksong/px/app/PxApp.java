package com.yksong.px.app;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;
import com.yksong.px.BuildConfig;
import com.yksong.px.component.AppComponent;
import com.yksong.px.component.AppModule;
import com.yksong.px.component.DaggerAppComponent;
import com.yksong.px.component.LoginComponent;

/**
 * Created by esong on 2015-04-26.
 */
public class PxApp extends Application {
    AppComponent mComponent;
    LoginComponent mLoginComponent;

    @Override
    public void onCreate() {
        buildComponentAndInject();

        if (BuildConfig.DEBUG) {
            // Picasso.with(this).setIndicatorsEnabled(true);
        }
    }

    public static AppComponent getAppComponent(Context context) {
        return ((PxApp) context.getApplicationContext()).mComponent;
    }

    public static LoginComponent getLoginComponent(Context context) {
        return ((PxApp) context.getApplicationContext()).mLoginComponent;
    }

    public static void setLoginComponent(Context context, LoginComponent component) {
        ((PxApp) context.getApplicationContext()).mLoginComponent = component;
    }

    public void buildComponentAndInject() {
        mComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
