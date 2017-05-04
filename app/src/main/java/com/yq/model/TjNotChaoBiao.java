package com.yq.model;

/**
 * Created by Administrator on 2017/4/26.
 */
public class TjNotChaoBiao {

    /*
    Hm	户名
    hmph  用户编号   -- 户号
    dzbq 电子标签号
    dh	电话
    Dz	地址
    */

    private String hm;
    private String hmph;
    private String dzbq;
    private String dh;
    private String dz;

    /** 户名 */
    public String getHm() {
        return hm;
    }
    /** 户名 */
    public void setHm(String hm) {
        this.hm = hm;
    }

    /** 用户编号   -- 户号 */
    public String getHmph() {
        return hmph;
    }
    /** 用户编号   -- 户号 */
    public void setHmph(String hmph) {
        this.hmph = hmph;
    }

    /** 电子标签号 */
    public String getDzbq() {
        return dzbq;
    }
    /** 电子标签号 */
    public void setDzbq(String dzbq) {
        this.dzbq = dzbq;
    }

    /** 电话 */
    public String getDh() {
        return dh;
    }
    /** 电话 */
    public void setDh(String dh) {
        this.dh = dh;
    }

    /** 地址 */
    public String getDz() {
        return dz;
    }
    /** 地址 */
    public void setDz(String dz) {
        this.dz = dz;
    }

    @Override
    public String toString() {
        return "TjNotChaoBiao{" +
                "hm='" + hm + '\'' +
                ", hmph='" + hmph + '\'' +
                ", dzbq='" + dzbq + '\'' +
                ", dh='" + dh + '\'' +
                ", dz='" + dz + '\'' +
                '}';
    }
}
