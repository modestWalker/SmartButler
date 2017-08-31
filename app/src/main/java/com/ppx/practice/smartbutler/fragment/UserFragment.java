package com.ppx.practice.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.entity.MyUser;
import com.ppx.practice.smartbutler.ui.CourierActivity;
import com.ppx.practice.smartbutler.ui.LoginActivity;
import com.ppx.practice.smartbutler.ui.PhoneSearchActivity;
import com.ppx.practice.smartbutler.utils.utilTools;
import com.ppx.practice.smartbutler.view.CustomDialog;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PPX on 2017/8/22.
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private CircleImageView profile_image;
    //头选择提示框
    private CustomDialog dialog;
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;
    private TextView edit_user;
    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;
    private Button btn_update;
    private TextView tv_courier;
    private TextView tv_phone_search;
    private Button btn_exit_user;
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 1001;
    public static final int IMAGE_REQUEST_CODE = 1002;
    public static final int RESULT_REQUEST_CODE = 1003;
    private File tempFile = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //设置头像
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        //从ShareUtils中获取头像
        utilTools.getImageFromShare(getActivity(), profile_image, "image_header", "");
        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0, R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //设置dialog屏幕外点击无效
        dialog.setCancelable(false);
        //初始化Dialog中的控件
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        //编辑资料
        edit_user = (TextView) view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_sex = (EditText) view.findViewById(R.id.et_sex);
        et_age = (EditText) view.findViewById(R.id.et_age);
        et_desc = (EditText) view.findViewById(R.id.et_desc);
        //输入框默认不可输入，点击编辑资料按钮之后才可输入
        setEnabled(false);
        //设置当前用户对象的信息
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        et_sex.setText(userInfo.isSex() ? "男" : "女");
        et_age.setText(userInfo.getAge() + "");
        et_desc.setText(userInfo.getDesc());
        //确认修改
        btn_update = (Button) view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        //物流查询
        tv_courier = (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        //号码归属地查询
        tv_phone_search = (TextView) view.findViewById(R.id.tv_phone_search);
        tv_phone_search.setOnClickListener(this);
        //退出登录
        btn_exit_user = (Button) view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //设置头像
            case R.id.profile_image:
                //弹出对话框
                dialog.show();
                break;
            //拍照设置头像
            case R.id.btn_camera:
                toCamera();
                break;
            //相册选择头像
            case R.id.btn_picture:
                toPicture();
                break;
            //取消头像设置
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            //编辑资料
            case R.id.edit_user:
                setEnabled(true);
                btn_update.setVisibility(View.VISIBLE);
                break;
            //确认修改
            case R.id.btn_update:
                //取得输入框的值
                String username = et_username.getText().toString().trim();
                String sex = et_sex.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(sex) & !TextUtils.isEmpty(age)) {
                    //更新信息
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    //判断性别设置
                    if (sex.equals("男")) {
                        user.setSex(true);
                    } else if (sex.equals("女")) {
                        user.setSex(false);
                    } else {
                        Toast.makeText(getActivity(), "输入的性别不合法，只能为'男'或'女'！", Toast.LENGTH_SHORT).show();
                    }
                    user.setAge(Integer.parseInt(age));
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc("这个人很懒，什么都没有留下");
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                setEnabled(false);
                                btn_update.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "信息更新成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "信息修改失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "输入框不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            //物流查询
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            //归属地查询
            case R.id.tv_phone_search:
                startActivity(new Intent(getActivity(), PhoneSearchActivity.class));
                break;
            //退出登录
            case R.id.btn_exit_user:
                //清除用户缓存对象
                MyUser.logOut();
                //现在的currentUser是null
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    //设置输入框是否可编辑
    private void setEnabled(boolean isEnable) {
        et_username.setEnabled(isEnable);
        et_sex.setEnabled(isEnable);
        et_age.setEnabled(isEnable);
        et_desc.setEnabled(isEnable);
    }

    //进入相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //进入相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果成功返回数据
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册选择图片
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机照片
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能取消裁剪
                    if (data != null) {
                        //拿到图片进行设置
                        setImageToView(data);
                        //设置图片应将原先的图片进行删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                        break;
                    }
            }
        }
    }

    //裁剪照片
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //将图片设置到View中
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存头像
        utilTools.putImageToShare(getActivity(), profile_image, "image_header");
    }
}
