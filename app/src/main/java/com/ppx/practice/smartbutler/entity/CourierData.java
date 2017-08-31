package com.ppx.practice.smartbutler.entity;

/**
 * 快递查询实体类
 * Created by PPX on 2017/8/25.
 */

public class CourierData {
    //时间
    private String datetime;
    //状态
    private String remark;
    //地点
    private String zone;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
