package com.yq.model;

import com.smtlibrary.utils.JsonUtils;

/**
 * 一个用户中的所有信息<p>
 *
 *
 *
 * 水费的算法就是　5吨保底不足按5吨计算×单价  ，
 * 超过就是按单价*实际
 * 实际应收＝单价*实际读＋上月结余
 *
 */

public class Cbj {
    /*
    GROUPID	区域编号
    GROUPNAME	区域编号名字
    Hm	户名
    Dz	地址
    Zsbh	终身编号
    cmds0	上月表数
    sysl0	上月水量
    cmds1	本月表数
    sysl1	本月水量
    je	金额
    scjy	上次节余
    dh	电话
    sbwz	水表位置
    ysxz	用水性质
    sh	生活
    xz	行政
    gy	工业
    jy	经营
    tz	特种
    qybh	区域编号
    qymc	区域名称
    zbbh	总表编号
    tzbh	表身号
    kj	口径
    sbpp	水表品牌
    sbxh	水表型号
    cbye	抄表月份
    sfy	收费员
    csdu	初始吨位
    rq	抄表日期
    qsf	欠水费
    fbh	分表号
    Jd	经度
    Wd	纬度
    dds 代表 底吨数

    btbh  代表 报停保户

    dzbq 代表标签号

    dk  代表银行扣款

    yjMoney  预交金额

    hmph  用户编号   -- 户号
    yfs   新增抄表月数
    */

    private String GROUPID;
    private String GROUPNAME;
    private String hmph;
    private String Hm;
    private String Dz;
    private String Zsbh;
    private String cmds0;
    private String sysl0;
    private String cmds1;
    private String sysl1;
    private String je;
    private String scjy;
    private String dh;
    private String sbwz;
    private String ysxz;
    private String sh;
    private String xz;
    private String gy;
    private String jy;
    private String tz;
    private String qybh;
    private String qymc;
    private String zbbh;
    private String tzbh;
    private String kj;
    private String sbpp;
    private String sbxh;
    private String cbye;
    private String sfy;
    private String csdu;
    private String rq;
    private String qsf;
    private String fbh;
    private String Jd;
    private String Wd;
    private String dds;
    private String btbh;
    private String dzbq;
    private String dk;
    private int yjMoney;
    private int isUpload;
    private int isChaoBiao;
    private int yfs;//新增抄表月数
//    private String zjm;


    private String tempScjy;      // shen 添加 -- 确保上月结余不变 -- 因为抄表后会改变 scjy 才上传




    public Cbj() {

    }

    /** 电子标签号 */
    public String getDzbq() {
        return dzbq;
    }
    /** 电子标签号 */
    public void setDzbq(String dzbq) {
        this.dzbq = dzbq;
    }

    /** 区域编号 */
    public String getGROUPID() {
        return GROUPID;
    }
    /** 区域编号 */
    public void setGROUPID(String GROUPID) {
        this.GROUPID = GROUPID;
    }

    /** 区域编号名字 */
    public String getGROUPNAME() {
        return GROUPNAME;
    }
    /** 区域编号名字 */
    public void setGROUPNAME(String GROUPNAME) {
        this.GROUPNAME = GROUPNAME;
    }

    /** 户名 */
    public String getHm() {
        return Hm;
    }
    /** 户名 */
    public void setHm(String hm) {
        Hm = hm;
    }

    /** 地址 */
    public String getDz() {
        return Dz;
    }
    /** 地址 */
    public void setDz(String dz) {
        Dz = dz;
    }

    /** 终身编号 */
    public String getZsbh() {
        return Zsbh;
    }
    /** 终身编号 */
    public void setZsbh(String zsbh) {
        Zsbh = zsbh;
    }

    /** 上月表数 */
    public String getCmds0() {
        return cmds0;
    }
    /** 上月表数 */
    public void setCmds0(String cmds0) {
        this.cmds0 = cmds0;
    }

    /** 上月水量 */
    public String getSysl0() {
        return sysl0;
    }
    /** 上月水量 */
    public void setSysl0(String sysl0) {
        this.sysl0 = sysl0;
    }

    /** 本月表数 */
    public String getCmds1() {
        return cmds1;
    }
    /** 本月表数 */
    public void setCmds1(String cmds1) {
        this.cmds1 = cmds1;
    }

    /** 本月水量 */
    public String getSysl1() {
        return sysl1;
    }
    /** 本月水量 */
    public void setSysl1(String sysl1) {
        this.sysl1 = sysl1;
    }

    /** 金额 */
    public String getJe() {
        return je;
    }
    /** 金额 */
    public void setJe(String je) {
        this.je = je;
    }

    /** 上次节余 */
    public String getScjy() {
        return scjy;
    }
    /** 上次节余 */
    public void setScjy(String scjy) {
        this.scjy = scjy;
    }

    /** 电话 */
    public String getDh() {
        return dh;
    }
    /** 电话 */
    public void setDh(String dh) {
        this.dh = dh;
    }

    /** 水表位置 */
    public String getSbwz() {
        return sbwz;
    }
    /** 水表位置 */
    public void setSbwz(String sbwz) {
        this.sbwz = sbwz;
    }

    /** 用水性质 */
    public String getYsxz() {
        return ysxz;
    }
    /** 用水性质 */
    public void setYsxz(String ysxz) {
        this.ysxz = ysxz;
    }

    /** 生活 */
    public String getSh() {
        return sh;
    }
    /** 生活 */
    public void setSh(String sh) {
        this.sh = sh;
    }

