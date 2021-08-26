package com.tjmedicine.emergency.ui.mine.contact.view;

import com.tjmedicine.emergency.ui.bean.ContactBean;

import java.util.List;

public interface IContactView {


    void addContactSuccess();

    void addContactFail(String info);


    void findContactSuccess(List<ContactBean> contactBean);

    void findContactFail(String info);


    void delContactSuccess(String id);

    void delContactFail(String info);


}
