package com.ppx.practice.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.entity.GirlData;
import com.ppx.practice.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 美女社区图片适配器
 * Created by PPX on 2017/8/29.
 */

public class GirlAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<GirlData> mList;
    private GirlData data;
    private int width, height;
    private WindowManager manager;

    public GirlAdapter(Context mContext, List<GirlData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
        manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.girl_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_girl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        data = mList.get(position);
        //解析图片
        String url = data.getImgUrl();
        PicassoUtils.loadImageViewSize(mContext, url, width / 2, height / 5, viewHolder.imageView);
        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
    }
}
