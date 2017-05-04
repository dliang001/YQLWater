package com.yq.model;

/**
 * Created by Administrator on 2017/5/3.
 */
public class YjMoneyBean {

    private String id;
    private String isUpload;        // 是否上传了
    private String hmph;            // 用户编号
    private String yjMoney;         // 预交金额
    private String czybm;           // 登录账号 -- 抄表员编号
    private String time;            // 上传时间


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /** 是否上传了 -- 0:未上传 1:已上传 */
    public String getIsUpload() {
        return isUpload;
    }
    /** 是否上传了 -- 0:未上传 1:已上传 */
    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    /** 用户编号 */
    public String getHmph() {
        return hmph;
    }
    /** 用户编号 */
    public void setHmph(String hmph) {
        this.hmph = hmph;
    }

    /** 预交金额 */
    public String getYjMoney() {
        return yjMoney;
    }
    /** 预交金额 */
    public void setYjMoney(String yjMoney) {
        this.yjMoney = yjMoney;
    }

    /** 登录账号 -- 抄表员编号 */
    public String getCzybm() {
        return czybm;
    }
    /** 登录账号 -- 抄表员编号 */
    public void setCzybm(String czybm) {
        this.czybm = czybm;
    }

    /** 上传时间 */
    public String getTime() {
        return time;
    }
    /** 上传时间 */
    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "YjMoneyBean{" +
                "id='" + id + '\'' +
                ", isUpload='" + isUpload + '\'' +
                ", hmph='" + hmph + '\'' +
                ", yjMoney='" + yjMoney + '\'' +
                ", czybm='" + czybm + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}


