package com.tjmedicine.emergency.ui.mine.apply.view;

import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.bean.SignUpBean;

import java.util.List;

public interface IMineApplyView {

    void findSignUpSuccess(List<SignUpBean> contactBean);

    void findSignUpFail(String info);

    void signUpSuccess();

    void signUpFail(String info);


}
