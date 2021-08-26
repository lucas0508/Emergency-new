package com.tjmedicine.emergency.ui.login.view.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;
import com.tjmedicine.emergency.ui.login.presenter.RegisterPresenter;
import com.tjmedicine.emergency.ui.login.view.IRegisterView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class BindWXActivity extends BaseActivity implements IRegisterView, View.OnClickListener {
    private RegisterPresenter mRegisterPresenter = new RegisterPresenter(this);
    private Button mSendValidateCode, mRegister;
    private EditText mPhone, mValidateCode, mInviteCode;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private String captcha_key;
    private int counter; // 计数器

    @BindView(R.id.tv_title)
    TextView tv_title;
    private String token;

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

    @Override
    protected void initView() {
        token = getIntent().getStringExtra("Token");
        mSendValidateCode = findViewById(R.id.b_send_validate_code);
        mRegister = findViewById(R.id.b_register);
        mPhone = findViewById(R.id.et_phone);
        mValidateCode = findViewById(R.id.et_validate_code);
        mInviteCode = findViewById(R.id.et_invite_code);
        tv_title.setText("绑定手机号");
       /* mLoginAgreement = findViewById(R.id.tv_login_agreement);
        mLoginAgreement.setOnClickListener(this);
        mLoginAgreement.setText(Html.fromHtml("点击登录,即同意<font color=\"#0498f5\">《叫个工人用户协议》</font>"));*/
        mSendValidateCode.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_bind_wx;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_send_validate_code:
                sendValidate();
                break;
            case R.id.b_register:
                submit();
                break;
//            case R.id.tv_login_agreement:
//                Intent intent = new Intent(this, WebActivity.class);
//                intent.putExtra(WebActivity.KEY_URL,"https://www.jiaogegongren.com/clause/Agreement.html");
//                intent.putExtra(WebActivity.KEY_FLAG,"1");
//                startActivity(intent);
//                break;
        }
    }

    public void sendValidate() {
        String msg = mRegisterPresenter.sendValidateCode(mPhone.getText().toString());
        if (msg != null) {
            mApp.shortToast(msg);
        } else {
            mSendValidateCode.setEnabled(false);
            mSendValidateCode.setText("...");
        }
    }

    public void submit() {
        String msg = mRegisterPresenter.register(
                mPhone.getText().toString(),
                mValidateCode.getText().toString(),
                captcha_key,
                token
        );
        if (msg != null) {
            mApp.shortToast(msg);
        } else {
            mApp.getLoadingDialog().show();
        }
    }

    @Override
    public void sendValidateCodeSuccess(String key) {
        captcha_key = key;
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
        mSendValidateCode.setText("发送");
    }

    @Override
    public void registerSuccess(UserInfoEntity userInfoEntity) {
        mApp.getLoadingDialog().hide();
        //UserInfo.setUserInfo(userInfoEntity);
//        UserInfo.setToken(token);
        LoginActivity.instance.finish();
        finish();


    }

    @Override
    public void registerFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }
}
