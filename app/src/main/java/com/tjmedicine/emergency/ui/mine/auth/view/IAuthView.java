package com.tjmedicine.emergency.ui.mine.auth.view;

import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.ui.bean.ContactBean;

import java.util.List;

public interface IAuthView {


    void goRealAuthSuccess();

    void goRealAuthFail(String info);


    void findRealAuthSuccess(VolunteerBean volunteerBeans);

    void findRealAuthFail(String info);


}
