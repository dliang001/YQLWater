package com.yq.model;

import java.util.List;

/**
 * 查询某个"标签号"、"电话号码"的用户的详情
 *
 *
 */
public class QueryBean {

    /**
     * ret : ok
     * msg : 成功
     * totals : 0
     * DATA : [{"sybs":"128","bcye":"0.20","ysxz":"服务100%","xzje":"96.00","bybs":"176","jfje":"96.00","sysl":"48","jfrq":"2017-03-19 09:50:00","ysqj":"201703","bqsf":"96.00"}]
     *
     * 20170418 接口修改后 ： 添加了  --1、 bm(hmph用户编号-- 户号)  2、 mc(Hm	户名)
     * {"ret":"ok","msg":"成功","totals":0,
     * "DATA":[{"sybs":"0","bcye":"0.00","ysxz":"大埫100%","xzje":null,"bybs":"0","jfje":null,"sysl":"15","scye":"0.00","jfrq":null,"bm":"360008","ysqj":"201704","bqsf":"42.00","mc":"廖德平"}]}
     */

    private String ret;
    private String msg;
    private int totals;
    private List<DATABean> DATA;


    /** 这个应该是，服务器回的json数据中 ret属性： -- OK 代表获取数据成功*/
    public String getRet() {
        return ret;
    }
    /** 这个应该是，服务器回的json数据中 ret属性： -- OK 代表获取数据成功*/
    public void setRet(String ret) {
        this.ret = ret;
    }

    /** 这个应该是，服务器回的json数据中 msg属性： -- "成功" */
    public String getMsg() {
        return msg;
    }
    /** 这个应该是，服务器回的json数据中 msg属性： -- "成功" */
    public void setMsg(String msg) {
        this.msg = msg;
    }


    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    /** List<DATABean> 某个"标签号"、"电话号码"的用户的详情 --   DATABean:xxx -- */
    public List<DATABean> getDATA() {
        return DATA;
    }
    /** List<DATABean> 某个"标签号"、"电话号码"的用户的详情 --   DATABean:xxx -- */
    public void setDATA(List<DATABean> DATA) {
        this.DATA = DATA;
    }



    public static class DATABean {
        /**
         * sybs : 128
         * bcye : 0.20
         * ysxz : 服务100%
         * xzje : 96.00
         * bybs : 176
         * jfje : 96.00
         * sysl : 48
         * jfrq : 2017-03-19 09:50:00
         * ysqj : 201703
         * bqsf : 96.00
         */

        private String sybs;                // 上月表数
        private String bcye;                // 本次结余
        private String ysxz;                // 用水性质
        private String xzje;                // 销账金额
        private String bybs;                // 本月表数
        private String jfje;                // 交费金额
        private String sysl;                // 收费水量
        private String jfrq;                // 交费日期
        private String ysqj;                // 用水期间
        private String bqsf;                // 本期水费
        private String scye;                // 上次结余
        private String bm;                  // 用户编号-- 户号
        private String mc;                  // 户名


        /** 上月表数 */
        public String getSybs() {
            return sybs;
        }
        /** 上月表数 */
        public void setSybs(String sybs) {
            this.sybs = sybs;
        }

        /** 本次结余 */
        public String getBcye() {
            return bcye;
        }
        /** 本次结余 */
        public void setBcye(String bcye) {
            this.bcye = bcye;
        }

        /** 用水性质 */
        public String getYsxz() {
            return ysxz;
        }
        /** 用水性质 */
        public void setYsxz(String ysxz) {
            this.ysxz = ysxz;
        }

        /** 销账金额 */
        public String getXzje() {
            return xzje;
        }
        /** 销账金额 */
        public void setXzje(String xzje) {
            this.xzje = xzje;
        }

        /** 本月表数*/
        public String getBybs() {
            return bybs;
        }
        /** 本月表数*/
        public void setBybs(String bybs) {
            this.bybs = bybs;
        }

        /** 交费金额 */
        public String getJfje() {
            return jfje;
        }
        /** 交费金额 */
        public void setJfje(String jfje) {
            this.jfje = jfje;
        }

        /** 收费水量 */
        public String getSysl() {
            return sysl;
        }
        /** 收费水量 */
        public void setSysl(String sysl) {
            this.sysl = sysl;
        }

        /** 交费日期 */
        public String getJfrq() {
            return jfrq;
        }
        /** 交费日期 */
        public void setJfrq(String jfrq) {
            this.jfrq = jfrq;
        }

        /** 用水期间 */
        public String getYsqj() {
            return ysqj;
        }
        /** 用水期间 */
        public void setYsqj(String ysqj) {
            this.ysqj = ysqj;
        }

        /** 本期水费 */
        public String getBqsf() {
            return bqsf;
        }
        /** 本期水费 */
        public void setBqsf(String bqsf) {
            this.bqsf = bqsf;
        }

        /** 上次结余 */
        public String getScye() {
            return scye;
        }
        /** 上次结余 */
        public void setScye(String scye) {
            this.scye = scye;
        }


        /** hmph用户编号-- 户号 */
        public String getBm() {
            return bm;
        }
        /** hmph用户编号-- 户号 */
        public void setBm(String bm) {
            this.bm = bm;
        }

        /** Hm	户名 */
        public String getMc() {
            return mc;
        }
        /** Hm	户名 */
        public void setMc(String mc) {
            this.mc = mc;
        }
    }
}
