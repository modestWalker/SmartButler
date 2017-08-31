package com.ppx.practice.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.entity.WeChatData;
import com.ppx.practice.smartbutler.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 微信精选adapter
 * Created by PPX on 2017/8/29.
 */

public class WeChatAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<WeChatData> mList;
    private WeChatData data;
    private int width, height;
    private WindowManager manager;

    public WeChatAdapter(Context context, List<WeChatData> list) {
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(context);
        //获取屏幕宽高
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
            convertView = mInflater.inflate(R.layout.wechat_item, null);
            viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.tv_tilte = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        data = mList.get(position);
        if (data.getImgUrl().isEmpty()) {
            viewHolder.iv_img.setImageResource(R.mipmap.ic_launcher);
        } else {
            PicassoUtils.loadImageViewSize(mContext, data.getImgUrl(), width / 4, 200, viewHolder.iv_img);
        }
        viewHolder.tv_tilte.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getSource());
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_tilte;
        private TextView tv_source;
    }
}
