package com.yksong.px.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yksong.px.MainActivity;
import com.yksong.px.R;
import com.yksong.px.ui.Container;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by esong on 2015-05-01.
 */
public class MainContainer extends FrameLayout implements Container {
    @InjectView(R.id.toolbar) Toolbar mToolBar;
    @InjectView(R.id.photo_list) PhotoListView mPhotoList;

    @Inject NavigationView mNavigationView;
    @Inject DrawerLayout mDrawerLayout;

    public static String PX_PREFERENCE = "PX_PREFERENCE";
    public static String PX_PREFERENCE_NAVI_ITEM = "PX_PREFERENCE_NAVI_ITEM";

    public MainContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((MainActivity) context).getDiComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        mToolBar.setNavigationIcon(R.drawable.menu_icon);
        mToolBar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int menuId = menuItem.getItemId();
                SharedPreferences preferences = getContext().getSharedPreferences(PX_PREFERENCE, 0);
                preferences.edit()
                        .putInt(PX_PREFERENCE_NAVI_ITEM, menuId)
                        .apply();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                mPhotoList.onListChange(menuId);
                return true;
            }
        });
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void statusBarShown() {
        ((ViewGroup.MarginLayoutParams) mToolBar.getLayoutParams())
                .setMargins(0, getStatusBarHeight(), 0, 0);
        mToolBar.requestLayout();
    }

    public void statusBarHidden() {
        ((ViewGroup.MarginLayoutParams) mToolBar.getLayoutParams())
                .setMargins(0, 0, 0, 0);
        mToolBar.requestLayout();
    }

    @Override
    public void showItem(String item) {

    }
}
