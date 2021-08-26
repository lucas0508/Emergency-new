package com.tjmedicine.emergency.ui.me.presenter;


import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.ui.me.model.LogOutModel;
import com.tjmedicine.emergency.ui.me.model.impl.ILogOutModel;
import com.tjmedicine.emergency.ui.me.view.ILogOutView;

public class LogoutPresenter extends BasePresenter {


    private ILogOutView iLogOutView;

    private ILogOutModel iLogOutModel = new LogOutModel();

    public LogoutPresenter(ILogOutView iLogOutView) {
        this.iLogOutView = iLogOutView;
    }

    public void logOut() {
        iLogOutModel.logout(res -> {
            if (HttpProvider.isSuccessful(res.getCode())) {
                iLogOutView.logoutSuccess();
            } else {
                iLogOutView.logoutFail(res.getMsg());
            }
        });
    }

}
