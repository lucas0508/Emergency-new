package com.tjmedicine.emergency.ui.map.presenter;

import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.map.model.IMapDataModel;
import com.tjmedicine.emergency.ui.map.model.impl.MapDataModel;
import com.tjmedicine.emergency.ui.teach.model.IBannerModel;
import com.tjmedicine.emergency.ui.teach.model.impl.BannerModel;
import com.tjmedicine.emergency.ui.teach.view.IBannerView;

public class MapDataPresenter {


    private IMapDataView iMapDataView;

    private IMapDataModel iMapDataModel = new MapDataModel();

    public MapDataPresenter(IMapDataView iMapDataView) {
        this.iMapDataView = iMapDataView;
    }


    public void queryMapData(int type){
        iMapDataModel.queryMapData(type,new IBaseModel.OnCallbackDataListener() {
            @Override
            public void callback(ResponseDataEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())){
                    iMapDataView.queryMapDataSuccess(res.getData());
                }else {
                    iMapDataView.queryMapDataFail(res.getMsg());
                }
            }
        });
    }

    public void updateAddress(double lat,double lng){
        iMapDataModel.updateAddress(lat, lng, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())){
                    iMapDataView.updateAddressSuccess();
                }else {
                    iMapDataView.updateAddressFail(res.getMsg());
                }
            }
        });
    }

}
