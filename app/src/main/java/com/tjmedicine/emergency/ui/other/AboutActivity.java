package com.tjmedicine.emergency.ui.other;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mob.MobSDK;
import com.mob.PrivacyPolicy;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.VersionUpdateEntity;
import com.tjmedicine.emergency.common.dialog.VersionUpdateDialog;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.ui.other.presenter.VersionUpdatePresenter;
import com.tjmedicine.emergency.utils.CompareVersions;
import com.tjmedicine.emergency.utils.DevicePermissionsUtils;

import butterknife.BindView;

import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_FLAG;
import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_URL;


public class AboutActivity extends BaseActivity implements IVersionUpdateView {
    private VersionUpdatePresenter versionUpdate = new VersionUpdatePresenter(this);
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.ll_product_brief)
    LinearLayout ll_product_brief;
    @BindView(R.id.company_profile)
    LinearLayout company_profile;
    @BindView(R.id.ll_update_version)
    LinearLayout ll_update_version;
    @BindView(R.id.tv_login_agreement_user_agreement)
    TextView mLoginAgreement;
    @BindView(R.id.tv_login_agreement_privacyPolicy)
    TextView mLoginAgreementPrivacy;
    private String url;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        mTitle.setText("关于我们");
        tv_version.setText("版本  v" + DevicePermissionsUtils.getAppCurrentVersion());
        mLoginAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, WebActivity.class);
                intent.putExtra(WEB_KEY_URL, GlobalConstants.AGREEMENT_URL);
                intent.putExtra(WEB_KEY_FLAG, 1);
                startActivity(intent);
            }
        });
        mLoginAgreementPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AboutActivity.this, WebActivity.class);
                intent1.putExtra(WEB_KEY_URL, url);
                intent1.putExtra(WEB_KEY_FLAG, 1);
                startActivity(intent1);
            }
        });

        ll_update_version.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mApp.getLoadingDialog().show();
                versionUpdate.updateVersion();
            }
        });

        ll_product_brief.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                startActivity(ProductBriefActivity.class);
            }
        });
        company_profile.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                startActivity(CompanyProfileActivity.class);
            }
        });

        // 异步方法查询隐私,locale可以为null或不设置，默认使用当前系统语言
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

    @Override
    public void updateVersionSuccess(VersionUpdateEntity versionUpdateEntity) {
        mApp.getLoadingDialog().hide();
        String clientVersion = versionUpdateEntity.getVersion();
        String downloadUrl = versionUpdateEntity.getUrl();
        int updateInstall = versionUpdateEntity.getIfUp();
        String content = versionUpdateEntity.getContent();
        if (CompareVersions.compare(clientVersion, DevicePermissionsUtils.getAppCurrentVersion())) {
            VersionUpdateDialog versionUpdateDialog = new VersionUpdateDialog(AboutActivity.this);
            versionUpdateDialog.showNoticeDialog(clientVersion, String.valueOf(updateInstall), downloadUrl,content);
        }else {
            mApp.shortToast("当前是最新版本");
        }
    }

    @Override
    public void updateVersionFail(String info) {
        mApp.shortToast(info);
    }
}
