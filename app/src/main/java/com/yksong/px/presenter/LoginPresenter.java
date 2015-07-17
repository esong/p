package com.yksong.px.presenter;

import android.content.Context;

import com.fivehundredpx.api.FiveHundredException;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.auth.OAuthAuthorization;
import com.fivehundredpx.api.auth.XAuthProvider;
import com.yksong.px.R;
import com.yksong.px.app.AccountManager;
import com.yksong.px.app.PxApp;
import com.yksong.px.component.DaggerLoginComponent;
import com.yksong.px.component.LoginComponent;
import com.yksong.px.component.LoginModule;
import com.yksong.px.view.LoginView;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by esong on 2015-04-30.
 */
@Singleton
public class LoginPresenter extends BasePresenter<LoginView> {
    @Inject AccountManager mAccountManager;

    @Inject
    public LoginPresenter() {
    }

    public boolean notLoggedin() {
        AccessToken token = mAccountManager.getAccessToken();

        return token.getToken().isEmpty() && token.getTokenSecret().isEmpty();
    }

    public void login(final String user, final String password) {
        final LoginView view = getView();
        final Context context = view.getContext();
        final String consumerKey = context.getResources().getString(R.string.pxConsumerKey);
        final String consumerSecret = context.getResources().getString(R.string.pxConsumerSecret);

        Observable<AccessToken> observable = Observable.create(
                new Observable.OnSubscribe<AccessToken>() {
            @Override
            public void call(Subscriber<? super AccessToken> subscriber) {
                try {
                    final OAuthAuthorization oauth = new OAuthAuthorization.Builder()
                            .consumerKey(consumerKey)
                            .consumerSecret(consumerSecret)
                            .build();

                    final AccessToken accessToken = oauth
                            .getAccessToken(new XAuthProvider(user, password));

                    subscriber.onNext(accessToken);
                } catch (FiveHundredException e) {
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<AccessToken, Object>() {
                    @Override
                    public Object call(AccessToken accessToken) {
                        mAccountManager.storeAccessToken(accessToken);
                        wireLoginComponents();
                        return null;
                    }
                })
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.loginFailed(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        view.loginSuccess();
                    }
                });
    }

    public void wireLoginComponents() {
        final Context context = getView().getContext();
        final String consumerKey = context.getResources().getString(R.string.pxConsumerKey);
        final String consumerSecret = context.getResources().getString(R.string.pxConsumerSecret);

        LoginComponent loginComponent = DaggerLoginComponent.builder()
                .loginModule(new LoginModule(consumerKey, consumerSecret
                        , mAccountManager.getAccessToken()))
                .build();

        PxApp.setLoginComponent(context, loginComponent);
    }
}
