package com.yq.model;

import java.util.List;

/**
 * @author tank
 * @time 2017/3/22  18:11
 * @desc ${TODD}
 */


public class LoginBean {

    /**
     * ret : ok
     * msg : 成功
     * totals : 0
     * DATA : [{"姓名":"系统主管","操作员编码":"100000","口令":"G\\\u0007\u0018Tpp\r;"}]
     * DATA:[{"czybm":"110104","kl":"?\u00053y\u0007\r","xm":"田枚云"}]
     */

    private String ret;

    private String msg;

    private int totals;
    private List<DATABean> DATA;

    public LoginBean(String ret, String msg, int totals, List<DATABean> DATA) {
        this.ret = ret;
        this.msg = msg;
        this.totals = totals;
        this.DATA = DATA;
    }

    public String getRet() {
        return ret;
    }

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

    public List<DATABean> getDATA() {
        return DATA;
    }

    public void setDATA(List<DATABean> DATA) {
        this.DATA = DATA;
    }


    public class DATABean {

        /**
         * 姓名 : 系统主管
         * 操作员编码 : 100000
         * 口令 : G\Tpp;
         */

        private String xm;
        private String czybm;
        private String kl;

        /** 户名 -- hm -- 操作员 -- 姓名*/
        public String getXm() {
            return xm;
        }
        /** 户名 -- hm*/
        public void setXm(String xm) {
            this.xm = xm;
        }

        /** 登录账户 -- 如 100000 -- 操作员编码 */
        public String getCzybm() {
            return czybm;
        }
        /** 登录账户 -- 如 100000 -- 操作员编码 */
        public void setCzybm(String czybm) {
            this.czybm = czybm;
        }


        /** 口令 -- 登录密码*/
        public String getKl() {
            return kl;
        }
        /** 口令 -- 登录密码*/
        public void setKl(String kl) {
            this.kl = kl;
        }
    }
}
