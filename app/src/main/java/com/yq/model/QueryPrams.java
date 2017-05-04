package com.yq.model;

import com.smtlibrary.utils.JsonUtils;

/**
 * @author tank
 * @time 2017/3/30  10:53
 * @desc ${TODD}
 */


public class QueryPrams {

    private String bq;//标签号
    private String dh;//电话
    private String ny;//年月


    /** 标签号 */
    public String getBq() {
        return bq;
    }
    /** 标签号 */
    public void setBq(String bq) {
        this.bq = bq;
    }

    /** 电话 */
    public String getDh() {
        return dh;
    }
    /** 电话 */
    public void setDh(String dh) {
        this.dh = dh;
    }

    /** 年月 */
    public String getNy() {
        return ny;
    }
    /** 年月 */
    public void setNy(String ny) {
        this.ny = ny;
    }


    public QueryPrams(String bq, String dh, String ny) {
        this.bq = bq;
        this.dh = dh;
        this.ny = ny;
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }
}
