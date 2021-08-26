package com.tjmedicine.emergency.ui.mine.contact.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.mine.contact.presenter.ContactPresenter;
import com.tjmedicine.emergency.utils.ValidateUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

public class EmergencyAddContactActivity extends BaseActivity implements IContactView {

    private ContactPresenter contactPresenter = new ContactPresenter(this);


    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.et_name)
    EditText mRelationName;
    @BindView(R.id.et_relation)
    EditText mRelation;
    @BindView(R.id.et_relation_phone)
    EditText mRelationPhone;
    @BindView(R.id.btn_add)
    Button btn_add;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_emergencyaddcontact;
    }

    @Override
    protected void initView() {
        mTitle.setText("紧急联系人");


        ContactBean contactData = (ContactBean) getIntent().getSerializableExtra("contactData");
        if (null != contactData) {
            mRelationName.setText(contactData.getUsername());
            mRelation.setText(contactData.getRelation());
            mRelationPhone.setText(contactData.getPhone());
        }
        btn_add.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        if (TextUtils.isEmpty(mRelationName.getText().toString().trim())) {
            mApp.shortToast("请输入姓名!");
            return;
        }
        if (TextUtils.isEmpty(mRelation.getText().toString().trim())) {
            mApp.shortToast("请输入与他的关系!");
            return;
        }
        Logger.e(mRelationPhone.getText().toString().trim());
        if (!ValidateUtils.validatePhone((mRelationPhone.getText().toString().trim()))) {
            mApp.shortToast("请输入您的手机号!");
            return;
        }
        mApp.getLoadingDialog().show();
        contactPresenter.addContact(mRelationName.getText().toString().trim(),
                mRelation.getText().toString().trim(), mRelationPhone.getText().toString().trim());
    }

    @Override
    public void addContactSuccess() {
        mApp.getLoadingDialog().hide();
        mApp.shortToast("添加成功！");
        finish();
    }

    @Override
    public void addContactFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

    @Override
    public void findContactSuccess(List<ContactBean> contactBean) {

    }


    @Override
    public void findContactFail(String info) {

    }

    @Override
    public void delContactSuccess(String id) {

    }

    @Override
    public void delContactFail(String info) {

    }
}
