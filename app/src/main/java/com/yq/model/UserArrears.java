package com.yq.model;

import java.util.List;

/**
 *
 * 用户欠费
 */

public class UserArrears {

    private String ret;
    private String msg;
    private int totals;
    private List<User> DATA;

    public UserArrears(String ret, String msg, int totals, List<User> DATA) {
        this.ret = ret;
        this.msg = msg;
        this.totals = totals;
        this.DATA = DATA;
    }

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

    /** List<User> User列表 --   User类: 总金额、欠费月份、违约金、水费金额、客户编号 -- */
    public List<User> getDATA() {
        return DATA;
    }
    /** List<User> User列表 --   User类: 总金额、欠费月份、违约金、水费金额、客户编号 -- */
    public void setDATA(List<User> DATA) {
        this.DATA = DATA;
    }


    /**
     * User: 总金额、欠费月份、违约金、水费金额、客户编号
     */
    public class User {

        private String zje;     //总金额
        private String qfyf;    //欠费月份
        private String wyj;     //违约金
        private String je;      //水费金额
        private String hmph;    //客户编号


        /** 总金额*/
        public String getZje() {
            return zje;
        }
        /** 总金额*/
        public void setZje(String zje) {
            this.zje = zje;
        }

        /** 欠费月份*/
        public String getQfyf() {
            return qfyf;
        }
        /** 欠费月份*/
        public void setQfyf(String qfyf) {
            this.qfyf = qfyf;
        }

        /** 违约金*/
        public String getWyj() {
            return wyj;
        }
        /** 违约金*/
        public void setWyj(String wyj) {
            this.wyj = wyj;
        }

        /** 水费金额*/
        public String getJe() {
            return je;
        }
        /** 水费金额*/
        public void setJe(String je) {
            this.je = je;
        }

        /** 客户编号*/
        public String getHmph() {
            return hmph;
        }
        /** 客户编号*/
        public void setHmph(String hmph) {
            this.hmph = hmph;
        }
    }




}
