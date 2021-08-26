package com.tjmedicine.emergency.ui.other.model;


import com.tjmedicine.emergency.common.base.IBaseModel;

public interface IVersionUpdateModel extends IBaseModel {


    /**
     *  获取版本号
     *
     * @param onCallbackListener
     */
    void  updateVersion(OnCallbackListener onCallbackListener);

}
