package com.ppx.practice.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.utils.L;

/**
 * 版本更新界面
 * Created by PPX on 2017/8/30.
 */

public class UpdateActivity extends BaseActivity {
    private TextView tv_size;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    private void initView() {
        tv_size = (TextView) findViewById(R.id.tv_size);
        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";
        //下载
        String url = getIntent().getStringExtra("url");
        L.i(url);
        if (!TextUtils.isEmpty(url)) {
            RxVolley.download(path, "http://192.168.37.14:8080/pipixiong/item5.jpg", new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    L.i("" + transferredBytes + totalSize);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    L.i("成功");
                }

                @Override
                public void onFailure(VolleyError error) {
                    L.i("失败"+ error.toString());
                }
            });
        }
    }
}
