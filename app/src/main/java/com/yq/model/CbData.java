package com.yq.model;

import java.util.List;

/**
 * 用到的地方之一<p>
 * 四大标签 -- 下载<br>
 * 指示器页面之一  --  用户数据  -- 下载"用户数据" json 对应的bean <br>
 *
 *  下载某月（看条件） 中 多个区域的所有用户<br>
 */
public class CbData {
    private String ret;
    private String msg;
    private int totals;
    private List<CbjDetail> DATA;

    /** 这个应该是，服务器回的json数据中 ret属性： -- OK 代表获取数据成功*/
    public String getRet() {
        return ret;
    }
    /** 这个应该是，服务器回的json数据中 ret属性： -- OK 代表获取数据成功*/
    public void setRet(String ret) {
        this.ret = ret;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }


    /** List<CbjDetail> 获取所有区域 --   CbjDetail类:某个区域的所有用户信息 -- */
    public List<CbjDetail> getDATA() {
        return DATA;
    }
    /** List<CbjDetail> 获取所有区域 --   CbjDetail类:某个区域的所有用户信息 -- */
    public void setDATA(List<CbjDetail> DATA) {
        this.DATA = DATA;
    }


    /** 类 -- 某个区域的所有用户信息 */
    public  class CbjDetail {
        private String GROUPID;
        private String GROUPNAME;
        private String GROUPNUM;
        private List<Cbj> GPOUPDATA;

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

        /** 区域 -- NUM  -- 这个区域用户数量？ */
        public String getGROUPNUM() {
            return GROUPNUM;
        }
        /** 区域 -- NUM  -- 这个区域用户数量？ */
        public void setGROUPNUM(String GROUPNUM) {
            this.GROUPNUM = GROUPNUM;
        }

        /** List 存放所有用户信息 -- 每个用户信息放在  Cbj--bean 中*/
        public List<Cbj> getGPOUPDATA() {
            return GPOUPDATA;
        }
        /** List 存放所有用户信息 -- 每个用户信息放在  Cbj--bean 中*/
        public void setGPOUPDATA(List<Cbj> GPOUPDATA) {
            this.GPOUPDATA = GPOUPDATA;
        }
    }

}
