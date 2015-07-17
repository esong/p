package com.yksong.px.component;

import com.yksong.px.app.AccountManager;
import com.yksong.px.view.LoginView;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;

/**
 * Created by esong on 2015-05-24.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(LoginView view);
}