    /** 行政 */
    public String getXz() {
        return xz;
    }
    /** 行政 */
    public void setXz(String xz) {
        this.xz = xz;
    }

    /** 工业 */
    public String getGy() {
        return gy;
    }
    /** 工业 */
    public void setGy(String gy) {
        this.gy = gy;
    }

    /** 经营 */
    public String getJy() {
        return jy;
    }
    /** 经营 */
    public void setJy(String jy) {
        this.jy = jy;
    }

    /** 特种 */
    public String getTz() {
        return tz;
    }
    /** 特种 */
    public void setTz(String tz) {
        this.tz = tz;
    }

    /** 区域编号 */
    public String getQybh() {
        return qybh;
    }
    /** 区域编号 */
    public void setQybh(String qybh) {
        this.qybh = qybh;
    }

    /** 区域名称 */
    public String getQymc() {
        return qymc;
    }
    /** 区域名称 */
    public void setQymc(String qymc) {
        this.qymc = qymc;
    }

    /** 总表编号 */
    public String getZbbh() {
        return zbbh;
    }
    /** 总表编号 */
    public void setZbbh(String zbbh) {
        this.zbbh = zbbh;
    }

    /** 表身号 */
    public String getTzbh() {
        return tzbh;
    }
    /** 表身号 */
    public void setTzbh(String tzbh) {
        this.tzbh = tzbh;
    }

    /** 口径 */
    public String getKj() {
        return kj;
    }
    /** 口径 */
    public void setKj(String kj) {
        this.kj = kj;
    }

    /** 水表品牌 */
    public String getSbpp() {
        return sbpp;
    }
    /** 水表品牌 */
    public void setSbpp(String sbpp) {
        this.sbpp = sbpp;
    }

    /** 水表型号 */
    public String getSbxh() {
        return sbxh;
    }
    /** 水表型号 */
    public void setSbxh(String sbxh) {
        this.sbxh = sbxh;
    }

    /** 抄表月份 */
    public String getCbye() {
        return cbye;
    }
    /** 抄表月份 */
    public void setCbye(String cbye) {
        this.cbye = cbye;
    }

    /** 收费员 */
    public String getSfy() {
        return sfy;
    }
    /** 收费员 */
    public void setSfy(String sfy) {
        this.sfy = sfy;
    }

    /** 收费员 */
    public String getCsdu() {
        return csdu;
    }
    /** 初始吨位 */
    public void setCsdu(String csdu) {
        this.csdu = csdu;
    }

    /** 抄表日期 */
    public String getRq() {
        return rq;
    }
    /** 抄表日期 */
    public void setRq(String rq) {
        this.rq = rq;
    }

    /** 欠水费 */
    public String getQsf() {
        return qsf;
    }
    /** 欠水费 */
    public void setQsf(String qsf) {
        this.qsf = qsf;
    }

    /** 分表号 */
    public String getFbh() {
        return fbh;
    }
    /** 分表号 */
    public void setFbh(String fbh) {
        this.fbh = fbh;
    }

    /** 经度 */
    public String getJd() {
        return Jd;
    }
    /** 经度 */
    public void setJd(String jd) {
        Jd = jd;
    }

    /** 纬度 */
    public String getWd() {
        return Wd;
    }
    /** 纬度 */
    public void setWd(String wd) {
        Wd = wd;
    }

    /** 用户编号   -- 户号 */
    public String getHmph() {
        return hmph;
    }
    /** 用户编号   -- 户号 */
    public void setHmph(String hmph) {
        this.hmph = hmph;
    }


    /** 底吨数 */
    public String getDds() {
        return dds;
    }
    /** 底吨数 */
    public void setDds(String dds) {
        this.dds = dds;
    }

    /** 底吨数 -- 报停保户 */
    public String getBtbh() {
        return btbh;
    }
    /** 底吨数 -- 报停保户 */
    public void setBtbh(String btbh) {
        this.btbh = btbh;
    }


    /** 收款方式  -- 1：银行扣款 */
    public String getDk() {
        return dk;
    }
    /** 收款方式  -- 1：银行扣款  */
    public void setDk(String dk) {
        this.dk = dk;
    }

    /** 预交金额 */
    public int getYjMoney() {
        return yjMoney;
    }
    /** 预交金额 */
    public void setYjMoney(int yjMoney) {
        this.yjMoney = yjMoney;
    }

    /** 是否 上传？ -- isUpload INTEGER default 0*/
    public int getIsUpload() {
        return isUpload;
    }
    /** 是否 上传？ -- isUpload INTEGER default 0*/
    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }

    /** 是否抄表 -- isChaoBiao INTEGER default 0*/
    public int getIsChaoBiao() {
        return isChaoBiao;
    }
    /** 是否抄表 -- isChaoBiao INTEGER default 0*/
    public void setIsChaoBiao(int isChaoBiao) {
        this.isChaoBiao = isChaoBiao;
    }

    /** 新增 -- 抄表月数 */
    public int getYfs() {
        return yfs;
    }
    /** 新增 -- 抄表月数 */
    public void setYfs(int yfs) {
        this.yfs = yfs;
    }


    /** shen 添加 -- 确保上月结余不变 -- 因为抄表后会改变 scjy 才上传 */
    public String getTempScjy() {
        return tempScjy;
    }
    /** shen 添加 -- 确保上月结余不变 -- 因为抄表后会改变 scjy 才上传 */
    public void setTempScjy(String tempScjy) {
        this.tempScjy = tempScjy;
    }



    /**
     * 重写：<p>
     * return JsonUtils.serialize(this);
     */
    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }
}
