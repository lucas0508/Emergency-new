package com.tjmedicine.emergency.ui.mine.volunteer.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.popup.CityPickerPopup;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.QiNiuUtils;
import com.tjmedicine.emergency.model.takePhoto.PhotoUtils;
import com.tjmedicine.emergency.model.widget.SelectPicDialog;
import com.tjmedicine.emergency.ui.mine.auth.view.MineAuthActivity;
import com.tjmedicine.emergency.ui.mine.volunteer.presenter.ApplyVolunteerPresenter;
import com.tjmedicine.emergency.ui.other.WebActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

@SuppressLint("NonConstantResourceId")
public class ApplyVolunteerActivity extends BaseActivity implements IApplyVolunteerView {


    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.et_name)
    TextView mName;
    @BindView(R.id.et_phone)
    TextView mPhone;
    @BindView(R.id.et_address)
    EditText mAddress;
    @BindView(R.id.tv_user)
    RadioButton mUser;
    @BindView(R.id.tv_doctor)
    RadioButton mDoctor;
    @BindView(R.id.iv_cardiopulmonary_resuscitation)
    ImageView mCardiopulmonaryResuscitation;
    @BindView(R.id.iv_id_card_front)
    ImageView iv_id_card_front;
    @BindView(R.id.iv_id_card_back)
    ImageView iv_id_card_back;
    @BindView(R.id.iv_doctors_certificate)
    ImageView mDoctorsCertificate;
    @BindView(R.id.checkbox)
    AppCompatCheckBox mCheckBox;
    @BindView(R.id.btn_apply)
    Button mBtnApply;
    @BindView(R.id.scroview)
    ScrollView scroview;
    @BindView(R.id.btn_upload)
    Button btn_upload;
    @BindView(R.id.ll_review)
    LinearLayout ll_review;
    @BindView(R.id.ll_review_yes)
    LinearLayout ll_review_yes;
    @BindView(R.id.ll_review_no)
    LinearLayout ll_review_no;
    @BindView(R.id.tv_refuse)
    TextView tv_refuse;
    @BindView(R.id.tv_chooseAddress)
    TextView tv_chooseAddress;
    @BindView(R.id.tv_121_agreement)
    TextView tv_121_agreement;
    String imgIdCardFront = "";
    String imgIdCardBack = "";
    String imgCardiopulmonaryResuscitation = ""; //心肺复苏证
    String imgDoctorsCertificate = "";//医生资质证书


    private String userType = "1";
    private ApplyVolunteerPresenter applyVolunteerPresenter = new ApplyVolunteerPresenter(this);
    private int selectIndex = 1;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_apply_volunteer;
    }

    @Override
    protected void initView() {
        mTitle.setText("申请成为志愿者");
        mBtnApply.setEnabled(false);
        mPhone.setText(UserInfo.getUserPhone());
        initListeners();
        mApp.getLoadingDialog().show();
        applyVolunteerPresenter.queryVolunteer();
    }


    private void initListeners() {
        mUser.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mDoctor.setChecked(false);
                userType = "1";
                mCardiopulmonaryResuscitation.setVisibility(View.VISIBLE);
                mDoctorsCertificate.setVisibility(View.GONE);


            }
        });
        mDoctor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                userType = "2";
                mUser.setChecked(false);
                mCardiopulmonaryResuscitation.setVisibility(View.GONE);
                mDoctorsCertificate.setVisibility(View.VISIBLE);
            }
        });
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBtnApply.setEnabled(true);
            } else {
                mBtnApply.setEnabled(false);
            }

        });


        mBtnApply.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                submit();
            }
        });

        iv_id_card_front.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mApp.getSelectPicDialog()
                        .show(new SelectPicDialog.Callback() {
                            @Override
                            public void clickItem(int position) {
                                selectIndex = 1;
                                if (position == 0) {
                                    PhotoUtils.autoObtainCameraPermission(getContext(), false);
                                } else if (position == 1) {
                                    PhotoUtils.autoObtainStoragePermission(getContext(), false);
                                }
                            }
                        });
            }
        });
        iv_id_card_back.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mApp.getSelectPicDialog()
                        .show(new SelectPicDialog.Callback() {
                            @Override
                            public void clickItem(int position) {
                                selectIndex = 2;
                                if (position == 0) {
                                    PhotoUtils.autoObtainCameraPermission(getContext(), false);
                                } else if (position == 1) {
                                    PhotoUtils.autoObtainStoragePermission(getContext(), false);
                                }
                            }
                        });
            }
        });
        mCardiopulmonaryResuscitation.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mApp.getSelectPicDialog()
                        .show(new SelectPicDialog.Callback() {
                            @Override
                            public void clickItem(int position) {
                                selectIndex = 3;
                                if (position == 0) {
                                    PhotoUtils.autoObtainCameraPermission(getContext(), false);
                                } else if (position == 1) {
                                    PhotoUtils.autoObtainStoragePermission(getContext(), false);
                                }
                            }
                        });
            }
        });
        mDoctorsCertificate.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mApp.getSelectPicDialog()
                        .show(new SelectPicDialog.Callback() {
                            @Override
                            public void clickItem(int position) {
                                selectIndex = 4;
                                if (position == 0) {
                                    PhotoUtils.autoObtainCameraPermission(getContext(), false);
                                } else if (position == 1) {
                                    PhotoUtils.autoObtainStoragePermission(getContext(), false);
                                }
                            }
                        });
            }
        });

        tv_chooseAddress.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                CityPickerPopup popup = new CityPickerPopup(ApplyVolunteerActivity.this);
                popup.setCityPickerListener(new CityPickerListener() {
                    @Override
                    public void onCityConfirm(String province, String city, String area, View v) {
                        tv_chooseAddress.setText(province + city + area);
                    }

                    @Override
                    public void onCityChange(String province, String city, String area) {
                    }
                });
                new XPopup.Builder(ApplyVolunteerActivity.this)
                        .asCustom(popup)
                        .show();
            }
        });


        tv_121_agreement.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //点击代码
                Intent intent = new Intent(ApplyVolunteerActivity.this, WebActivity.class);
                intent.putExtra(Constants.WEB_KEY_URL, GlobalConstants.PRIVACYPOLICY_URL);
                intent.putExtra(Constants.WEB_KEY_FLAG, 1);
                startActivity(intent);
            }
        });
    }


    private void submit() {
        if (TextUtils.isEmpty(mName.getText().toString().trim())) {
            mApp.shortToast("请输入姓名!");
            return;
        }
        if (TextUtils.isEmpty(tv_chooseAddress.getText().toString().trim())) {
            mApp.shortToast("请选择常住地址!");
            return;
        }
        if (TextUtils.isEmpty(mAddress.getText().toString().trim())) {
            mApp.shortToast("请输入详细地址!");
            return;
        }

        Map<String, Object> objectMap = new HashMap<>();
        if (TextUtils.isEmpty(imgIdCardFront)) {
            mApp.shortToast("请上传身份证正面!");
            return;
        }
        if (TextUtils.isEmpty(imgIdCardBack)) {
            mApp.shortToast("请上传身份证背面!");
            return;
        }
        if (userType.equals("1")) {
            if (!TextUtils.isEmpty(imgCardiopulmonaryResuscitation)) {
                objectMap.put("bookUrl", imgCardiopulmonaryResuscitation);

            } else {
                mApp.shortToast("请上传心肺复苏证!");
                return;
            }
        } else {
            if (!TextUtils.isEmpty(imgDoctorsCertificate)) {
                objectMap.put("bookUrl", imgDoctorsCertificate);
            } else {
                mApp.shortToast("请上传医生职业资格证!");
                return;
            }
        }
        objectMap.put("username",  mName.getText().toString().trim());
        objectMap.put("volunteerAddress", tv_chooseAddress.getText().toString().trim()+mAddress.getText().toString().trim());
        objectMap.put("idUpUrl", imgIdCardFront);
        objectMap.put("idDownUrl", imgIdCardBack);
        objectMap.put("lng", UserInfo.getLang());
        objectMap.put("lat", UserInfo.getLat());
        objectMap.put("userType", userType);
        mApp.getLoadingDialog().show();
        applyVolunteerPresenter.applyVolunteer(objectMap);
    }


    @Override
    public void applyVolunteerSuccess() {
        mApp.getLoadingDialog().hide();
        mApp.shortToast("预计3个工作日内审核完毕,审核结果会以短信通知\n" +
                "到您的注册手机上");
        finish();
    }

    @Override
    public void applyVolunteerFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

    @Override
    public void queryVolunteerSuccess(VolunteerBean volunteerBean) {
        mApp.getLoadingDialog().hide();
        //审核状态：1审核中，2审核通过，3审核拒绝 4未申请
        int type = volunteerBean.getType();
        if (type == 1) {
            ll_review.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ll_review_yes.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            ll_review_no.setVisibility(View.VISIBLE);
            tv_refuse.setText(volunteerBean.getReason());
            btn_upload.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    scroview.setVisibility(View.VISIBLE);
                    ll_review_no.setVisibility(View.GONE);
                }
            });
        } else if (type == 4) {
            scroview.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(volunteerBean.getIdUpUrl())) {
                imgIdCardFront = volunteerBean.getIdUpUrl();
                Glide.with(this).load(volunteerBean.getIdUpUrl()).into(iv_id_card_front);
            }
            if (!TextUtils.isEmpty(volunteerBean.getIdDownUrl())) {
                imgIdCardBack = volunteerBean.getIdDownUrl();
                Glide.with(this).load(volunteerBean.getIdDownUrl()).into(iv_id_card_back);
            }
        }
    }

    @Override
    public void queryVolunteerFail(String info, int code) {
        mApp.getLoadingDialog().hide();
        if (code == 303) {
            scroview.setVisibility(View.VISIBLE);
        } else {
            mApp.shortToast(info);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtils.onActivityResult(requestCode, resultCode, data, getContext(), new PhotoUtils.CropHandler() {
            @Override
            public void handleCropResult(final Uri uri, int tag) {
                mApp.getLoadingDialog().show();
                final String s = QiNiuUtils.getInstance().compressImageUpload(uri.getPath());
                QiNiuUtils.getInstance().upload(s, new QiNiuUtils.UploadResult() {
                    @Override
                    public void success(String path) {
                        mApp.getLoadingDialog().hide();
                        try {
                            if (selectIndex == 1) {
                                imgIdCardFront = path;
                                Glide.with(ApplyVolunteerActivity.this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(uri.getPath()).into(iv_id_card_front);
                            } else if (selectIndex == 2) {
                                imgIdCardBack = path;
                                Glide.with(ApplyVolunteerActivity.this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(uri.getPath()).into(iv_id_card_back);
                            } else if (selectIndex == 3) {
                                imgCardiopulmonaryResuscitation = path;
                                Glide.with(ApplyVolunteerActivity.this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(uri.getPath()).into(mCardiopulmonaryResuscitation);
                            } else if (selectIndex == 4) {
                                imgDoctorsCertificate = path;
                                Glide.with(ApplyVolunteerActivity.this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(uri.getPath()).into(mDoctorsCertificate);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

}
