package com.tjmedicine.emergency.common.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.utils.DevicePermissionsUtils;
import com.tjmedicine.emergency.utils.GsonUtils;
import com.tjmedicine.emergency.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 */

public class HttpProvider {


    private static OkHttpClient mClient;
    private static Handler mHandler;
    private static Map<String, Object> mHeaderData;

    /**
     * MICROSECONDS     微秒      一百万分之一秒（就是毫秒/1000）
     * MILLISECONDS     毫秒      千分之一秒
     * NANOSECONDS      毫微秒    十亿分之一秒（就是微秒/1000）
     * SECONDS          秒
     * MINUTES          分钟
     * HOURS            小时
     * DAYS             天
     */
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private static final int TIME_OUT = 5;

    private static Map<String, Object> mRequestBody;

    private static Type mType = new TypeToken<ResponseEntity<Object>>() {
    }.getType();

    private HttpProvider() {
    }

    public static OkHttpClient getOkHttpClient() {
        if (mClient == null) {
            synchronized (HttpProvider.class) {
                if (mClient == null) {
                    HttpsUtils httpsUtils = new HttpsUtils();
                    mClient = httpsUtils.getTrusClient();
                    mClient.newBuilder().connectTimeout(TIME_OUT, TIME_OUT_UNIT)
                            .readTimeout(TIME_OUT, TIME_OUT_UNIT)
                            .writeTimeout(TIME_OUT, TIME_OUT_UNIT)
                    ;
                    mHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return mClient;
    }

    /**
     * 设置请求头
     * phoneModel       --      手机型号;
     * phoneSystem      --      手机系统 (ios，android，weixin)
     * phoneVersion     --      系统版本号
     * largeVersion    --      app大版本号
     * minorVersion    --      app小版本号
     *
     * @return
     */
    private static Headers setHeaders() {
        String authorization = UserInfo.getToken(); // UserInfo.getToken();
        Headers.Builder headers = new Headers.Builder();//DevicePermissionsUtils.getUniqueId()
        headers.set("phoneModel", DevicePermissionsUtils.getPhoneModel());
        headers.set("deviceId", DevicePermissionsUtils.getUniqueId());
        headers.set("phoneSystem", DevicePermissionsUtils.getPhoneSystem());
        headers.set("phoneVersion", DevicePermissionsUtils.getSystemVersion());
        headers.set("largeVersion", DevicePermissionsUtils.getAppMaxVersion());
        headers.set("minorVersion", DevicePermissionsUtils.getAppMinVersion());
        if (!TextUtils.isEmpty(authorization)) {
            headers.set("Authorization", authorization);//dfYAwSKczd1h1hIsiReNB9TE6Al+D2TOwL6oFCKhxLX1bSMoMNyblwxrts8uthImGkJvl5Tk17gaFWcIw39OBa==
        }
        if (mHeaderData != null)
            for (String k : mHeaderData.keySet()) {
                headers.set(k, mHeaderData.get(k).toString());
            }
        if (mHeaderData != null) mHeaderData.clear();
        return headers.build();
    }

    /**
     * 设置请求头
     *
     * @return
     */
    public static Map<String, Object> getHeader() {
        if (mHeaderData == null) mHeaderData = new HashMap<>();
        mHeaderData.clear();
        return mHeaderData;
    }

    /**
     * 获取请求
     *
     * @param type { 1:post, 2:put, 3:delete, other:get }
     * @param url
     * @param body
     * @return
     */
    private static Request getRequest(int type, String url, RequestBody body) {
        Headers headers = setHeaders();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .headers(headers);
        switch (type) {
            case 0: // get
                break;
            case 1: // post
                builder.post(body);
                break;
            case 2: // put
                builder.put(body);
                break;
            case 3: // delete
                builder.delete(body);
                break;
        }

        return builder.build();
    }

    /**
     * 设置POST请求参数
     *
     * @param data
     * @return
     */
    private static FormBody setRequestBody(Map<String, Object> data) {
        FormBody.Builder builder = new FormBody.Builder();
        if (data != null) {
            for (String k : data.keySet()) {
                Object d = data.get(k);
                builder.add(k, d != null ? d.toString() : "");
            }
        }

        return builder.build();
    }

    /**
     * GET参数拼接
     *
     * @param data
     * @return
     */
    public static String getUrlDataByMap(Map<String, Object> data) {
        if (data != null) {
            String urlData = "?";
            for (String k : data.keySet()) {
                urlData += k + "=" + data.get(k) + "&";
            }
            return urlData.substring(0, urlData.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * POST请求
     *
     * @param url
     * @param callback
     */
    public static void doPost(final String url, final Map<String, Object> data, final ResponseCallback callback) {
        mRequestBody = data;
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                GsonUtils.parseToJson(data));
        execute(getRequest(1, url, body), callback);
    }


    /**
     * GET请求
     *
     * @param url
     * @param callback
     */
    public static void doGet(final String url, final ResponseCallback callback) {
        mRequestBody = null;
        execute(getRequest(0, url, null), callback);
    }

    /**
     * json请求
     *
     * @param url
     * @param data
     * @param callback
     */
    public static void doJson(final String url, final Object data, final ResponseCallback callback) {
//        mRequestBody = GsonUtils.parseToJson(data);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                GsonUtils.parseToJson(data));
        execute(getRequest(1, url, body), callback);
    }

    /**
     * file请求
     *
     * @param url
     * @param callback
     */
    public static void doFile(final String url, final List<String> mImgUrls, final ResponseCallback callback) {
        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        int j = 0;
        for (int i = 0; i < mImgUrls.size(); i++) {
            File f = new File(mImgUrls.get(i));
            if (f != null) {
                builder.addFormDataPart("jiaogegongren", f.getName(), RequestBody.create(MediaType.parse("image/jpg"), f));
            }
        }
        mRequestBody = null;
        builder.addFormDataPart("imagetype", "multipart/form-data").build();
        MultipartBody requestBody = builder.build();
        execute(getRequest(1, url, requestBody), callback);
    }

    /**
     * put请求
     *
     * @param url
     * @param data
     * @param callback
     */
    public static void doPut(final String url, final Map<String, Object> data, final ResponseCallback callback) {
        mRequestBody = data;
        String jsonData = GsonUtils.parseToJson(data);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonData);
        execute(getRequest(2, url, body), callback);
    }

    /**
     * delete 请求
     *
     * @param url
     * @param callback
     */
    public static void doDelete(final String url, final Map<String, Object> data, final ResponseCallback callback) {
        mRequestBody = data;
        String jsonData = GsonUtils.parseToJson(data);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonData);
        execute(getRequest(3, url, body), callback);
    }

    /**
     * 执行请求
     *
     * @param request  请求
     * @param callback 回掉
     */
    private static void execute(final Request request, final ResponseCallback callback) {
        Log.e("REQ【" + request.method().toUpperCase() + "】",
                " URL：" + request.url() +
                        "\nHEAD：" + request.headers().toString().replaceAll("\\s", " ") +
                        "\nBODY：" + mRequestBody +
                        "\n "
        );
        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onHttpCallback(request, null, callback);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String authorization = response.header("authorization");
//                    if (!TextUtils.isEmpty(authorization)) {
//                    PreferenceUtil.put(Authorization, authorization);
//                    Logger.d(authorization);
//                    }
                    // TODO: 2020/4/14 注释 token
                    onHttpCallback(request, response.body().string(), callback);
                } else {
                    onHttpCallback(request, null, callback);
                }
            }
        });
    }

    /**
     * http回掉函数，做响应的基本处理
     *
     * @param responseText 响应文本
     * @param callback     回掉
     */
    public static void onHttpCallback(Request request, final String responseText, final ResponseCallback callback) {
        final String resText;
        Logger.d("Http result " + responseText);
        if (responseText != null) {
            resText = responseText;
        } else {
            resText = "{\"code\":\"503\",\"msg\":\"服务器异常,请联系管理员！\",\"timestamp\":" +
                    System.currentTimeMillis() + "}";
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                checkResponse(resText, callback);
            }
        });

        Log.e("RES【" + request.method().toUpperCase() + "】",
                " URL: " + request.url() +
                        "\nBODY: " + mRequestBody +
                        "\n RES: " + resText +
                        "\n Header:" + request.header("authorization")
        );
    }

    /**
     * 判断响应是否成功
     *
     * @param code
     * @return
     */
    public static boolean isSuccessful(int code) {
        return code == 200;
    }

    /**
     * 解析数据
     *
     * @param resText
     * @return
     */
    private static void checkResponse(String resText, ResponseCallback callback) {
        ResponseEntity res = GsonUtils.parseJsonWithClass(resText, mType);
        if (res != null) {
            if (res.getCode() != null && res.getCode() == 401) {
                UserInfo.removeUserInfo();
                //   ToastUtils.showTextToas(MyApplication.getContext(),"登录已失效,请重新登录");
            }
            if (callback != null) callback.callback(resText);
        } else {
            ToastUtils.showTextToas(EmergencyApplication.getContext(), "出错了，工程师正在抢修中，请稍后再试！");
        }
    }

    /**
     * 响应回掉接口
     */
    public interface ResponseCallback {

        /**
         * 回掉方法
         *
         * @param responseText
         */
        void callback(String responseText);
    }

}
