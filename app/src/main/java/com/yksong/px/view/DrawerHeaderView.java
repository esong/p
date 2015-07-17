package com.yksong.px.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksong.px.R;
import com.yksong.px.app.PxApp;
import com.yksong.px.model.ApiResult;
import com.yksong.px.model.Photo;
import com.yksong.px.model.User;
import com.yksong.px.network.PxApi;
import com.yksong.px.ui.CircleTransform;
import com.yksong.px.ui.RoundedTransformation;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by esong on 2015-06-03.
 */
public class DrawerHeaderView extends FrameLayout {
    @InjectView(R.id.cover) ImageView mCover;
    @InjectView(R.id.avatar) ImageView mAvatar;
    @InjectView(R.id.username) TextView mUserName;

    @Inject User mUser;
    @Inject PxApi mApi;

    public DrawerHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        PxApp.getLoginComponent(context).inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        mApi.coverPhoto(mUser.id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResult apiResult) {
                        List<Photo> photos = apiResult.photos;

                        if (photos != null && photos.size() != 0) {
                            Picasso.with(getContext())
                                    .load(photos.get(0).image_url)
                                    .into(mCover);
                        }
                    }
                });

        Picasso.with(getContext())
                .load(mUser.userpic_url)
                .transform(new CircleTransform())
                .into(mAvatar);

        mUserName.setText(mUser.fullname);


    }
}
