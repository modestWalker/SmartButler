package com.ppx.practice.smartbutler.entity;

import cn.bmob.v3.BmobUser;

/**
 * 用户实体类
 * Created by PPX on 2017/8/23.
 */

public class MyUser extends BmobUser {
     private int age;
    private boolean sex;
    private String desc;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
