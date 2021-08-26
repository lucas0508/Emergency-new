package com.tjmedicine.emergency.ui.mine.health;

import com.tjmedicine.emergency.common.bean.HealthAllFileBeen;
import com.tjmedicine.emergency.common.bean.HealthFileBeen;
import com.tjmedicine.emergency.common.bean.HealthTagBeen;
import com.tjmedicine.emergency.ui.bean.ContactBean;

import java.util.List;

public interface IHealthView {


    void addHealthRecordsSuccess();

    void addHealthRecordsFail(String info);


    void delHealthRecordsSuccess();

    void delHealthRecordsFail(String info);


    void findHealthRecordsListSuccess(List<HealthFileBeen> healthFileBeens);

    void findHealthRecordsListFail(String info);


    void findHealthRecordsDetailSuccess(HealthAllFileBeen healthFileBeen);

    void findHealthRecordsDetailFail(String info);



    void findHealthRecordsTagSuccess(List<HealthTagBeen> healthTagBeens);

    void findHealthRecordsTagFail(String info);
}
