package com.ppx.practice.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ppx.practice.smartbutler.MainActivity;
import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.entity.MyUser;
import com.ppx.practice.smartbutler.utils.ShareUtils;
import com.ppx.practice.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登录界面
 * Created by PPX on 2017/8/23.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_password;
    private CheckBox keep_password;
    private Button mBtn_Login;
    private Button mBtn_Register;
    private TextView tv_forget;
    private CustomDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        keep_password = (CheckBox) findViewById(R.id.keep_password);
        mBtn_Login = (Button) findViewById(R.id.btn_login);
        mBtn_Register = (Button) findViewById(R.id.btn_register);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        mDialog = new CustomDialog(this, 500, 500, R.layout.dialog_loading, R.style.theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //设置dialog屏幕外点击无效
        mDialog.setCancelable(false);
        //设置CheckBox选中状态
        boolean isCheck = ShareUtils.getBoolean(this, "keepPassword", false);
        if (isCheck) {
            et_name.setText(ShareUtils.getString(this, "username", ""));
            et_password.setText(ShareUtils.getString(this, "password", ""));
            keep_password.setChecked(isCheck);
        } else {
            keep_password.setChecked(false);
        }

        mBtn_Login.setOnClickListener(this);
        mBtn_Register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //获取输入框的内容
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //判断输入框是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) {
                    mDialog.show();
                    //登录
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            mDialog.dismiss();
                            if (e == null) {
                                //判断邮箱是否已经验证
                                if (user.getEmailVerified()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "请先进行邮箱验证！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //根据错误码提示错误信息
//                                int errorCode = e.getErrorCode();
//                                if (errorCode == 9014) {
//                                    Toast.makeText(LoginActivity.this, "第三方账号授权失败！" + e.toString(), Toast.LENGTH_SHORT).show();
//                                }
                                Toast.makeText(LoginActivity.this, "登录失败！" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "输入框不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forget:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;
        }
    }

    //直接写在onDestroy方法中有瑕疵，如果用户输入用户名密码之后不登录直接退出应用，信息也会被保存，可以进一步逻辑优化
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this, "keepPassword", keep_password.isChecked());
        //是否记住密码
        if (keep_password.isChecked()) {
            //记住用户名和密码
            ShareUtils.putString(this, "username", et_name.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        } else {
            ShareUtils.deleShare(this, "username");
            ShareUtils.deleShare(this, "password");
        }
    }
}
