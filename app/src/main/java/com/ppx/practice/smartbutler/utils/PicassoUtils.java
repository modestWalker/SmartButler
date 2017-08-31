package com.ppx.practice.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * picasso封装
 * Created by PPX on 2017/8/29.
 */

public class PicassoUtils {
    //默认加载图片
    public static void loadImageView(Context context, String url, ImageView view) {
        Picasso.with(context).load(url).into(view);
    }

    //指定大小加载图片
    public static void loadImageViewSize(Context context, String url, int width, int height, ImageView view) {
        Picasso.with(context).load(url).resize(width, height).centerCrop().into(view);
    }

    //带默认图片错误图片的加载
    public static void loadImageViewSizeHolder(Context context, String url, int placeholderImg, int errorImg, ImageView view) {
        Picasso.with(context).load(url).placeholder(placeholderImg).error(errorImg).into(view);
    }

    //裁剪图片
    public static void loadImageViewCrop(Context context, String url, ImageView view) {
        Picasso.with(context).load(url).transform(new CropSquareTransformation()).into(view);
    }

    //按比例裁剪 矩形
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                //回收
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "ppx";
        }
    }
}
