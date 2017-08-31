package com.ppx.practice.smartbutler.service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.utils.L;
import com.ppx.practice.smartbutler.utils.StaticClass;
import com.ppx.practice.smartbutler.view.DispatchLinearLayout;

/**
 * 短信监听服务(需要动态申请权限)
 * 还要申请特殊权限
 * Created by PPX on 2017/8/29.
 */

public class SmsService extends Service {
    private SmsReceiver smsReceiver;
    //发件人号码
    private String smsPhone;
    //短信内容
    private String smsContent;
    //窗口管理器
    private WindowManager manager;
    //布局参数
    private WindowManager.LayoutParams params;
    private DispatchLinearLayout mView;
    private TextView tv_phone;
    private TextView tv_content;
    private Button btn_sendsms;
    private HomeWatchReceiver homeWatchReceiver;
    public static final String SYSTEM_DIALOGS_RESON_KEY = "reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY = "homekey";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //动态注册
        smsReceiver = new SmsReceiver();
        IntentFilter intent = new IntentFilter();
        //添加Action
        intent.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intent.setPriority(Integer.MAX_VALUE);
        //注册
        getApplicationContext().registerReceiver(smsReceiver, intent);

        //动态注册
        homeWatchReceiver = new HomeWatchReceiver();
        IntentFilter intent1 = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        getApplicationContext().registerReceiver(homeWatchReceiver, intent1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销
        getApplicationContext().unregisterReceiver(smsReceiver);
        getApplicationContext().unregisterReceiver(homeWatchReceiver);
    }

    //短信广播
    public class SmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StaticClass.SMS_ACTION)) {
                //获取短信内容，返回Object数组
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                //遍历数组
                for (Object obj : pdus) {
                    //把数组元素转换成短信对象
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    //发件人
                    smsPhone = sms.getOriginatingAddress();
                    //内容
                    smsContent = sms.getMessageBody();
                    L.i("短信的内容：" + smsPhone + ":" + smsContent);
                    showWindow();
                }
            }
        }
    }


    //窗口提示
    private void showWindow() {
        //获取系统服务
        manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        params.format = PixelFormat.TRANSLUCENT;
        //定义类型
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);
        tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
        tv_content = (TextView) mView.findViewById(R.id.tv_content);
        btn_sendsms = (Button) mView.findViewById(R.id.btn_sendsms);
        tv_phone.setText("发件人：" + smsPhone);
        tv_content.setText(smsContent);
        btn_sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
                //关闭窗口
                if (mView.getParent() != null) {
                    manager.removeView(mView);
                }
            }
        });
        //添加View到窗口
        manager.addView(mView, params);
        mView.setDispatchKeyEventListener(mDispatchKeyEventListener);
    }

    private DispatchLinearLayout.DispatchKeyEventListener mDispatchKeyEventListener
            = new DispatchLinearLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否按返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if (mView.getParent() != null) {
                    manager.removeView(mView);
                }
                return true;
            }
            return false;
        }
    };

    //回复短信
    private void sendSms() {
        Uri uri = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    //监听Home键的广播
    private class HomeWatchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_RESON_KEY);
                if (SYSTEM_DIALOGS_HOME_KEY.equals(reason)) {
                    if (mView.getParent() != null) {
                        manager.removeView(mView);
                    }
                }
            }
        }
    }

}
