package com.tjmedicine.emergency.utils;


import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 验证类
 *
 * @author QiZai
 * @date 2018/5/2 10:07
 */

public class ValidateUtils {

    // 校验手机是否合规 2020年最全的国内手机号格式
    private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";


    /**
     * 手机号验证
     *
     * @param phone
     * @return
     */
    public static Boolean validatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, phone);
    }

    /**
     * 邮箱验证
     *
     * @param email
     * @return
     */
    public static Boolean validateEmail(String email) {
        return email != null && Pattern.compile("^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$")
                .matcher(email).matches();
    }


    /**
     * 密码验证
     *
     * @param password
     * @return
     */
    public static Boolean validatePassword(String password) {
        return password != null && Pattern.compile("(?![0-9]+)([A-Za-z0-9]{8,16})").matcher(password).matches();
    }


    /**
     * 支付密码验证
     *
     * @param password
     * @return
     */
    public static Boolean validatePaymentPassword(String password) {
        return password != null && Pattern.compile("^\\d{6}$").matcher(password).matches();
    }


    /**
     * 验证码验证
     *
     * @param code
     * @return
     */
    public static Boolean validateCode(String code) {
        return code != null && Pattern.compile("^\\d{6}$").matcher(code).matches();
    }


    /**
     * 验证银行卡
     *
     * @param bankCard
     * @return
     */
    public static Boolean validateBankCard(String bankCard) {
        return bankCard != null && Pattern.compile("^([1-9]{1})(\\d{14,18})$").matcher(bankCard).matches();
    }


    /**
     * 验证身份证
     *
     * @param IDCard
     * @return
     */
    public static Boolean validateIDCard(String IDCard) {
        return IDCard != null && Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$").matcher(IDCard).matches();

    }


}
