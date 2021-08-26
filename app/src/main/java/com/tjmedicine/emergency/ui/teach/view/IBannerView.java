package com.tjmedicine.emergency.ui.teach.view;

import com.tjmedicine.emergency.common.bean.BannerBean;

import java.util.List;

public interface IBannerView {

    void queryBannerSuccess(List<BannerBean> bannerBeanList);

    void queryBannerFail(String info);

}
