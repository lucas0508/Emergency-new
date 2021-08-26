package com.tjmedicine.emergency.common.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/12
 */

public class BasePresenter {
    protected String PAY_ERROR_MSG = "网络连接较差，请稍后再试！";

    protected Map<String, Object> mFormData;

    protected Map<String, Object> getEmptyFormData() {
        if (mFormData == null) mFormData = new HashMap<>();
        mFormData.clear();
        return mFormData;
    }

    /**
     * list 转换为字符串
     * @param list
     * @param separator
     * @return
     */
    protected String listToString(List list, char separator) {
        if (list != null && list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i)).append(separator);
            }
            return sb.toString().substring(0, sb.toString().length() - 1);
        }
        return null;
    }
}
