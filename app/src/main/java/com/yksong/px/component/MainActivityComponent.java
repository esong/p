package com.yksong.px.component;

import com.yksong.px.view.MainContainer;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by esong on 2015-06-02.
 */
@Singleton
@Component(modules = {MainActivityModule.class})
public interface MainActivityComponent {
    void inject(MainContainer view);
}
