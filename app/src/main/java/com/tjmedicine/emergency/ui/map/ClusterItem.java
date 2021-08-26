package com.tjmedicine.emergency.ui.map;

import com.amap.api.maps.model.LatLng;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public interface ClusterItem {
    /**
     * 返回聚合元素的地理位置
     *
     * @return
     */
    LatLng getPosition();

    /**
     * 类型    设备  志愿者  转运车   医生
     */
    int getMapRole();

    /**
     * 志愿者名字
     * @return
     */
    String getTitleName();

    /**
     * 距离
     */
    String getAddress();
}
