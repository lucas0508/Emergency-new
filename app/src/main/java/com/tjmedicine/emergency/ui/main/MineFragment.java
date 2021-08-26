package com.tjmedicine.emergency.ui.main;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseFragment;
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.common.dialog.CustomDjsFullScreenPopup;
import com.tjmedicine.emergency.common.dialog.CustomFullScreenPopup;
import com.tjmedicine.emergency.ui.login.view.activity.LoginActivity;
import com.tjmedicine.emergency.ui.me.view.PersonalActivity;
import com.tjmedicine.emergency.ui.mine.apply.view.MineApplyActivity;
import com.tjmedicine.emergency.ui.mine.auth.view.MineAuthActivity;
import com.tjmedicine.emergency.ui.mine.contact.view.EmergencyContactActivity;
import com.tjmedicine.emergency.ui.mine.health.HealthFileActivity;
import com.tjmedicine.emergency.ui.mine.volunteer.view.ApplyVolunteerActivity;
import com.tjmedicine.emergency.ui.other.AboutActivity;
import com.tjmedicine.emergency.ui.other.AccessFeedbackActivity;
import com.tjmedicine.emergency.ui.uart.SettingActivity;
import com.tjmedicine.emergency.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment {


    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.ib_close)
    ImageButton mClose;
    @BindView(R.id.ll_login)
    LinearLayout ll_login;
    @BindView(R.id.ll_login_success)
    LinearLayout ll_login_success;
    @BindView(R.id.tv_nick_name)
    TextView mNickName;
    @BindView(R.id.tv_person_sign)
    TextView mPersonSign;
    @BindView(R.id.civ_header)
    CircleImageView mHeader;
    CustomDjsFullScreenPopup customDjsFullScreenPopup;
    private int i = -1;

    @Override
    protected void initView() {
        mTitle.setText("个人中心");
        mClose.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (mApp.isLogin()) {
            ll_login.setVisibility(View.GONE);
            ll_login_success.setVisibility(View.VISIBLE);
            mHeader.setEnabled(true);
            mNickName.setText(UserInfo.getUserNiceName());
            if (TextUtils.isEmpty(UserInfo.getPersonSign())) {
                mPersonSign.setText("这个家伙很懒,什么也没留下~");
            } else {
                mPersonSign.setText(UserInfo.getPersonSign());
            }
            Glide.with(this).applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.mipmap.head)
                    .error(R.mipmap.head))
                    .load(UserInfo.getUserInfoImage()).into(mHeader);
        } else {
            mHeader.setEnabled(false);
            ll_login.setVisibility(View.VISIBLE);
            ll_login_success.setVisibility(View.GONE);
        }
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_mine;
    }

    @OnClick({R.id.btn_login, R.id.ll_my_apply, R.id.ll_my_verified, R.id.ll_my_health_file,
            R.id.ll_about_us, R.id.ll_feedback, R.id.tv_apply_volunteer, R.id.tv_verified
            , R.id.tv_set_emergency_contact, R.id.ll_login_success, R.id.civ_header})
    public void initOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(LoginActivity.class);
                break;
            case R.id.ll_login_success:
                startActivity(PersonalActivity.class);
                break;
            case R.id.civ_header:
                startActivity(PersonalActivity.class);
                break;
            case R.id.tv_apply_volunteer:
                if (mApp.isLogin()) {
                    startActivity(ApplyVolunteerActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_set_emergency_contact:
                if (mApp.isLogin()) {
                    startActivity(EmergencyContactActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_verified:
                if (mApp.isLogin()) {
                    startActivity(MineAuthActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_my_apply:
                if (mApp.isLogin()) {
                    startActivity(MineApplyActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_my_verified:
                if (mApp.isLogin()) {
                    startActivity(MineAuthActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_my_health_file:
                if (mApp.isLogin()) {
                    startActivity(HealthFileActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_about_us:
                startActivity(AboutActivity.class);

                break;
            case R.id.ll_feedback:
                if (mApp.isLogin()) {
                    startActivity(AccessFeedbackActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }

//                new SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("提示")
//                        .setContentText("使用最大校准之前，首先把模拟人放平，按压5秒之后松开")
//                        .setConfirmText("确定")
//                        .setCancelText("取消")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog
//                                        .setTitleText("校准完成!")
//                                        .setConfirmText("确定")
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                            }
//                        })
//                        .show();
                break;
        }
    }

}
