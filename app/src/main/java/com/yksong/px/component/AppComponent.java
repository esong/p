package com.yksong.px.component;

import com.yksong.px.MainActivity;
import com.yksong.px.app.AccountManager;
import com.yksong.px.view.LoginView;
import com.yksong.px.view.MainContainer;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by esong on 15-07-20.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(LoginView view);
    void inject(MainActivity activity);
}
