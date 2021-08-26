package com.tjmedicine.emergency.ui.mine.auth.presenter;

import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.mine.auth.model.IAuthModel;
import com.tjmedicine.emergency.ui.mine.auth.model.impl.AuthModel;
import com.tjmedicine.emergency.ui.mine.auth.view.IAuthView;
import com.tjmedicine.emergency.ui.mine.contact.model.IContactModel;
import com.tjmedicine.emergency.ui.mine.contact.model.impl.ContactModel;
import com.tjmedicine.emergency.ui.mine.contact.view.IContactView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthPresenter extends BasePresenter {

    private IAuthView iAuthView;

    private IAuthModel iAuthModel = new AuthModel();

    public AuthPresenter(IAuthView iAuthView) {
        this.iAuthView = iAuthView;
    }


    public void goRealAuth(String mRelationName, String mRelation,
                           String imgFront,String imgBack) {
        Map<String, Object> Object = new HashMap<>();
        Object.put("username", mRelationName);
        Object.put("authAddress", mRelation);
        Object.put("idUpUrl", imgFront);
        Object.put("idDownUrl", imgBack);

        iAuthModel.goRealAuth(Object, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iAuthView.goRealAuthSuccess();
                } else {
                    iAuthView.goRealAuthFail(res.getMsg());
                }
            }
        });
    }

    public void findRealAuth() {
        iAuthModel.findRealAuth(new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iAuthView.findRealAuthSuccess((VolunteerBean) res.getData());
                } else {
                    iAuthView.findRealAuthFail(res.getMsg());
                }
            }
        });
    }

}
