package com.tjmedicine.emergency.ui.mine.volunteer.view;

import com.tjmedicine.emergency.common.bean.VolunteerBean;

public interface IApplyVolunteerView {


    void applyVolunteerSuccess();

    void applyVolunteerFail(String info);


    void queryVolunteerSuccess(VolunteerBean volunteerBean);


    void queryVolunteerFail(String info,int code );



}
