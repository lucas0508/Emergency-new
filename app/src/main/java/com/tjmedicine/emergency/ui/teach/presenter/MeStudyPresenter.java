package com.tjmedicine.emergency.ui.teach.presenter;

import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.StudyBean;
import com.tjmedicine.emergency.common.bean.TeachingBean;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.teach.model.IMeStudyModel;
import com.tjmedicine.emergency.ui.teach.model.ITeachingModel;
import com.tjmedicine.emergency.ui.teach.model.impl.MeStudyModel;
import com.tjmedicine.emergency.ui.teach.model.impl.TeachingModel;
import com.tjmedicine.emergency.ui.teach.view.IMeStudyView;
import com.tjmedicine.emergency.ui.teach.view.ITeachingView;

import java.util.List;
import java.util.Map;

public class MeStudyPresenter {


    private IMeStudyView iMeStudyView;

    private IMeStudyModel iMeStudyModel = new MeStudyModel();

    public MeStudyPresenter(IMeStudyView iMeStudyView) {
        this.iMeStudyView = iMeStudyView;
    }


    public void queryMeStudyList(Map<String, Object> map) {
        iMeStudyModel.queryMeStudyData(map,new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    StudyBean studyBean = (StudyBean) res.getData();
                    iMeStudyView.queryMeStudySuccess(studyBean.getList());
                } else {
                    iMeStudyView.queryBannerFail(res.getMsg());
                }
            }
        });
    }
}
