package com.yksong.px.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yksong.px.R;
import com.yksong.px.app.PxApp;
import com.yksong.px.model.Photo;
import com.yksong.px.presenter.PhotoPresenter;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by esong on 2015-05-26.
 */
public class PhotoListView extends LinearLayout {
    @InjectView(R.id.list) RecyclerView mPhotoView;

    @Inject PhotoPresenter mPresenter;

    FloatingActionButton actionButton;
    FloatingActionMenu actionMenu;

    DefaultAdapter.DefaultViewHolder mCurrentViewHolder;

    public PhotoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        PxApp.getLoginComponent(context).inject(this);
        mPresenter.takeView(this);

        SubActionButton.Builder buttonBuilder =
                new SubActionButton.Builder((Activity)getContext());

        buttonBuilder.setTheme(SubActionButton.THEME_DARK)
                .setLayoutParams(new FloatingActionButton.LayoutParams(150, 150));

        ImageView button1 = new ImageView(getContext());
        button1.setImageDrawable(getResources()
                .getDrawable(R.drawable.comment));
        ImageView button2 = new ImageView(getContext());
        button2.setImageDrawable(getResources()
                .getDrawable(R.drawable.fav));
        ImageView button3 = new ImageView(getContext());
        button3.setImageDrawable(getResources()
                .getDrawable(R.drawable.like));

        ImageView icon = new ImageView(getContext()); // Create an icon
        icon.setImageDrawable(getResources()
                .getDrawable(R.drawable.menu_icon));

        actionButton = new FloatingActionButton.Builder((Activity) getContext())
                .setContentView(icon)
                .setTheme(FloatingActionButton.THEME_DARK)
                .build();
        actionButton.setVisibility(View.INVISIBLE);

        actionMenu = new FloatingActionMenu.Builder((Activity) getContext())
                        .addSubActionView(buttonBuilder.setContentView(button1).build())
                        .addSubActionView(buttonBuilder.setContentView(button2).build())
                        .addSubActionView(buttonBuilder.setContentView(button3).build())
                        .attachTo(actionButton)
                        .build();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        Display display =  ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int height = size.y;

        mPhotoView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return height * 2;
            }
        });

        mPhotoView.setItemViewCacheSize(5);
        mPresenter.request();

        final GestureDetectorCompat detector = new GestureDetectorCompat(
                getContext(), new GestureDetector.SimpleOnGestureListener(){
            public void onLongPress(MotionEvent e) {
                View view = mPhotoView.findChildViewUnder(e.getX(), e.getY());
                mCurrentViewHolder = (DefaultAdapter.DefaultViewHolder)
                        mPhotoView.getChildViewHolder(view);

                int left = (int) e.getX() - actionButton.getMeasuredWidth() / 2;
                int top = (int) e.getY() - actionButton.getMeasuredHeight() / 2;
                actionButton.layout(left,
                        top - getNavBarHeight(),
                        left + actionButton.getMeasuredWidth(),
                        top + actionButton.getMeasuredHeight());

                actionButton.callOnClick();

                if (mCurrentViewHolder.mTextWrapper.getVisibility() == View.VISIBLE) {
                    mCurrentViewHolder.mTextWrapper.setVisibility(View.INVISIBLE);
                } else {
                    Picasso.with(getContext())
                            .load(mCurrentViewHolder.mUserPicUrl)
                            .into(mCurrentViewHolder.mAvatar);

                    mCurrentViewHolder.mTextWrapper.setVisibility(View.VISIBLE);
                }
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                if (actionMenu.isOpen()) {

                    int left = (int) e1.getX() - actionButton.getMeasuredWidth() / 2;
                    int top = (int) e1.getY() - actionButton.getMeasuredHeight() / 2;
                    actionButton.layout(left,
                            top - getNavBarHeight(),
                            left + actionButton.getMeasuredWidth(),
                            top + actionButton.getMeasuredHeight());

                    actionButton.callOnClick();

                    mCurrentViewHolder.mTextWrapper.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        mPhotoView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                detector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    public void displayPhotos(List<Photo> photoList) {
        mPhotoView.setAdapter(new DefaultAdapter(photoList));
    }

    public class DefaultAdapter extends RecyclerView.Adapter<DefaultAdapter.DefaultViewHolder> {
        private List<Photo> photoList;
        private Picasso mPicasso;

        public DefaultAdapter(List<Photo> photos) {
            photoList = photos;
            mPicasso = Picasso.with(getContext());
        }

        @Override
        public DefaultAdapter.DefaultViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_layout, parent, false);
            return new DefaultViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DefaultViewHolder holder, final int position) {
            mPicasso.load(photoList.get(position).image_url)
                    .transform(PaletteTransformation.instance())
                    .into(holder.getImageView(), new Callback() {
                        @Override
                        public void onSuccess() {
                            if (position == 0) {
                                return;
                            }

                            Bitmap bitmap = ((BitmapDrawable) holder.mImageView.getDrawable())
                                    .getBitmap();
                            Palette palette = PaletteTransformation.getPalette(bitmap);
                            int vibrantColor = palette.getVibrantColor(
                                    getResources().getColor(R.color.background_material_dark));
                            holder.mDivider.setBackgroundColor(
                                    palette.getLightVibrantColor(vibrantColor));
                        }

                        @Override
                        public void onError() {

                        }
                    });

            holder.mPhotoId = photoList.get(position).id;
            holder.mPhotoName.setText(photoList.get(position).name);
            holder.mUserName.setText(photoList.get(position).user.username);
            holder.mUserPicUrl = photoList.get(position).user.avatars.small.https;
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }

        public class DefaultViewHolder extends RecyclerView.ViewHolder {
            int mPhotoId;
            String mUserPicUrl;
            View mTextWrapper;
            ImageView mAvatar;
            TextView mUserName;
            TextView mPhotoName;
            ImageView mImageView;
            View mDivider;

            public DefaultViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.image_view);
                mDivider = itemView.findViewById(R.id.photo_divider);
                mTextWrapper = itemView.findViewById(R.id.text_wrapper);
                mPhotoName = (TextView) itemView.findViewById(R.id.photo_name);
                mUserName = (TextView) itemView.findViewById(R.id.user_name);
                mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            }

            public ImageView getImageView() {
                return mImageView;
            }
        }
    }

    public static final class PaletteTransformation implements Transformation {
        private static final PaletteTransformation INSTANCE = new PaletteTransformation();
        private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();

        public static PaletteTransformation instance() {
            return INSTANCE;
        }

        public static Palette getPalette(Bitmap bitmap) {
            return CACHE.get(bitmap);
        }

        private PaletteTransformation() {}

        @Override public Bitmap transform(Bitmap source) {
            Palette palette = Palette.from(source).generate();
            CACHE.put(source, palette);
            return source;
        }

        @Override
        public String key() {
            return "";
        }
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
}
