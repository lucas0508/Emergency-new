package com.tjmedicine.emergency.ui.login.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.mob.MobSDK;
import com.mob.PrivacyPolicy;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.ui.login.presenter.LoginPresenter;
import com.tjmedicine.emergency.ui.login.view.ILoginView;
import com.tjmedicine.emergency.ui.other.WebActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_FLAG;
import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_URL;


/**
 * 登陆页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, ILoginView {

    private LoginPresenter mLoginPresenter = new LoginPresenter(this);
    public static LoginActivity instance;

    private LinearLayout ValidateCode;
    private EditText mPhone, mValidateCode, mInviteCode;
    private TextView mLoginAgreement, mLoginAgreementPrivacy;
    private Button mSendValidateCode, mLogin;
    private ImageView mWXLogin;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int counter; // 计数器
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.ib_close)
    ImageButton mClose;
    @BindView(R.id.checkbox)
    AppCompatCheckBox appCompatCheckBox;

    private String captcha_key;
    String url;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_login;
    }

    public int getCounter() { // 获取计数时间
        return counter;
    }

    public void setCounter(int counter) { // 设置计数器数值，并反馈给用户
        if (counter <= 0) {
            mSendValidateCode.setEnabled(true);
            mSendValidateCode.setText("重发");
            mTimer.cancel();
            mTimer = null;
            mTimerTask = null;
        } else {
            mSendValidateCode.setText(counter + "秒");
        }
        this.counter = counter;
    }

    public void initView() {
        mTitle.setText("登录");
        instance = this;
        ValidateCode = findViewById(R.id.ll_yaoqingma);
        mPhone = findViewById(R.id.et_phone);
        mValidateCode = findViewById(R.id.et_validate_code);
        mInviteCode = findViewById(R.id.et_invite_code);
        mSendValidateCode = findViewById(R.id.b_send_validate_code);
        mLoginAgreement = findViewById(R.id.tv_login_agreement_user_agreement);
        mLoginAgreementPrivacy = findViewById(R.id.tv_login_agreement_privacyPolicy);
        mLogin = findViewById(R.id.b_login);
        mWXLogin = findViewById(R.id.b_wx_login);
        mSendValidateCode.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mWXLogin.setOnClickListener(this);
        mLoginAgreement.setOnClickListener(this);
        mLoginAgreementPrivacy.setOnClickListener(this);
        initPhoneEditText();

        appCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLogin.setEnabled(true);
                } else {
                    mLogin.setEnabled(false);
                }
            }
        });
        // 异步方法查询隐私,locale可以为null或不设置，默认使用当前系统语言
        MobSDK.getPrivacyPolicyAsync(MobSDK.POLICY_TYPE_TXT, new PrivacyPolicy.OnPolicyListener() {
            @Override
            public void onComplete(PrivacyPolicy data) {
                if (data != null) {
                    // 富文本内容
                    String text = data.getContent();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 请求失败
            }
        });

        MobSDK.getPrivacyPolicyAsync(MobSDK.POLICY_TYPE_URL, new PrivacyPolicy.OnPolicyListener() {
            @Override
            public void onComplete(PrivacyPolicy data) {
                if (data != null) {
                    // 富文本内容
                    url = data.getContent();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 请求失败
            }
        });
    }

    private void initPhoneEditText() {
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence == null || charSequence.length() == 0) {
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < charSequence.length(); i++) {
                    if (i != 3 && i != 8 && charSequence.charAt(i) == ' ') {
                        continue;
                    } else {
                        stringBuilder.append(charSequence.charAt(i));
                        if ((stringBuilder.length() == 4 || stringBuilder.length() == 9)
                                && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                            stringBuilder.insert(stringBuilder.length() - 1, ' ');
                        }
                    }
                }
                if (!stringBuilder.toString().equals(charSequence.toString())) {
                    int index = start + 1;
                    if (stringBuilder.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    mPhone.setText(stringBuilder.toString());
                    mPhone.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_send_validate_code:
                sendValidate();
                break;
            case R.id.b_login:
                boolean checked = appCompatCheckBox.isChecked();
                if (checked) {
                    loginBefore();
                } else {
                    mApp.shortToast("请勾选用户协议");
                }
                //mApp.shortToast("阅读并同意底部相关协议才能登录");
                break;
            case R.id.b_wx_login:
                toWXLogin();
                break;
            case R.id.tv_login_agreement_user_agreement:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(WEB_KEY_URL, GlobalConstants.AGREEMENT_URL);
                intent.putExtra(WEB_KEY_FLAG, 1);
                startActivity(intent);
                break;
            case R.id.tv_login_agreement_privacyPolicy:
                Intent intent1 = new Intent(this, WebActivity.class);
                intent1.putExtra(WEB_KEY_URL, url);
                intent1.putExtra(WEB_KEY_FLAG, 1);
                startActivity(intent1);
                break;
        }
    }

    public void sendValidate() {
        String msg = mLoginPresenter.sendValidateCode(mPhone.getText().toString().replaceAll(" ", ""));
        if (msg != null) {
            mApp.shortToast(msg);
        } else {
            mSendValidateCode.setEnabled(false);
            mSendValidateCode.setText("...");
        }
    }

    /**
     * 吊起微信登陆
     */
    public void toWXLogin() {
      /*  if (WXPayUtils.isWeixinAvilible(getContext())) {
            mApp.getLoadingDialog().show();
            mWxApi = WXAPIFactory.createWXAPI(this, null); //初始化微信api
            mWxApi.registerApp(WXPayUtils.APP_ID); //注册appid  appid可以在开发平台获取
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo";
            mWxApi.sendReq(req);
            WXEntryActivity.setResultCallback((errCode, code) -> {
                mApp.getLoadingDialog().hide();

                Logger.d(errCode);
                Logger.d(code);
                if (errCode == BaseResp.ErrCode.ERR_OK && code != null)
                    mLoginPresenter.toWXLogin(code);
            });
        } else {
            mApp.longToast("未检测到微信，建议去应用市场下载安装！");
            mApp.getLoadingDialog().hide();
        }*/
    }

    /**
     * 普通登陆
     */
    public void loginBefore() {
        String s = mPhone.getText().toString().replaceAll(" ", "");
        String msg = mLoginPresenter.toLogin(
                s,
                mValidateCode.getText().toString(),
                captcha_key
        );
        if (msg != null) {
            mApp.shortToast(msg);
        } else {
            mApp.getLoadingDialog().show();
        }
    }

    @Override
    public void sendValidateCodeSuccess() {
//        captcha_key = key;
        mApp.shortToast("验证码已发送，请留意您的手机！");
        setCounter(120);
        if (mTimer == null) mTimer = new Timer();
        if (mTimerTask == null) mTimerTask = new TimerTask() {
            @Override
            public void run() {
                getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getCounter() <= 0) {
                            mTimer.cancel();
                            mTimerTask.cancel();
                            mTimer = null;
                            mTimerTask = null;
                        }
                        setCounter(getCounter() - 1);
                    }
                });
            }
        };
        this.mTimer.schedule(mTimerTask, 1000, 1000);
    }

    @Override
    public void sendValidateCodeFail(String info) {
        mApp.shortToast(info);
        mSendValidateCode.setEnabled(true);
        mSendValidateCode.setText("重新发送");
    }

    @Override
    public void loginSuccess(UserInfoEntity infoEntity) {
        mApp.getLoadingDialog().hide();
        UserInfo.setUserInfo(infoEntity);
        finish();
        mApp.shortToast("登录成功~");
    }


    @Override
    public void loginFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

    @Override
    public void toWXLoginSuccess(UserInfoEntity data) {

    }


    @Override
    public void toWXLoginFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

}
