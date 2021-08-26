package com.tjmedicine.emergency.ui.login.model;


import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/28 13:18
 */

public interface IRegisterModel extends IBaseModel {

    /**
     * 微信发送验证码
     *
     * @param phone
     * @param listener
     */
    void sendWXValidateCode(String phone, String type, final OnCallbackListener listener);

    /**
     * 微信注册
     *
     * @param data
     * @param listener
     */
    void fromWXRegister(Map<String, Object> data, String token, OnCallbackListener listener);
}
