package com.tjmedicine.emergency.ui.login.view;


import com.tjmedicine.emergency.common.bean.UserInfoEntity;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/28 12:59
 */

public interface IRegisterView {

    /**
     * 验证码发送成功
     */
    void sendValidateCodeSuccess(String key);

    /**
     * 验证码发送失败
     * @param info
     */
    void sendValidateCodeFail(String info);

    /**
     * 注册成功
     * @param userInfoEntity
     */
    void registerSuccess(UserInfoEntity userInfoEntity);

    /**
     * 注册失败
     * @param info
     */
    void registerFail(String info);
}
