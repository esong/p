package com.yksong.px;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by esong on 2015-05-03.
 */
public class StartActivity extends Activity {
    boolean isLoggedin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isLoggedin) {
            startMainActivity();
        }
    }

    public void startMainActivity() {
        isLoggedin = true;
        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        finish();
    }
}
