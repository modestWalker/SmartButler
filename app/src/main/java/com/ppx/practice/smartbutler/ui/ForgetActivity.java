package com.ppx.practice.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ppx.practice.smartbutler.R;
import com.ppx.practice.smartbutler.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 重置密码界面
 * Created by PPX on 2017/8/23.
 */

public class ForgetActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_now;
    private EditText et_new_pass;
    private EditText et_new_password;
    private Button btn_update_password;
    private EditText et_email;
    private Button btn_forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        et_now = (EditText) findViewById(R.id.et_now);
        et_new_pass = (EditText) findViewById(R.id.et_new_pass);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        btn_update_password.setOnClickListener(this);
        btn_forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_password:
                //获取输入框内容
                String nowPassword = et_now.getText().toString().trim();
                String newPass = et_new_pass.getText().toString().trim();
                String newPassword = et_new_password.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(nowPassword)
                        & !TextUtils.isEmpty(newPass)
                        & !TextUtils.isEmpty(newPassword)) {
                    //判断两次输入新密码是否一致
                    if (newPass.equals(newPassword)) {
                        //重置密码
                        MyUser.updateCurrentUserPassword(nowPassword, newPassword, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(ForgetActivity.this, "修改密码成功，请返回登录！", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgetActivity.this, "修改密码失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "两次输入密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                        et_now.setText("");
                        et_new_pass.setText("");
                        et_new_password.setText("");
                    }
                } else {
                    Toast.makeText(this, "输入框不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_forget_password:
                //获取输入框邮箱地址
                final String email = et_email.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(email)) {
                    //发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(ForgetActivity.this, "邮箱已发送至" + email + "请前往修改密码！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ForgetActivity.this, "邮件发送失败，请核实后重新发送！", Toast.LENGTH_SHORT).show();
                                et_email.setText("");
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "输入框不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
