package com.tjmedicine.emergency.ui.uart.data.presenter;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.dialog.DialogUtil;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.login.model.ILoginModel;
import com.tjmedicine.emergency.ui.uart.data.model.IUARTControlModel;
import com.tjmedicine.emergency.ui.uart.data.model.impl.UARTControlModel;

import java.util.ArrayList;
import java.util.List;

public class UARTControlPresenter extends BasePresenter {


    private IUARTControlView iuartControlView;


    private IUARTControlModel iuartControlModel = new UARTControlModel();


    public UARTControlPresenter(IUARTControlView iuartControlView) {
        this.iuartControlView = iuartControlView;
    }

    public void postUARTData(List<String> list, String type,List<String> listPD_trough) {
        iuartControlModel.sendUARTData(list, type,listPD_trough, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                     PDScoreData data = (PDScoreData) res.getData();
                    iuartControlView.postUARTDataSuccess(data);
                } else {
                    iuartControlView.postUARTDataFail(res.getMsg());
                }
            }
        });
    }
}
