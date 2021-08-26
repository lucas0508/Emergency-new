package com.tjmedicine.emergency.common.base;


import android.os.Handler;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.net.HeaderUtils;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.utils.GsonUtils;

import java.lang.reflect.Type;


public class BaseModel {
   
   
    private HeaderUtils mHeaderUtils;
    private Handler mHandler = new Handler();
    private ResponseEntity mException;
    private ResponseDataEntity mDataException;
    public static int DELAY_MILLIS = 500;

    private Type mType = new TypeToken<ResponseEntity<Object>>() {
    }.getType();

    private Type mTypeData = new TypeToken<ResponseDataEntity<Object>>() {
    }.getType();

    public HeaderUtils getHeaderUtils() {
        if (mHeaderUtils == null) mHeaderUtils = new HeaderUtils();
        return mHeaderUtils;
    }

    /**
     * 获取错误对象
     *
     * @return
     */
    public ResponseEntity getException() {
        if (mException == null) {
            mException = new ResponseEntity();
            mException.setCode(500);
            mException.setMsg("出错了，工程师正在抢修中，请稍后再试！");
        }
        mException.setTimestamp(System.currentTimeMillis());
        return mException;
    }

    /**
     * 获取错误对象
     *
     * @return
     */
    public ResponseDataEntity getDataException() {
        if (mDataException == null) {
            mDataException = new ResponseDataEntity();
            mDataException.setCode(500);
            mDataException.setMsg("出错了，工程师正在抢修中，请稍后再试！");
        }
        mDataException.setTimestamp(System.currentTimeMillis());
        return mDataException;
    }

    /**
     * 解析数据
     *
     * @param jsonData
     * @param type
     * @return
     */
    private ResponseEntity resolve(String jsonData, Type type) {
        ResponseEntity res;
        try {
            res = GsonUtils.parseJsonWithClass(jsonData, mType);
            if (HttpProvider.isSuccessful(res.getCode()) && type != null) {
                res = GsonUtils.parseJsonWithClass(jsonData, type);
            }
        } catch (Exception e) {
            return getException();
        }
        return res;
    }

    /**
     * 解析数据
     *  当data为List的时候使用
     * @param jsonData
     * @param type
     * @return
     */
    private ResponseDataEntity resolveData(String jsonData, Type type) {
        ResponseDataEntity res;
        try {
            res = GsonUtils.parseJsonWithClass(jsonData, mTypeData);
            if (HttpProvider.isSuccessful(res.getCode()) && type != null) {
                res = GsonUtils.parseJsonWithClass(jsonData, type);
            }
        } catch (Exception e) {
            return getDataException();
        }
        return res;
    }


    /**
     * 解析数据
     *
     * @param jsonData
     * @return
     */
    private ResponseEntity resolveEncrypt(String jsonData) {
        ResponseEntity res;
        try {
            res = GsonUtils.parseJsonWithClass(jsonData, mType);
            if (HttpProvider.isSuccessful(res.getCode()))
                res.setData(getHeaderUtils().checkSign(res.getData().toString()));
        } catch (Exception e) {
            return getException();
        }
        return res;
    }

    /**
     * 有延迟的执行回调
     *
     * @param jsonData
     * @param type
     * @param listener
     */
    public void delayedExecuteCallback(String jsonData, Type type, final IBaseModel.OnCallbackListener listener) {
        final ResponseEntity res = resolve(jsonData, type);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) listener.callback(res);
            }
        }, DELAY_MILLIS);
    }

    /**
     * 没有延迟的执行回调
     *
     * @param jsonData
     * @param type
     * @param listener
     */
    public void executeCallback(String jsonData, Type type, final IBaseModel.OnCallbackListener listener) {
        if (listener != null) listener.callback(resolve(jsonData, type));
    }

    /**
     * 没有延迟的执行回调
     *
     * @param jsonData
     * @param type
     * @param listener
     */
    public void executeDataCallback(String jsonData, Type type, final IBaseModel.OnCallbackDataListener listener) {
        if (listener != null) listener.callback(resolveData(jsonData, type));
    }


    /**
     * 有延迟的执行回调
     *
     * @param jsonData
     * @param listener
     */
    public void delayedEncryptExecuteCallback(String jsonData, final IBaseModel.OnCallbackListener listener) {
        final ResponseEntity res = resolveEncrypt(jsonData);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) listener.callback(res);
            }
        }, DELAY_MILLIS);
    }

    /**
     * 没有延迟的执行回调
     *
     * @param jsonData
     * @param listener
     */
    public void executeEncryptCallback(String jsonData, final IBaseModel.OnCallbackListener listener) {
        if (listener != null) listener.callback(resolveEncrypt(jsonData));
    }
}









