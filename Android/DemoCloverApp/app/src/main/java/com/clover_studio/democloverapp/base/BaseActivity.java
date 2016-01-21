package com.clover_studio.democloverapp.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.clover_studio.democloverapp.R;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
