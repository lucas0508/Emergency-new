package com.tjmedicine.emergency.ui.uart.data.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.List;
import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 */

public interface IUARTControlModel extends IBaseModel {


    void sendUARTData(List<String> list, String type, List<String> listPD_trough, final OnCallbackListener listener);


    void queryUARTList(Map<String, Object> info, String type, OnCallbackListener listener);


    void queryUARTDataDetail(String trainId, OnCallbackListener listener);

}
