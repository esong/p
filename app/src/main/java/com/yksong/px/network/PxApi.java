package com.yksong.px.network;

import com.yksong.px.model.ApiResult;
import com.yksong.px.model.UserApiResult;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by esong on 2015-06-02.
 */
public interface PxApi {
    @GET("/photos?image_size=600&rpp=50")
    Observable<ApiResult> photos(@Query("feature") String feature);

    @GET("/photos?feature=user&sort=highest_rating&image_size=20&rpp=1")
    Observable<ApiResult> coverPhoto(@Query("user_id")int user_id);

    @GET("/users")
    Observable<UserApiResult> user();
}
