package com.tjmedicine.emergency.ui.uart.data.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.UARTBean;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.uart.data.model.IUARTControlModel;
import com.tjmedicine.emergency.ui.uart.data.model.impl.UARTControlModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UARTRobotControlPresenter extends BasePresenter {


    private IUARTRobotControlView iuartControlView;


    private IUARTControlModel iuartControlModel = new UARTControlModel();


    public UARTRobotControlPresenter(IUARTRobotControlView iuartControlView) {
        this.iuartControlView = iuartControlView;
    }


    public void queryUARTData(Map<String, Object> map, String type) {
        iuartControlModel.queryUARTList(map, type, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    UARTBean data = (UARTBean) res.getData();
                    iuartControlView.queryUARTDataSuccess(data.getList());
                } else {
                    iuartControlView.queryUARTDataFail(res.getMsg());
                }
            }
        });
    }

    public static <T> List<T> getObjectList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
