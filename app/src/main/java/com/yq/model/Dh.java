package com.yq.model;

import com.smtlibrary.utils.JsonUtils;

import java.util.List;

/**
 * 上传电话
 */

public class Dh {
    private String totals;
    private List<Prams> DATA;

    public Dh(List<Prams> DATA) {
        this.totals = "1";
        this.DATA = DATA;
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }
}
