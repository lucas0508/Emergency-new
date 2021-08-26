package com.tjmedicine.emergency.ui.mine.contact.presenter;

import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.mine.contact.model.IContactModel;
import com.tjmedicine.emergency.ui.mine.contact.model.impl.ContactModel;
import com.tjmedicine.emergency.ui.mine.contact.view.IContactView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactPresenter extends BasePresenter {

    private IContactView iContactView;

    private IContactModel iContactModel = new ContactModel();

    public ContactPresenter(IContactView iContactView) {
        this.iContactView = iContactView;
    }


    public void addContact(String mRelationName, String mRelation, String mRelationPhone) {
        Map<String, Object> Object = new HashMap<>();
        Object.put("username", mRelationName);
        Object.put("phone", mRelationPhone);
        Object.put("relation", mRelation);
        iContactModel.addContact(Object, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iContactView.addContactSuccess();
                } else {
                    iContactView.addContactFail(res.getMsg());
                }
            }
        });
    }

    public void findContact() {
        iContactModel.findContact(new IBaseModel.OnCallbackDataListener() {
            @Override
            public void callback(ResponseDataEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iContactView.findContactSuccess((List<ContactBean>) res.getData());
                } else {
                    iContactView.findContactFail(res.getMsg());
                }
            }
        });
    }

    public void delContact(String id) {
        iContactModel.editContact(id, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iContactView.delContactSuccess(id);
                } else {
                    iContactView.delContactFail(res.getMsg());
                }
            }
        });
    }
}
