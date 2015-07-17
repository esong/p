package com.yksong.px.component;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import com.yksong.px.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by esong on 2015-06-02.
 */
@Module
public class MainActivityModule {
    private MainActivity mActivity;

    public MainActivityModule(MainActivity activity) {
        mActivity = activity;
    }

    @Provides
    @Singleton
    DrawerLayout getDrawerLayout() {
        return mActivity.getDrawerLayout();
    }

    @Provides
    @Singleton
    NavigationView getNavigationView() {
        return mActivity.getNavigationView();
    }
}
