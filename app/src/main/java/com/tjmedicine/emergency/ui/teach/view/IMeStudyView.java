package com.tjmedicine.emergency.ui.teach.view;

import com.tjmedicine.emergency.common.bean.BannerBean;
import com.tjmedicine.emergency.common.bean.StudyBean;

import java.util.List;

public interface IMeStudyView {

    void queryMeStudySuccess(List<StudyBean.ListBean> studyBeans);

    void queryBannerFail(String info);

}
