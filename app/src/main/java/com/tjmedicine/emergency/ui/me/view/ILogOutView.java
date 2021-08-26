package com.tjmedicine.emergency.ui.me.view;

public interface ILogOutView {
    /**
     * 退出登录 - 成功
     */
    void logoutSuccess();

    /**
     * 退出登录 - 失败
     *
     * @param info
     */
    void logoutFail(String info);
}
