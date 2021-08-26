package com.tjmedicine.emergency.ui.uart.data.presenter;


import com.tjmedicine.emergency.common.bean.UserInfoEntity;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 *
 * 练习数据
 */

public interface IUARTControlView {

    /**
     * 成功
     */
    void postUARTDataSuccess(PDScoreData data);

    /**
     * 失败
     *
     * @param info
     */
    void postUARTDataFail(String info);

}
