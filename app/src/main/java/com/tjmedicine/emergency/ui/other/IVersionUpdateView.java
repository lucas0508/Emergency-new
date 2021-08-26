package com.tjmedicine.emergency.ui.other;


import com.tjmedicine.emergency.common.bean.VersionUpdateEntity;

public interface IVersionUpdateView {

    /**
     * 版本更新成功
     */
    void updateVersionSuccess(VersionUpdateEntity versionUpdateEntity);

    /**
     * 版本更新失败
     */
    void updateVersionFail(String info);

}
