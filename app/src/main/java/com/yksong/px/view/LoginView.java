package com.yksong.px.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yksong.px.R;
import com.yksong.px.StartActivity;
import com.yksong.px.app.PxApp;
import com.yksong.px.presenter.LoginPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by esong on 2015-04-30.
 */
public class LoginView extends FrameLayout {
    @Inject LoginPresenter mPresenter;

    @InjectView(R.id.cover) ImageView mCoverImage;
    @InjectView(R.id.email_username) EditText mEmailUserName;
    @InjectView(R.id.password) EditText mPassword;
    @InjectView(R.id.error_message) TextView mErrorMessage;
    @InjectView(R.id.progressBar) ProgressBar mLoginProgress;
    @InjectView(R.id.loginButton) Button mLoginButton;

    private boolean mLoginClicked;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        PxApp.getAppComponent(context).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenter.dropView(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        RelativeLayout.LayoutParams signUpLayout = (RelativeLayout.LayoutParams)
                findViewById(R.id.signUp).getLayoutParams();
        signUpLayout.setMargins(20, 0, 0, getNavBarHeight() + 20);

        ButterKnife.inject(this);

        Picasso.with(getContext())
                .load(R.drawable.home_cover)
                .fit()
                .centerCrop()
                .into(mCoverImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (mPresenter.notLoggedin()) {
                            findViewById(R.id.loginControl).setVisibility(VISIBLE);
                        } else {
                            Observable.create(new Observable.OnSubscribe<Object>() {
                                @Override
                                public void call(Subscriber<? super Object> subscriber) {
                                    mPresenter.wireLoginComponents();
                                    subscriber.onNext(null);
                                }
                            }).subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Object>() {
                                        @Override
                                        public void call(Object o) {
                                            loginSuccess();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public int getNavBarHeight() {
        int result = 0;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            int resourceId = getResources()
                    .getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    @OnClick(R.id.loginButton)
    public void login() {
        mLoginClicked = true;
        String emailUserName = mEmailUserName.getText().toString();
        String password = mPassword.getText().toString();

        if (emailUserName.isEmpty()) {
            displayLoginError(getResources().getString(R.string.longin_empty,
                    getResources().getString(R.string.email_username)));
            return;
        }

        if (password.isEmpty()) {
            displayLoginError(getResources().getString(R.string.longin_empty,
                    getResources().getString(R.string.password)));
            return;
        }

        mLoginButton.setVisibility(GONE);
        mLoginProgress.setVisibility(VISIBLE);
        mPresenter.login(emailUserName, password);
    }

    public void displayLoginError(String errorMessage) {
        mErrorMessage.setVisibility(VISIBLE);
        mErrorMessage.setText(errorMessage);
    }

    public void loginSuccess() {
        mCoverImage.destroyDrawingCache();
        ((StartActivity)getContext()).startMainActivity();
    }

    public void loginFailed(Throwable e) {
        displayLoginError(e.getMessage());
        mLoginButton.setVisibility(VISIBLE);
        mLoginProgress.setVisibility(GONE);
    }

    @OnClick(R.id.signUp)
    public void singup(){
        Uri uriUrl = Uri.parse(getResources().getString(R.string.sign_up_url));
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        getContext().startActivity(launchBrowser);
    }

}
