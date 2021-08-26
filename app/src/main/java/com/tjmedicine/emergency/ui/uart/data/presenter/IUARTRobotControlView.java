package com.tjmedicine.emergency.ui.uart.data.presenter;


import com.tjmedicine.emergency.common.bean.UARTBean;

import java.util.List;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 *
 * 练习数据
 */

public interface IUARTRobotControlView {

    /**
     * 成功
     * @param uartBeans
     */
    void queryUARTDataSuccess(List<UARTBean.ListBean> uartBeans);

    /**
     * 失败
     *
     * @param info
     */
    void queryUARTDataFail(String info);

}
