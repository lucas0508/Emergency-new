package com.tjmedicine.emergency.ui.mine.apply.presenter;

import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.bean.SignUpBean;
import com.tjmedicine.emergency.ui.me.model.MineFragmentModel;
import com.tjmedicine.emergency.ui.me.model.impl.IMineFragmentModel;
import com.tjmedicine.emergency.ui.mine.apply.model.ISignUpModel;
import com.tjmedicine.emergency.ui.mine.apply.model.impl.SignUpModel;
import com.tjmedicine.emergency.ui.mine.apply.view.IMineApplyView;
import com.tjmedicine.emergency.ui.mine.contact.model.IContactModel;
import com.tjmedicine.emergency.ui.mine.contact.model.impl.ContactModel;
import com.tjmedicine.emergency.ui.mine.contact.view.IContactView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineApplyPresenter extends BasePresenter {

    private IMineApplyView iMineApplyView;

    private ISignUpModel iSignUpModel = new SignUpModel();

    public MineApplyPresenter(IMineApplyView iMineApplyView) {
        this.iMineApplyView = iMineApplyView;
    }


    public void findSignUp() {
        iSignUpModel.findSignUp(new IBaseModel.OnCallbackDataListener() {
            @Override
            public void callback(ResponseDataEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iMineApplyView.findSignUpSuccess((List<SignUpBean>) res.getData());
                } else {
                    iMineApplyView.findSignUpFail(res.getMsg());
                }
            }
        });
    }

    public void signUp(String id) {
        iSignUpModel.signUp(id, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iMineApplyView.signUpSuccess();
                } else {
                    iMineApplyView.signUpFail(res.getMsg());
                }
            }
        });
    }
}
