package com.tjmedicine.emergency.ui.mine.volunteer.presenter;

import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.mine.volunteer.model.IApplyVolunteerModel;
import com.tjmedicine.emergency.ui.mine.volunteer.model.impl.ApplyVolunteerModel;
import com.tjmedicine.emergency.ui.mine.volunteer.view.IApplyVolunteerView;

import java.util.Map;

public class ApplyVolunteerPresenter extends BasePresenter {


    private IApplyVolunteerView iApplyVolunteerView;

    private IApplyVolunteerModel iApplyVolunteerModel = new ApplyVolunteerModel();


    public ApplyVolunteerPresenter(IApplyVolunteerView iApplyVolunteerView) {
        this.iApplyVolunteerView = iApplyVolunteerView;
    }


    public void applyVolunteer(Map<String, Object> map) {
        iApplyVolunteerModel.applyVolunteer(map, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iApplyVolunteerView.applyVolunteerSuccess();
                } else {
                    iApplyVolunteerView.applyVolunteerFail(res.getMsg());
                }
            }
        });
    }


    public void queryVolunteer() {
        iApplyVolunteerModel.queryVolunteer(new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iApplyVolunteerView.queryVolunteerSuccess((VolunteerBean) res.getData());
                } else {
                    iApplyVolunteerView.queryVolunteerFail(res.getMsg(),res.getCode());
                }
            }
        });
    }

}
