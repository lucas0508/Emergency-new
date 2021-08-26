package com.tjmedicine.emergency.ui.me.model.impl;


import com.tjmedicine.emergency.common.base.IBaseModel;

public interface ILogOutModel extends IBaseModel {


    /**
     * 退出登录
     * @param listener
     */
    void logout(IBaseModel.OnCallbackListener listener);


}
