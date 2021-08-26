package com.tjmedicine.emergency.ui.teach.presenter;

import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.ui.teach.model.IBannerModel;
import com.tjmedicine.emergency.ui.teach.model.impl.BannerModel;
import com.tjmedicine.emergency.ui.teach.view.IBannerView;

public class BannerPresenter {


    private IBannerView iBannerView;

    private IBannerModel iBannerModel = new BannerModel();

    public BannerPresenter(IBannerView iBannerView) {
        this.iBannerView = iBannerView;
    }


    public void queryBannerData(){

        iBannerModel.queryBannerData(new IBaseModel.OnCallbackDataListener() {
            @Override
            public void callback(ResponseDataEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())){
                    iBannerView.queryBannerSuccess(res.getData());
                }else {
                    iBannerView.queryBannerFail(res.getMsg());
                }
            }
        });

    }
}
