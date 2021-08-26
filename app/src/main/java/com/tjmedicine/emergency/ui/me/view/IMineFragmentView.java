package com.tjmedicine.emergency.ui.me.view;


import com.tjmedicine.emergency.common.bean.UserInfoEntity;

public interface IMineFragmentView {

    void getUserInfoSuccess(UserInfoEntity userInfoEntity);

    void getUserInfoFail(String info, int code);

    void submitUserInfoSuccess(UserInfoEntity userInfoEntity);

    void submitUserInfoFail(String info);


}
