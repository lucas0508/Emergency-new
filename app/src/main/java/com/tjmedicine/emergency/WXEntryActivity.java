package com.tjmedicine.emergency;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tjmedicine.emergency.common.base.BaseActivity;

import me.jessyan.autosize.utils.LogUtils;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    @Override
    public void onReq(BaseReq resp) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData =launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
        }
    }

    @Override
    protected int setLayoutResourceID() {
        return 0;
    }

    @Override
    protected void initView() {

    }
}








