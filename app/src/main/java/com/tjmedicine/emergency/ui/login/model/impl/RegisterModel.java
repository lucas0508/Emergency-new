package com.tjmedicine.emergency.ui.login.model.impl;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.login.model.IRegisterModel;
import com.tjmedicine.emergency.utils.GsonUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/28 13:25
 */

public class RegisterModel extends BaseModel implements IRegisterModel {

    @Override
    public void sendWXValidateCode(String phone, String type, final IBaseModel.OnCallbackListener listener) {
        String URL = GlobalConstants.APP_LOAD_CAPTCHA + "?phone=" + phone + "&type=" + type;

        HttpProvider.doGet(URL, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, null, listener);
            }
        });
    }

    @Override
    public void fromWXRegister(Map<String, Object> data, String token, final IBaseModel.OnCallbackListener listener) {
//        String URL = GlobalConstants.APP_BIND_PHONE;
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                GsonUtils.parseToJson(data));
        final Request request = new Request.Builder()
                .url("URL")
                .header("Authorization", token)
                .post(body)//默认就是GET请求，可以不写
                .build();
        HttpProvider.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    delayedExecuteCallback(response.body().string(), new TypeToken<ResponseEntity<UserInfoEntity>>() {
                    }.getType(), listener);
                } else {
                    delayedExecuteCallback(null, new TypeToken<ResponseEntity<UserInfoEntity>>() {
                    }.getType(), listener);
                }
            }
        });

     /*   HttpProvider.doPost(URL, data, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                delayedExecuteCallback(responseText, new TypeToken<ResponseEntity<UserInfoEntity>>() {
                }.getType(), listener);
            }
        });*/
    }
}
