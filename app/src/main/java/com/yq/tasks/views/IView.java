package com.yq.tasks.views;

import com.yq.model.CbData;
import com.yq.model.UserArrears;

/**
 * 下载成功后
 */
public interface IView extends BaseView {

    /** 下载成功后 干的事*/
    void onDownSuccess(CbData cbData);

    /** 下载成功后 干的事*/
    void onDownUserInfo(UserArrears userArrears);
}
