package com.tjmedicine.emergency.ui.main;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.tjmedicine.emergency.ui.other.WebActivity;
import com.tjmedicine.emergency.ui.uart.SettingActivity;
import com.tjmedicine.emergency.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_FLAG;
import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_URL;

public class MineFragment extends BaseFragment {


    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.ib_close)
    ImageButton mClose;
    @BindView(R.id.ll_login)
    LinearLayout ll_login;

    @BindView(R.id.ll_my_health_mall)
    LinearLayout ll_my_health_mall;
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
        mTitle.setText("????????????");
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
                mPersonSign.setText("??????????????????,??????????????????~");
            } else {
                mPersonSign.setText(UserInfo.getPersonSign());
            }
            Glide.with(this).applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.mipmap.head)
                    .error(R.mipmap.head))
                    .load(UserInfo.getUserInfoImage()).into(mHeader);
        } else {
            mHeader.setEnabled(false);
            mHeader.setImageResource(R.mipmap.head);
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
            , R.id.tv_set_emergency_contact, R.id.ll_login_success, R.id.civ_header,R.id.ll_my_health_mall})
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
            case R.id.ll_my_health_mall:

                //????????????
                    String userPhone = UserInfo.getUserPhone();
                    String url = "http://os.mengyuanyiliao.com/wap"+"?type=1";
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra(WEB_KEY_URL, url);
                    intent.putExtra(WEB_KEY_FLAG, 1);
                    startActivity(intent);

                break;
            case R.id.ll_feedback:
                if (mApp.isLogin()) {
                    startActivity(AccessFeedbackActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }

//                new SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("??????")
//                        .setContentText("????????????????????????????????????????????????????????????5???????????????")
//                        .setConfirmText("??????")
//                        .setCancelText("??????")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog
//                                        .setTitleText("????????????!")
//                                        .setConfirmText("??????")
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                            }
//                        })
//                        .show();
                break;
        }
    }

}
