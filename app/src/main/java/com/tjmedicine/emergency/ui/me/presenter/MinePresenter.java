package com.tjmedicine.emergency.ui.me.presenter;

import com.tjmedicine.emergency.common.base.BasePresenter;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.me.model.MineFragmentModel;
import com.tjmedicine.emergency.ui.me.model.impl.IMineFragmentModel;
import com.tjmedicine.emergency.ui.me.view.IMineFragmentView;

import java.util.Map;

public class MinePresenter extends BasePresenter {

    private IMineFragmentView iMineFragmentView;

    private IMineFragmentModel iMineFragmentModel = new MineFragmentModel();

    public MinePresenter(IMineFragmentView iMineFragmentView) {
        this.iMineFragmentView = iMineFragmentView;
    }

    public void queryUserInfo(){
        iMineFragmentModel.queryUserInfoData(new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())){
                    iMineFragmentView.getUserInfoSuccess((UserInfoEntity) res.getData());
                }else {
                    iMineFragmentView.getUserInfoFail(res.getMsg(),res.getCode());
                }
            }
        });
    }

    public void submitUserInfo(Map<String, Object> objectMap){

        iMineFragmentModel.submitUserInfoData(objectMap,new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())){
                    iMineFragmentView.submitUserInfoSuccess((UserInfoEntity) res.getData());
                }else {
                    iMineFragmentView.submitUserInfoFail(res.getMsg());
                }
            }
        });
    }
}
