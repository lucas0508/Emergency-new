package com.tjmedicine.emergency.ui.me.view;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.common.dialog.DialogUtil;
import com.tjmedicine.emergency.common.net.QiNiuUtils;
import com.tjmedicine.emergency.model.takePhoto.PhotoUtils;
import com.tjmedicine.emergency.model.widget.DateWheelPicker;
import com.tjmedicine.emergency.model.widget.RoundImageView;
import com.tjmedicine.emergency.model.widget.SelectPicDialog;
import com.tjmedicine.emergency.model.widget.SelectSexDialog;
import com.tjmedicine.emergency.ui.login.view.activity.LoginActivity;
import com.tjmedicine.emergency.ui.me.presenter.LogoutPresenter;
import com.tjmedicine.emergency.ui.me.presenter.MinePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class PersonalActivity extends BaseActivity implements IMineFragmentView, ILogOutView {

    private MinePresenter minePresenter = new MinePresenter(this);
//    private UploadFilePresenter uploadFilePresenter = new UploadFilePresenter(this);
    private LogoutPresenter logoutPresenter = new LogoutPresenter(this);
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_common_right)
    TextView tv_common_right;
    @BindView(R.id.ll_person_header_image)
    LinearLayout ll_person_header_image;
    @BindView(R.id.ll__person_gender)
    LinearLayout ll__person_gender;
    @BindView(R.id.ll_person_birthday)
    LinearLayout ll_person_birthday;
    @BindView(R.id.et__person_nickName)
    EditText et__person_nickName;
    @BindView(R.id.et__person_phone)
    EditText et__person_phone;
    @BindView(R.id.tv__person_birthday)
    TextView tv__person_birthday;
    @BindView(R.id.tv__person_gender)
    TextView tv__person_gender;
    @BindView(R.id.riv_headImage)
    RoundImageView riv_Image;
    @BindView(R.id.et__perso_sign)
    EditText et__perso_sign;
    @BindView(R.id.btn_logout)
    Button btn_logout;

    private String userSex = "";
    private String imageUrlPath = "";

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {
        mTitle.setText("个人信息");
        tv_common_right.setText("保存");
        tv_common_right.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                submit();
            }
        });
        mApp.getLoadingDialog().show();
        minePresenter.queryUserInfo();
        initData();
    }


    private void initData() {

        ll__person_gender.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                SelectSexDialog selectSexDialog = new SelectSexDialog(PersonalActivity.this);
                selectSexDialog.show(new SelectSexDialog.SelectSexListener() {
                    @Override
                    public void setSelectSex(String str, String sex) {
                        tv__person_gender.setText(str);
                        userSex = sex;
                    }
                });
            }
        });
        ll_person_birthday.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                ChooseBirthday();
            }
        });
        if (!TextUtils.isEmpty(et__person_phone.getText().toString())) {
            et__person_phone.setEnabled(false);
        }
        ll_person_header_image.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                SelectPicDialog selectPicDialog = new SelectPicDialog(PersonalActivity.this);
                selectPicDialog.show(new SelectPicDialog.Callback() {
                    @Override
                    public void clickItem(int position) {
                        if (position == 0) {
                            PhotoUtils.autoObtainCameraPermission(getContext(), true);
                        } else if (position == 1) {
                            PhotoUtils.autoObtainStoragePermission(getContext(), true);
                        }
                    }
                });
            }
        });

        btn_logout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mApp.getLoadingDialog().show();
                logoutPresenter.logOut();
            }
        });
    }
    private void ChooseBirthday(){
        DateWheelPicker picker= new DateWheelPicker(PersonalActivity.this);
        picker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        picker.setOnDateChangedListener(new DateWheelPicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DateWheelPicker view, int year, int monthOfYear, int dayOfMonth) {
                tv__person_birthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        });
        showDialog("请选择出生日期", picker);
    }

    private void showDialog(String title, View v) {
        DialogUtil.showDialog(PersonalActivity.this, title, v);
    }


    private void submit() {
        mApp.getLoadingDialog().show();
        if (TextUtils.isEmpty(userSex)) {
            String trim = tv__person_gender.getText().toString().trim();
            if (trim.equals("男")) {
                userSex = "1";
            } else if (trim.equals("女")) {
                userSex = "2";
            } else {
                userSex = "0";
            }
        }
        Map<String, Object> objectMap = new HashMap<>();
        if (!TextUtils.isEmpty(imageUrlPath)) {
            objectMap.put("avatar",imageUrlPath);
        }
        objectMap.put("birthday", tv__person_birthday.getText().toString().trim());
        objectMap.put("nickname", et__person_nickName.getText().toString().trim());
        objectMap.put("sex", userSex);
        objectMap.put("personSign", et__perso_sign.getText().toString().trim());
        minePresenter.submitUserInfo(objectMap);
    }

    @Override
    public void getUserInfoSuccess(UserInfoEntity userInfoEntity) {
        mApp.getLoadingDialog().hide();
        Glide.with(this).load(userInfoEntity.getAvatar())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.head))
                        .error(R. mipmap.head)
                .into(riv_Image);

        et__perso_sign.setText(userInfoEntity.getPersonSign());
        et__person_nickName.setText(userInfoEntity.getNickname());
        et__person_phone.setText(userInfoEntity.getPhone());
        tv__person_gender.setText(userInfoEntity.getSex());
        tv__person_birthday.setText(userInfoEntity.getBirthday());
