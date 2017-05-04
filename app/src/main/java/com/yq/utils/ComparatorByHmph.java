package com.yq.utils;

import com.yq.model.TjNotChaoBiao;

import java.util.Comparator;

/**
 * 自己实现的Comparator接口
 */
public class ComparatorByHmph implements Comparator {

    public int compare(Object o1,Object o2) {

        TjNotChaoBiao e1=(TjNotChaoBiao)o1;
        TjNotChaoBiao e2=(TjNotChaoBiao)o2;

//        if(Long.valueOf(e1.getHmph()) < Long.valueOf(e2.getHmph()))
//            return 1;
//        else
//            return 0;

        return e1.getHmph().compareTo(e2.getHmph());
    }
}