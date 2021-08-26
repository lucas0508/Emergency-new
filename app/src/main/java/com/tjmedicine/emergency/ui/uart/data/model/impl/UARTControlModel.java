package com.tjmedicine.emergency.ui.uart.data.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.bean.UARTBean;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.uart.data.model.IUARTControlModel;
import com.tjmedicine.emergency.ui.uart.data.presenter.PDScoreData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 */

public class UARTControlModel extends BaseModel implements IUARTControlModel {


    @Override
    public void sendUARTData(List<String> list, String type, List<String> listPD_trough, OnCallbackListener listener) {
        Map<String, Object> map = new HashMap(15);
        map.put("type", type);
        map.put("list", list);
        map.put("listPD_trough", listPD_trough);
        HttpProvider.doPost(GlobalConstants.APP_UART_TRAIN_ADD, map, responseText -> {
            executeCallback(responseText, new TypeToken<ResponseEntity<PDScoreData>>() {
            }.getType(), listener);
        });
    }


    @Override
    public void queryUARTList(Map<String, Object> info, String type, OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_UART_TRAIN_LIST + HttpProvider.getUrlDataByMap(info), responseText -> {
            executeCallback(responseText, new TypeToken<ResponseEntity<UARTBean>>() {
            }.getType(), listener);
        });
    }

    @Override
    public void queryUARTDataDetail(String trainId, OnCallbackListener listener) {
//        HttpProvider.doGet(GlobalConstants.APP_UART_TRAIN_INFO, responseText -> {
//            executeDataCallback(responseText, new TypeToken<ResponseDataEntity<HomeWorkerTypeEntity>>() {
//            }.getType(), listener);
//        });
    }
}
