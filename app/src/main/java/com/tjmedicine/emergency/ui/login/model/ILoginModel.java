package com.tjmedicine.emergency.ui.login.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 */

public interface ILoginModel extends IBaseModel {

    /**
     * 发送验证码
     *
     * @param phone
     * @param listener
     */
    void sendValidateCode(String phone, String type, final IBaseModel.OnCallbackListener listener);

    /**
     * app登陆
     *
     * @param info
     * @param listener
     */
    void toLogin(Map<String, Object> info, OnCallbackListener listener);

    /**
     * 微信登陆
     *
     * @param code
     * @param listener
     */
    void toWXLogin(String code, OnCallbackListener listener);
}
