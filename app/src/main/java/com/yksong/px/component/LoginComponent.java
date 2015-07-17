package com.yksong.px.component;

import com.yksong.px.view.DrawerHeaderView;
import com.yksong.px.view.PhotoListView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by esong on 2015-06-02.
 */
@Singleton
@Component (modules = {LoginModule.class})
public interface LoginComponent {
    void inject(PhotoListView p);
    void inject(DrawerHeaderView v);
}
