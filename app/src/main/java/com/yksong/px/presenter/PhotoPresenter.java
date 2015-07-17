package com.yksong.px.presenter;

import com.yksong.px.model.ApiResult;
import com.yksong.px.network.PxApi;
import com.yksong.px.view.PhotoListView;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by esong on 2015-06-02.
 */
@Singleton
public class PhotoPresenter extends BasePresenter<PhotoListView> {
    @Inject PxApi mApi;

    @Inject
    public PhotoPresenter(){

    }

    public void request() {
        mApi.popularPhotos().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ApiResult apiResult) {
                        getView().displayPhotos(apiResult.photos);
                    }
                });
    }
}
