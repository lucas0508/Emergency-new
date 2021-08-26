package com.tjmedicine.emergency.ui.map;

import com.amap.api.maps.model.LatLng;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private String mTitle;
    private int mapRole;
    private String address;

    public RegionItem(LatLng latLng, String title, int mapRole, String address) {
        mLatLng = latLng;
        mTitle = title;
        this.mapRole = mapRole;
        this.address = address;
    }

    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }

    @Override
    public int getMapRole() {
        return mapRole;
    }

    @Override
    public String getTitleName() {
        return mTitle;
    }

    @Override
    public String getAddress() {
        return address;
    }


}
