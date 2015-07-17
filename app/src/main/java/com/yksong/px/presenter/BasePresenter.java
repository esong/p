package com.yksong.px.presenter;

import android.view.View;

/**
 * Created by esong on 2015-05-24.
 */
public abstract class BasePresenter<T extends View> {
    private T mView;

    public void takeView(T view) {
        if (view != null) {
            if (mView != null) {
                dropView(mView);
            }
            mView = view;
        }
    }

    public void dropView(T view) {
        if (view != null && mView == view) {
            mView = null;
        }
    }

    protected T getView() {
        return mView;
    }
}
