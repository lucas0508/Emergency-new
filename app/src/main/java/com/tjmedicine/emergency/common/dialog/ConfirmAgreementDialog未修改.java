package com.tjmedicine.emergency.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.ui.other.WebActivity;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/2 11:12
 */

public class ConfirmAgreementDialog未修改 extends Dialog {
    private Context mContext;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private View mView;
    private TextView mMessage, mWarnMessage, mOk, mCancel;
    private ConfirmCallback mConfirmCallback;
    private RelativeLayout mDialogContent;
    private String url;

    public ConfirmAgreementDialog未修改(@NonNull Context context) {
        super(context);
        this.mContext = context;
        mBuilder = new AlertDialog.Builder(mContext);
        mView = getLayoutInflater().inflate(R.layout.dialog_confirm_agreement2, null);
        mDialogContent = mView.findViewById(R.id.ll_dialog_content);
        mMessage = mView.findViewById(R.id.tv_message);
        mWarnMessage = mView.findViewById(R.id.tv_warn_message);
        mOk = mView.findViewById(R.id.b_ok);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(1);
            }
        });

        mCancel = mView.findViewById(R.id.b_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(0);
            }
        });
//        MobSDK.getPrivacyPolicyAsync(MobSDK.POLICY_TYPE_URL, new PrivacyPolicy.OnPolicyListener() {
//            @Override
//            public void onComplete(PrivacyPolicy data) {
//                if (data != null) {
//                    // 富文本内容
//                    url = data.getContent();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                // 请求失败
//            }
//        });
        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        Window w = mDialog.getWindow();
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    //  Html.fromHtml("点击登录,即同意<font color=\"#0498f5\">《叫个工人用户协议》</font>");

    String ServiceAgreement = "<font color=\"#0498f5\">服务协议及隐私政策</font>";

//    String privacyPolicy = "<font color=\"#0498f5\">第三方服务和隐私政策</font>";

    String notice = "<font color=\"#000000\"><br>" +
            "1. 允许应用修改或删除存储卡上的照片、媒体内容和文件,(用于app拍照上传文件、存储)。<br>" +
            "2. 允许应用基于GPS、基站、 Wi-Fi 等网络源获取位置信息。这可能会增加耗电量,(用于定位用户的当前位置,发出求救信号后可及时救援)。<br>" +
            "3. 允许应用基于基站、 Wi-Fi 等网络源获取位置信息,(用于向用户展示志愿者、医生及急救设备的具体位置)。<br>" +
            "4. 允许应用在后台运行时检索用户位置信息,(用于用户导航寻找急救设备)。<br>" +
            "5. 允许应用录制音频(用于模拟人连接后心肺复苏提示音播放)。<br>" +
            "6. 允许应用拍摄照片和视频,(由于用户进行实名认证及志愿者申请时拍摄照片)。<br>" +
            "7. 允许应用读取存储卡上的照片、媒体内容和文件,(由于用户进行实名认证及志愿者申请时选择相册照片)。<br>" +
            "8.读取通话状态和移动网络信息(用于连接网络) 。，你可以在设置中查看、变更并管理你的授权。<br>"+
            "9.基于我们为您终端用户设备进行唯一性标识,需要获取用户的MAC地址。<br>" +
            "10.获取用户软件安装列表权限用于后续APP软件升级时使用。</font>";

    public void show(@NonNull String message, ConfirmCallback callback) {
        mMessage.setText(message);
        mConfirmCallback = callback;
        showDialog();
        // 异步方法查询隐私,locale可以为null或不设置，默认使用当前系统语言

//        SpannableString spannableString = new SpannableString(Html.fromHtml("请你务必审慎阅读、充分理解" + ServiceAgreement + "/" + privacyPolicy + "各条款，包括但不限于:" +
//                notice));

        SpannableString spannableString = new SpannableString(Html.fromHtml("请你务必审慎阅读、充分理解" + ServiceAgreement  + "各条款，包括但不限于:" +
                notice));


        spannableString.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击代码
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(Constants.WEB_KEY_URL, GlobalConstants.AGREEMENT_URL);
                intent.putExtra(Constants.WEB_KEY_FLAG, 1);
                mContext.startActivity(intent);
            }
        }), 13, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        spannableString.setSpan(new Clickable(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //点击代码
//                Intent intent = new Intent(mContext, WebActivity.class);
//                intent.putExtra(Constants.WEB_KEY_URL, url);
//                intent.putExtra(Constants.WEB_KEY_FLAG, 1);
//                mContext.startActivity(intent);
//            }
//        }), 23, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mWarnMessage.setText(spannableString);
        mWarnMessage.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void showDialog() {
        mDialog.show();
        mView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha_in));
        mDialogContent.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.scale_in));
    }

    private void dismissDialog(final int type) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_out);
        mDialogContent.startAnimation(animation);
        mView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha_out));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (type == 1 && mConfirmCallback != null)
                    mConfirmCallback.onOk();
                if (type == 0 && mConfirmCallback != null)
                    mConfirmCallback.onCancel();
                mDialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public interface ConfirmCallback {
        void onOk();
        void onCancel();
    }


    class Clickable extends ClickableSpan implements View.OnClickListener {

        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            //设置是否有下划线
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }


}
