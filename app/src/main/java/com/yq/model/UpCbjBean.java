package com.yq.model;

import android.text.TextUtils;

/**
 * Created by gbh on 16/12/9.
 */

public class UpCbjBean {


    private String hmph;            // 户号
    private String Cmds1;
    private String Rq;
    private String JD;
    private String Wd;
    private int TPYE;
    private String FBH;

    public UpCbjBean() {
        JD = "0";
        Wd = "0";
        TPYE = 0;
    }

    /** 用户编号*/
    public String getHmph() {
        return hmph;
    }
    /** 用户编号*/
    public void setHmph(String hmph) {
        this.hmph = hmph;
    }

    /** 本月表数*/
    public String getCmds1() {
        return Cmds1;
    }
    /** 本月表数*/
    public void setCmds1(String cmds1) {
        Cmds1 = cmds1;
    }

    /** 抄表日期*/
    public String getRq() {
        return Rq;
    }
    /** 抄表日期*/
    public void setRq(String rq) {
        Rq = rq;
    }

    /** 经度*/
    public String getJD() {
        return JD;
    }
    /** 经度*/
    public void setJD(String JD) {
        this.JD = JD;
    }

    /** 纬度*/
    public String getWd() {
        return Wd;
    }
    /** 纬度*/
    public void setWd(String wd) {
        Wd = wd;
    }

    /** ??? */
    public int getTPYE() {
        return TPYE;
    }
    /** ??? */
    public void setTPYE(int TPYE) {
        this.TPYE = TPYE;
    }

    /** 分表号 -- return TextUtils.isEmpty(FBH) ? "01" : FBH;*/
    public String getFBH() {
        return TextUtils.isEmpty(FBH) ? "01" : FBH;
    }
    /** 分表号 */
    public void setFBH(String FBH) {
        this.FBH = FBH;
    }
}
