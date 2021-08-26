package com.tjmedicine.emergency.ui.login.view;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/8 12:06
 */

public interface IForgetPasswordView {

    /**
     * 验证码发送成功
     */
    void sendValidateCodeSuccess();

    /**
     * 验证码发送失败
     * @param info
     */
    void sendValidateCodeFail(String info);


    /**
     * 密码修改成功
     */
    void passwordUpdateSuccess();

    /**
     * 密码修改失败
     * @param info
     */
    void passwordUpdateFail(String info);
}
