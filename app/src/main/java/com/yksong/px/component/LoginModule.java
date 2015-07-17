package com.yksong.px.component;

import com.fivehundredpx.api.auth.AccessToken;
import com.yksong.px.model.User;
import com.yksong.px.model.UserApiResult;
import com.yksong.px.network.PxApi;
import com.yksong.px.network.SignedOkClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by esong on 2015-06-02.
 */
@Module
public class LoginModule {
    private final PxApi mApi;
    private User mCurrentUser;
    private static String HOST = "https://api.500px.com/v1";

    public LoginModule(String consumerKey, String consumerSecret, AccessToken token) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(HOST)
                .setClient(new SignedOkClient(consumerKey, consumerSecret, token))
                .build();

        mApi = restAdapter.create(PxApi.class);

        mCurrentUser = mApi.user().toBlocking().single().user;
    }

    @Provides
    @Singleton
    public PxApi getPxApi() {
        return mApi;
    }

    @Provides
    @Singleton
    public User getUser() {
        return mCurrentUser;
    }
}