//        if (!TextUtils.isEmpty(userInfoEntity.getPhone())){
//            et__person_phone.setFocusable(false);
//        }
//        if (!TextUtils.isEmpty(userInfoEntity.getBirthday())){
//            ll_person_birthday.setOnClickListener(null);
//        }
//        if(!TextUtils.isEmpty(userInfoEntity.getSex())){
//            ll__person_gender.setOnClickListener(null);
//        }
    }

    @Override
    public void getUserInfoFail(String info, int code ) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

    @Override
    public void submitUserInfoSuccess(UserInfoEntity userInfoEntity) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast("修改成功");
        UserInfo.setUserNiceName(userInfoEntity.getNickname());
        UserInfo.setUserInfoImage(userInfoEntity.getAvatar());
        UserInfo.setUserPersonSign(userInfoEntity.getPersonSign());
        finish();
    }

    @Override
    public void submitUserInfoFail(String info) {
        mApp.getLoadingDialog().show();
        mApp.shortToast(info);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtils.onActivityResult(requestCode, resultCode, data, getContext(), new PhotoUtils.CropHandler() {
            @Override
            public void handleCropResult(final Uri uri, int tag) {
                mApp.getLoadingDialog().show();
                Logger.d("执行uri.getPath()"+uri.getPath());
                final String s = QiNiuUtils.getInstance().compressImageUpload(uri.getPath());
                QiNiuUtils.getInstance().upload(s, new QiNiuUtils.UploadResult() {
                    @Override
                    public void success(String path) {
                        imageUrlPath = path;
                        Glide.with(PersonalActivity.this).load(uri.getPath())
                                .apply(new RequestOptions()
                                        .placeholder(R.mipmap.head))
                                .error(R. mipmap.head)
                                .into(riv_Image);
                        mApp.getLoadingDialog().hide();
                    }

                    @Override
                    public void fail() {
                        mApp.getLoadingDialog().hide();
                    }
                });
            }

            @Override
            public void handleCropError(Intent data) {
                mApp.shortToast("图片错误");
            }
        });
    }


//    @Override
//    public void UploadMultipleFileSuccess(List<String> strings) {
//        mApp.getLoadingDialog().hide();
//        Map<String, Object> objectMap = new HashMap<>();
//        objectMap.put("birthday", tv__person_birthday.getText().toString().trim());
//        objectMap.put("name", et__person_nickName.getText().toString().trim());
//        objectMap.put("phone", et__person_phone.getText().toString().trim());
//        objectMap.put("profile", strings.get(0));
//        objectMap.put("sex", userSex);
//        minePresenter.submitUserInfo(objectMap);
//    }

//    @Override
//    public void UploadMultipleFileFail(String msg) {
//        mApp.getLoadingDialog().hide();
//        mApp.shortToast(msg);
//    }
//
//
    @Override
    public void logoutSuccess() {
        mApp.getLoadingDialog().hide();
        UserInfo.removeUserInfo();
        mApp.shortToast("退出成功");
        startActivity(LoginActivity.class);
        finish();
    }

    @Override
    public void logoutFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

}
