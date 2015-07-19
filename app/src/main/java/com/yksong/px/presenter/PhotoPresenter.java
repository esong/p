package com.yksong.px.presenter;

import com.yksong.px.R;
import com.yksong.px.model.ApiResult;
import com.yksong.px.network.PxApi;
import com.yksong.px.view.PhotoListView;

import java.util.HashMap;
import java.util.Map;

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

    static Map<Integer, String> photoFeatureMap = new HashMap<>();

    static {
        photoFeatureMap.put(R.id.navigation_editor, "editors");
        photoFeatureMap.put(R.id.navigation_fresh, "fresh_today");
        photoFeatureMap.put(R.id.navigation_upcoming, "upcoming");
        photoFeatureMap.put(R.id.navigation_popular, "popular");
    }

    @Inject
    public PhotoPresenter(){

    }

    public void request(int listId) {
        String feature = photoFeatureMap.get(listId);
        mApi.photos(feature).subscribeOn(Schedulers.newThread())
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
