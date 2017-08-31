package com.ppx.practice.smartbutler.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.adapter.GirlAdapter;
import com.ppx.practice.smartbutler.entity.GirlData;
import com.ppx.practice.smartbutler.utils.PicassoUtils;
import com.ppx.practice.smartbutler.utils.StaticClass;
import com.ppx.practice.smartbutler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 美女社区
 * Created by PPX on 2017/8/22.
 */

public class GirlFragment extends Fragment {
    private GridView mGridView;
    private List<GirlData> mList = new ArrayList<>();
    private GirlAdapter adapter;
    private CustomDialog dialog;
    //预览图片
    private ImageView iv_img;
    //图片地址（用于预览显示）
    List<String> listUrl = new ArrayList<>();
    private PhotoViewAttacher attacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                R.layout.dialog_girl, R.style.theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        iv_img = (ImageView) dialog.findViewById(R.id.iv_img);

        RxVolley.get(StaticClass.GIRL_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                PicassoUtils.loadImageView(getActivity(), listUrl.get(position), iv_img);
                //缩放图片
                attacher = new PhotoViewAttacher(iv_img);
                //刷新
                attacher.update();
                dialog.show();
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String url = json.getString("url");
                listUrl.add(url);
                GirlData data = new GirlData();
                data.setImgUrl(url);
                mList.add(data);
            }
            adapter = new GirlAdapter(getActivity(), mList);
            mGridView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
