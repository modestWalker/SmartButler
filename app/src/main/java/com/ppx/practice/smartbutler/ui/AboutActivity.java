package com.ppx.practice.smartbutler.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.utils.utilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于软件
 * Created by PPX on 2017/8/30.
 */

public class AboutActivity extends BaseActivity {
    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
        mList.add("应用名：" + getString(R.string.app_name));
        mList.add("版本号：" + utilTools.getVersion(this));
        mList.add("官网：www.imooc.com");
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
    }
}
