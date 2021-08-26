package com.tjmedicine.emergency.ui.mine.health.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface IHealthModel extends IBaseModel{


    void addHealthRecords(Map<String, Object> map, final OnCallbackListener listener);

    void delHealthRecords(String id,final OnCallbackListener listener);

    void findHealthRecordsList(OnCallbackDataListener listener);


    void findHealthRecordsDetail(int  id ,OnCallbackListener listener);


    void findHealthRecordsTag(OnCallbackDataListener listener);
}
