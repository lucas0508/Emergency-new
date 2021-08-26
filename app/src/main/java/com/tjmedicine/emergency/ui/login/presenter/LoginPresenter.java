package com.tjmedicine.emergency.ui.login.presenter;


import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.login.model.ILoginModel;
import com.tjmedicine.emergency.ui.login.model.impl.LoginModel;
import com.tjmedicine.emergency.ui.login.view.ILoginView;
import com.tjmedicine.emergency.utils.ValidateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 */

public class LoginPresenter extends BasePresenter {

    private ILoginView mLoginView;
    private ILoginModel mLoginModel = new LoginModel();
    private Map<String, Object> mLoginFormData = new HashMap<>();

    public LoginPresenter(ILoginView loginView) {
        this.mLoginView = loginView;
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    public String sendValidateCode(String phone) {
        if (!ValidateUtils.validatePhone(phone)) return "手机号输入不正确！";
        sendNormalValidateCode(phone,"login");
        return null;
    }

    /**
     * 普通验证码发送
     *
     * @param phone
     */
    private void sendNormalValidateCode(String phone , String type) {
        mLoginModel.sendValidateCode(phone,type, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    mLoginView.sendValidateCodeSuccess();
                } else {
                    mLoginView.sendValidateCodeFail(res.getMsg());
                }
            }
        });
    }

    /**
     * 普通登陆
     *
     * @param phone
     * @param validateCode
     * @param inviteCode
     * @return
     */
    public String toLogin(String phone, String validateCode, String inviteCode) {
        if (!ValidateUtils.validatePhone(phone))
            return "手机号输入不正确！";
        if (!ValidateUtils.validateCode(validateCode))
            return "验证码为6位纯数字！";

//        String finalPhone = SecurityUtil.encryptRSAPublic(phone);
//        String finalValidateCode = SecurityUtil.encryptRSAPublic(validateCode);
//        String finalInviteCode = SecurityUtil.encryptRSAPublic(inviteCode);

        mLoginFormData.put("phone", phone);
        mLoginFormData.put("code", validateCode);
//        mLoginFormData.put("uuid", inviteCode);
//      mLoginFormData.put("cityCode", UserInfo.getUserHadCity() != null ? UserInfo.getUserHadCity().get("HADCITYCODE") : "");

        mLoginModel.toLogin(mLoginFormData, new ILoginModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    Logger.e("数据"+res.getData());
                    mLoginView.loginSuccess((UserInfoEntity) res.getData());
                } else {
                    mLoginView.loginFail(res.getMsg());
                }
            }
        });
        return null;
    }

    /**
     * 微信登陆
     *
     * @param code
     */
    public void toWXLogin(String code) {
        mLoginModel.toWXLogin(code, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    mLoginView.toWXLoginSuccess((UserInfoEntity) res.getData());
                } else {
                    mLoginView.toWXLoginFail(res.getMsg());
                }
            }
        });
    }
}
