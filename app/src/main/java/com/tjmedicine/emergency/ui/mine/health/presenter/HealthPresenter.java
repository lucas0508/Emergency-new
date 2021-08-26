package com.tjmedicine.emergency.ui.mine.health.presenter;

import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.HealthAllFileBeen;
import com.tjmedicine.emergency.common.bean.HealthFileBeen;
import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.mine.auth.model.IAuthModel;
import com.tjmedicine.emergency.ui.mine.auth.model.impl.AuthModel;
import com.tjmedicine.emergency.ui.mine.auth.view.IAuthView;
import com.tjmedicine.emergency.ui.mine.health.IHealthView;
import com.tjmedicine.emergency.ui.mine.health.model.IHealthModel;
import com.tjmedicine.emergency.ui.mine.health.model.impl.HealthModel;

import java.util.HashMap;
import java.util.Map;

public class HealthPresenter extends BasePresenter {

    private IHealthView iHealthView;

    private IHealthModel iHealthModel = new HealthModel();

    public HealthPresenter(IHealthView iHealthView) {
        this.iHealthView = iHealthView;
    }


    public void addHealthRecords(Map<String, Object> Object) {
        iHealthModel.addHealthRecords(Object, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iHealthView.addHealthRecordsSuccess();
                } else {
                    iHealthView.addHealthRecordsFail(res.getMsg());
                }
            }
        });
    }


    public void delHealth(String id){
        iHealthModel.delHealthRecords(id, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iHealthView.delHealthRecordsSuccess();
                } else {
                    iHealthView.delHealthRecordsFail(res.getMsg());
                }
            }
        });
    }


    public void findHealthRecordsList() {
        iHealthModel.findHealthRecordsList(new IBaseModel.OnCallbackDataListener() {
            @Override
            public void callback(ResponseDataEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iHealthView.findHealthRecordsListSuccess(res.getData());
                } else {
                    iHealthView.findHealthRecordsListFail(res.getMsg());
                }
            }
        });
    }

    public void findHealthRecordsTag() {
        iHealthModel.findHealthRecordsTag(new IBaseModel.OnCallbackDataListener() {
            @Override
            public void callback(ResponseDataEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iHealthView.findHealthRecordsTagSuccess(res.getData());
                } else {
                    iHealthView.findHealthRecordsTagFail(res.getMsg());
                }
            }
        });
    }

    public void findHealthRecordsDetail(int id) {
        iHealthModel.findHealthRecordsDetail(id, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iHealthView.findHealthRecordsDetailSuccess((HealthAllFileBeen) res.getData());
                } else {
                    iHealthView.findHealthRecordsDetailFail(res.getMsg());
                }
            }
        });
    }

}
