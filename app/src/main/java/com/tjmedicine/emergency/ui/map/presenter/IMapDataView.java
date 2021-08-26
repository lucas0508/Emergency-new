package com.tjmedicine.emergency.ui.map.presenter;

import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.BannerBean;
import com.tjmedicine.emergency.common.bean.MapDataBeen;

import java.util.List;

public interface IMapDataView {

    void queryMapDataSuccess(List<MapDataBeen> mapDataBeens);

    void queryMapDataFail(String info);


    void updateAddressSuccess();

    void updateAddressFail(String info);

}
