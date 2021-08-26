package com.tjmedicine.emergency.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.ui.uart.SettingActivity;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Json字符串转换类
 *
 * @author QiZai
 * @date 2018/4/11
 */

public class GsonUtils {
    private static Gson gson = new Gson();

    /**
     * 将json数据转换成对应的类型数据
     */
    public static <T> T parseJsonWithClass(String jsonData, Type type) {
        return gson.fromJson(jsonData, type);
    }

    /**
     * 将其他类型数据转换成json数据
     */
    public static String parseToJson(Object jsonData) {
        return gson.toJson(jsonData);
    }

    /**
     * 将json数据转换成list集合
     */
    public static <T> List<T> parseJsonArrayWithGson(String jsonData, Class<T> type) {
        return gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
    }


    public static String returnFormatText(String data) {
        String splitter = "<M>";
        String splitter1 = "</M>";
        String regex = "^" + splitter + "*|" + splitter1 + "*$";
        return data.replaceAll(regex, "");
    }

    /**
     * <PD=0,0,0,0,0,0|CD>  <PD=00,0,0,0,0|CD>   <PD=0,0,0,0,0,|CD>
     * <OK>
     * <Errno=errno|XY>
     * <Blow>
     * <Battery =bat|XY>
     * <FWVer=ver|XY>
     * <HWVer=ver|XY>
     *
     * @param data
     * @return
     */
    public static String returnFormatText2(String data) {
        String TAG = "TAG-RETURN";
        String str = "";
        if (TextUtils.isEmpty(data))
            return null;
        if (data.contains("Blow")) {
            String splitter = "<";
            String splitter1 = ">";
            String regex = "^" + splitter + "*|" + splitter1 + "*$";
            str = data.replaceAll(regex, "");
            return str;
        }
        if (data.startsWith("<") && data.endsWith(">") && data.contains("|")) {
            String splitter = "<";
            String splitter1 = ">";
            String regex = "^" + splitter + "*|" + splitter1 + "*$";

            str = data.replaceAll(regex, "");
            // String str = "PD=3,3,3,3,3,3|DF";
//            String str = "Blow";

            String[] split2 = str.split("\\|");
            String s = split2[0];
            String d = split2[1];
            if (getChecksum(s).toUpperCase().equals(d)) {
                if (s.startsWith("PD=")) {
                    String[] split = s.split("=");
                    if (split.length > 1) {
                        //PD=0,0,0,0,0,0
                        str = split[1];
                        System.out.println("returnFormatText2: split2=  dstr==" + str);
                    }
                }
                if (s.startsWith("Battery=")) {//电池电量 单位百分比
//                    String[] split = s.split("=");
//                    str = split[1];
                    str = s;
                    Log.e(TAG, "returnFormatText2: Battery=" + str);
                }
                if (s.startsWith("FWVer=")) {//软件版本号
//                    String[] split = s.split("=");
//                    str = split[1];
                    str = s;
                    Log.e(TAG, "returnFormatText2: FWVer=" + str);
                }
                if (s.startsWith("HWVer=")) {//硬件版本号
//                    String[] split = s.split("=");
//                    str = split[1];
                    str = s;
                    Log.e(TAG, "returnFormatText2: HWVer=" + str);
                }

                if (s.startsWith("Blow")) {
                    str = s;
                    Log.e(TAG, "returnFormatText2: Blow=" + str);
                }
                if (s.startsWith("Errno=")) {
                    Log.e(TAG, "returnFormatText2: Errno=" + str);
                }
                if (s.startsWith("OK=")) {
                    Log.e(TAG, "returnFormatText2: OK=" + str);
                }
                return str;
            } else {
                str = "";
            }
        }
        return str;
    }


    public static String getChecksum(String str) {
        int nChecksum = 0;
        for (int i = 0; i < str.length(); i++) {
            nChecksum += str.charAt(i);
        }
        nChecksum = nChecksum & 0xFF;
        String strChecksum = Integer.toHexString(nChecksum);
        if (nChecksum < 16)
            strChecksum = "0" + Integer.toHexString(nChecksum);

        Log.e("校验--", "getChecksum=" + str);
        return strChecksum;
    }


//    手机->设备	Start
//    设备->手机	OK
//    Failed
//2.2停止测试
//    手机->设备	Stop
//    设备->手机	OK
//    Failed
//2.3上报按压深度
//    设备->手机	PD=XXX，单位mm
//2.4上报一次吹气
//    设备->手机	Blow

    public static String returnStatus(String data) {
        //手机->设备	Start
        if ("Blow".equals(data)) {

            return "吹起成功：" + data;
        }
        if ("PD".startsWith(data)) {
            String[] split = data.split("=");

            return "--按压深度：" + split[1] + "mm";
        }
        return "";
    }
}