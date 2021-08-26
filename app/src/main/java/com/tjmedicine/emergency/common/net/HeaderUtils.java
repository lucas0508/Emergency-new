package com.tjmedicine.emergency.common.net;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author QiZai
 * @desc
 * @date 2018/7/27 14:09
 */

public class HeaderUtils {

    private String key;

    /**
     * 随机字符串
     *
     * @return
     */
    public String CreateNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 32; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * HashMap转SortedMap
     *
     * @param data
     * @return
     */
    public SortedMap<Object, Object> hashMapToSortedMap(Map<String, Object> data) {
        if (data != null) {
            SortedMap<Object, Object> sortedMap = new TreeMap<>();
            for (String k : data.keySet()) {
                sortedMap.put(k, data.get(k).toString());
            }
            return sortedMap;
        }
        return null;
    }


    /**
     * 微信支付签名算法sign
     *
     * @param key
     * @param parameters
     * @return
     */
    public String createSign(String key, Map<String, Object> parameters) {
        hashMapToSortedMap(parameters);
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = string2MD5(sb.toString()).toUpperCase();
        return sign;
    }

    /**
     * MD5加码 生成32位md5码
     *
     * @param str
     * @return
     */
    public String string2MD5(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取sign
     *
     * @param value
     * @return
     */
    public String getSecret(Map<String, Object> value) {
        key = CreateNoncestr();
        String sign = createSign(key, value);
//        String privateKey = SecurityUtil.encryptRSAPublic(key);
        String result = null;
//        try {
//            result = "value=" + URLEncoder.encode(value.toString(), "UTF-8")
//                    + "&sign=" + URLEncoder.encode(sign, "UTF-8")
//                    + "&privateKey=" + URLEncoder.encode(privateKey, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    /**
     * 校验sign
     *
     * @param signObj
     * @return
     */
    public Map<String, String> checkSign(Object signObj) {
        try {
            if (signObj == null)
                return null;
            String sign = signObj.toString();
            String[] split = sign.split("&");
            String str = split[0] + "&key=" + key;
            String sss = string2MD5(str);
            split[1].split("=")[1].equals(sss);
            if (split[1].split("=")[1].equals(sss)) {
                Map<String, String> resultData = getResultData(split[0]);
                if (resultData.size() > 0) return resultData;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 获取结果集
     *
     * @param str
     * @return
     */
    private Map<String, String> getResultData(String str) {
        Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
        Matcher matcher = pattern.matcher(str);
        Map<String, String> data = new HashMap<>();
        while (matcher.find()) {
            String[] split = matcher.group().replaceAll("[\\s|\\']*", "").split(",");
            for (String string : split) {
                String[] split2 = string.split("=");
                if (split2.length == 2) {
                    data.put(split2[0], split2[1].equals("null") ? null : split2[1]);
                }
            }
        }
        return data;
    }

}
