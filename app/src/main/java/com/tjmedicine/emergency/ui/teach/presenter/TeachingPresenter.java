package com.tjmedicine.emergency.ui.teach.presenter;

import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.TeachingBean;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.teach.model.IBannerModel;
import com.tjmedicine.emergency.ui.teach.model.ITeachingModel;
import com.tjmedicine.emergency.ui.teach.model.impl.BannerModel;
import com.tjmedicine.emergency.ui.teach.model.impl.TeachingModel;
import com.tjmedicine.emergency.ui.teach.view.IBannerView;
import com.tjmedicine.emergency.ui.teach.view.ITeachingView;

import java.util.List;
import java.util.Map;

public class TeachingPresenter {


    private ITeachingView iTeachingView;

    private ITeachingModel iTeachingModel = new TeachingModel();

    public TeachingPresenter(ITeachingView iTeachingView) {
        this.iTeachingView = iTeachingView;
    }


    public void findTeachingList(Map<String, Object> map) {
        iTeachingModel.findTeachingList(map,new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    TeachingBean data = (TeachingBean) res.getData();
                    iTeachingView.findTeachingSuccess(data.getList());
                } else {
                    iTeachingView.findTeachingFail(res.getMsg());
                }
            }
        });
    }

    public void findTeachingDetail(int id) {
        iTeachingModel.findTeachingDetail(id, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    TeachingBean data = (TeachingBean) res.getData();
                    //iTeachingView.findTeachingDetailSuccess(data.getList());
                } else {
                    iTeachingView.findTeachingDetailFail(res.getMsg());
                }
            }
        });
    }
}
