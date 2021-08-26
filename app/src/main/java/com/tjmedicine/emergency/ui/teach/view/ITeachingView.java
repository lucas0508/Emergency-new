package com.tjmedicine.emergency.ui.teach.view;

import com.tjmedicine.emergency.common.bean.BannerBean;
import com.tjmedicine.emergency.common.bean.HealthAllFileBeen;
import com.tjmedicine.emergency.common.bean.HealthFileBeen;
import com.tjmedicine.emergency.common.bean.TeachingBean;

import java.util.List;

public interface ITeachingView {


    void findTeachingSuccess(List<TeachingBean.ListBean> listBeans);


    void findTeachingFail(String info);


    void findTeachingDetailSuccess();


    void findTeachingDetailFail(String info);


}
