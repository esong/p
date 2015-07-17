package com.yksong.px;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yksong.px.component.DaggerMainActivityComponent;
import com.yksong.px.component.MainActivityComponent;
import com.yksong.px.component.MainActivityModule;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {
    @InjectView(R.id.main_drawer_layout) DrawerLayout mDrawerLayout;
    @InjectView(R.id.main_navigation) NavigationView mNavigationView;
    @InjectView(R.id.main_content) ViewGroup mMainContent;

    private MainActivityComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideStatusBar();

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mComponent = DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .build();

        LayoutInflater.from(this).inflate(R.layout.main_layout, mMainContent);
    }

    @Override
    protected void onResume () {
        super.onResume();
        hideStatusBar();
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int visibility = 0;

        if (Build.VERSION.SDK_INT >= 16) {
            visibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 19) {
            visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        decorView.setSystemUiVisibility(visibility);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    public MainActivityComponent getDiComponent() {
        return mComponent;
    }

}
