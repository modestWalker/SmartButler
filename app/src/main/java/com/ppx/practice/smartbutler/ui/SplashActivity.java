package com.ppx.practice.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ppx.practice.smartbutler.MainActivity;
import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.utils.ShareUtils;
import com.ppx.practice.smartbutler.utils.StaticClass;
import com.ppx.practice.smartbutler.utils.utilTools;

/**
 * 闪屏页
 * 1.延时2000ms
 * 2.判断应用是否第一次运行
 * 3.自定义字体
 * 4.Activity全屏主题
 * Created by PPX on 2017/8/22.
 */

public class SplashActivity extends AppCompatActivity {
    private TextView tv_splash;
    private ImageView iv_splash;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    //判断应用是否第一次运行
                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        //延时2000ms
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        tv_splash = (TextView) findViewById(R.id.tv_splash);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        iv_splash.setImageResource(R.drawable.beauty);
        //设置字体
        utilTools.setFont(this, tv_splash, "FONT.TTF");
    }

    private boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        if (isFirst) {
            ShareUtils.putBoolean(this, StaticClass.SHARE_IS_FIRST, false);
            return true;
        } else {
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
