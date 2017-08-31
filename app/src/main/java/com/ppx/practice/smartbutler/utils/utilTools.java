package com.ppx.practice.smartbutler.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 统一工具类
 * Created by PPX on 2017/8/22.
 */

public class utilTools {
    //设置字体
    public static void setFont(Context context, TextView view, String font) {
        Typeface fontType = Typeface.createFromAsset(context.getAssets(), "fonts/" + font);
        view.setTypeface(fontType);
    }

    //保存ImageView中的图片到ShareUtils
    public static void putImageToShare(Context context, ImageView view, String key) {
        //1.获取Bitmap形式图片
        BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //2.将bitmap转换成字节输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
        //3.利用Base64将字节数组输出流转换成String
        byte[] byteArray = bos.toByteArray();
        String s = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //4.将String保存在ShareUtils中
        ShareUtils.putString(context, key, s);
    }

    //从ShareUtils中获取图片设置到ImageView中
    public static void getImageFromShare(Context context, ImageView view, String key, String defValue) {
        //1.从shareUtils中获取到图片字符串
        String imgString = ShareUtils.getString(context, key, defValue);
        if (!imgString.equals("")) {
            //2.利用Base64将String转换成字节数组输出流
            byte[] byteArray = Base64.decode(imgString, Base64.DEFAULT);
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            //3.生成Bitmap并设置
            Bitmap bitmap = BitmapFactory.decodeStream(bis);
            view.setImageBitmap(bitmap);
        }
    }

    //获取版本
    public static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知版本";
        }
    }
}
