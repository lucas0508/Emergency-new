package com.tjmedicine.emergency.ui.map.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface IMapDataModel extends IBaseModel {


    void updateAddress(double lat,double lng,OnCallbackListener listener);

    /**
     * @param type  1:普通人员,2:医生,3:模拟人,4:急救设备(不传值查询全部)
     * @param listener
     */
    void queryMapData(int type, OnCallbackDataListener listener);


}
