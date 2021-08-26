package com.tjmedicine.emergency.ui.mine.auth.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.popup.CityPickerPopup;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.common.net.QiNiuUtils;
import com.tjmedicine.emergency.model.takePhoto.PhotoUtils;
import com.tjmedicine.emergency.model.widget.SelectPicDialog;
import com.tjmedicine.emergency.ui.mine.auth.presenter.AuthPresenter;

import java.io.File;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

@SuppressLint("NonConstantResourceId")
public class MineAuthActivity extends BaseActivity implements IAuthView {

    private AuthPresenter authPresenter = new AuthPresenter(this);


    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.iv_id_card_front)
    ImageView iv_id_card_front;
    @BindView(R.id.iv_id_card_back)
    ImageView iv_id_card_back;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.btn_auth)
    Button btn_auth;
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

    private int selectIndex = 1;
    private String imgIdCardFront, imgIdCardBack;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_mine_auth;
    }

    @Override
    protected void initView() {
        mTitle.setText("实名认证");
        authPresenter.findRealAuth();
        initListener();
        et_phone.setText(UserInfo.getUserPhone());
    }

    private void initListener() {
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

        btn_auth.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                submit();

            }
        });

        tv_chooseAddress.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                CityPickerPopup popup = new CityPickerPopup(MineAuthActivity.this);
                popup.setCityPickerListener(new CityPickerListener() {
                    @Override
                    public void onCityConfirm(String province, String city, String area, View v) {
                        tv_chooseAddress.setText(province + city + area);
                    }

                    @Override
                    public void onCityChange(String province, String city, String area) {
                    }
                });
                new XPopup.Builder(MineAuthActivity.this)
                        .asCustom(popup)
                        .show();
            }
        });
    }

    private void submit() {

        if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
            mApp.shortToast("请输入姓名!");
            return;
        }
        if (TextUtils.isEmpty(tv_chooseAddress.getText().toString().trim())) {
            mApp.shortToast("请选择常住地址!");
            return;
        }
        if (TextUtils.isEmpty(et_address.getText().toString().trim())) {
            mApp.shortToast("请输入详细地址!");
            return;
        }

        if (TextUtils.isEmpty(imgIdCardFront)) {
            mApp.shortToast("请上传身份证正面!");
            return;
        }
        if (TextUtils.isEmpty(imgIdCardBack)) {
            mApp.shortToast("请上传身份证背面!");
            return;
        }
        mApp.getLoadingDialog().show();
        authPresenter.goRealAuth(et_name.getText().toString().trim(),
                tv_chooseAddress.getText().toString().trim() + et_address.getText().toString().trim(),
                imgIdCardFront, imgIdCardBack);
    }

    @Override
    public void goRealAuthSuccess() {
        mApp.getLoadingDialog().hide();
        mApp.shortToast("预计3个工作日内审核完毕,审核结果会以短信通知\n" +
                "到您的注册手机上");
        finish();
    }

    @Override
    public void goRealAuthFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

    @Override
    public void findRealAuthSuccess(VolunteerBean volunteerBeans) {
        mApp.getLoadingDialog().hide();
        //审核状态：1审核中，2审核通过，3审核拒绝 4未申请
        int type = volunteerBeans.getType();
        if (type == 1) {
            ll_review.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ll_review_yes.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            ll_review_no.setVisibility(View.VISIBLE);
            tv_refuse.setText(volunteerBeans.getReason());
            btn_upload.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    scroview.setVisibility(View.VISIBLE);
                    ll_review_no.setVisibility(View.GONE);
                }
            });
        } else if (type == 4) {
            scroview.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(volunteerBeans.getIdUpUrl())) {
                imgIdCardFront = volunteerBeans.getIdUpUrl();
                Glide.with(this).load(volunteerBeans.getIdUpUrl()).into(iv_id_card_front);
            }
            if (!TextUtils.isEmpty(volunteerBeans.getIdDownUrl())) {
                imgIdCardBack = volunteerBeans.getIdDownUrl();
                Glide.with(this).load(volunteerBeans.getIdDownUrl()).into(iv_id_card_back);
            }
        }
    }

    @Override
    public void findRealAuthFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtils.onActivityResult(requestCode, resultCode, data, getContext(), new PhotoUtils.CropHandler() {
            @Override
            public void handleCropResult(final Uri uri, int tag) {
                Luban.with(EmergencyApplication.getContext())
                        .load(uri.getPath())
                        .ignoreBy(100)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                try {
                                    Logger.d("图片返回路径uri----"+file.getPath());
                                    Logger.d("图片返回路径aaa----"+file.getAbsolutePath());
                                    QiNiuUtils.getInstance().upload(uri.getPath(), new QiNiuUtils.UploadResult() {
                                        @Override
                                        public void success(String path) {
                                            Logger.d("图片返回路径+" + path);
                                            mApp.getLoadingDialog().hide();
                                            try {
                                                if (selectIndex == 1) {
                                                    imgIdCardFront = path;
                                                    Glide.with(MineAuthActivity.this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(uri.getPath()).into(iv_id_card_front);
                                                } else if (selectIndex == 2) {
                                                    imgIdCardBack = path;
                                                    Glide.with(MineAuthActivity.this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(uri.getPath()).into(iv_id_card_back);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void fail() {
                                            mApp.shortToast("图片错误，请重新上传!");
                                            mApp.getLoadingDialog().hide();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        }).launch();
            }

            @Override
            public void handleCropError(Intent data) {
                mApp.shortToast("图片错误");
            }
        });
      /*  PhotoUtils.onActivityResult(requestCode, resultCode, data, getContext(), new PhotoUtils.CropHandler() {
            @Override
            public void handleCropResult(final Uri uri, int tag) {
                mApp.getLoadingDialog().show();
                Logger.d("图片返回路径+ 111111111111" );
                Logger.d("图片返回路径uri"+uri.getPath());
               // final String s = QiNiuUtils.getInstance().compressImageUpload(uri.getPath());
                QiNiuUtils.getInstance().upload(uri.getPath(), new QiNiuUtils.UploadResult() {
                    @Override
                    public void success(String path) {
                        Logger.d("图片返回路径+" + path);
                        mApp.getLoadingDialog().hide();
                        try {
                            if (selectIndex == 1) {
                                imgIdCardFront = path;
                                Glide.with(MineAuthActivity.this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(uri.getPath()).into(iv_id_card_front);
                            } else if (selectIndex == 2) {
                                imgIdCardBack = path;
                                Glide.with(MineAuthActivity.this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(uri.getPath()).into(iv_id_card_back);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void fail() {
                        mApp.shortToast("图片错误，请重新上传!");
                        mApp.getLoadingDialog().hide();
                    }
                });
            }

            @Override
            public void handleCropError(Intent data) {
                mApp.shortToast("图片错误");
            }
        });*/
    }
}
