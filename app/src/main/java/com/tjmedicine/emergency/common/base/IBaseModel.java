package com.tjmedicine.emergency.common.base;

import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;


public interface IBaseModel {

    /**
     * 回掉
     */
    interface OnCallbackListener{
        void callback(ResponseEntity res);
    }

    /**
     * 回掉
     */
    interface OnCallbackDataListener{
        void callback(ResponseDataEntity res);
    }
}
