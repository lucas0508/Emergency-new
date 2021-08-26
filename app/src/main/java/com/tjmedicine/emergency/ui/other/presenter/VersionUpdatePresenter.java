package com.tjmedicine.emergency.ui.other.presenter;

import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.VersionUpdateEntity;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.other.IVersionUpdateView;
import com.tjmedicine.emergency.ui.other.model.IVersionUpdateModel;
import com.tjmedicine.emergency.ui.other.model.impl.VersionUpdateModel;

public class VersionUpdatePresenter {
    private IVersionUpdateModel mAboutMeActivityModel = new VersionUpdateModel();

    private IVersionUpdateView iVersionUpdateView;

    public VersionUpdatePresenter(IVersionUpdateView iVersionUpdateView) {
        this.iVersionUpdateView = iVersionUpdateView;
    }

    public void updateVersion() {
        mAboutMeActivityModel.updateVersion(new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    iVersionUpdateView.updateVersionSuccess((VersionUpdateEntity) res.getData());
                } else {
                    iVersionUpdateView.updateVersionFail(res.getMsg());
                }
            }
        });
    }
}
