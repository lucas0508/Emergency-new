package com.tjmedicine.emergency.ui.mine.volunteer.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface IApplyVolunteerModel extends IBaseModel{


    void applyVolunteer(Map<String, Object> map, final IBaseModel.OnCallbackListener listener);


    void queryVolunteer(IBaseModel.OnCallbackListener listener);


}
