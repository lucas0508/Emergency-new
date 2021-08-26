package com.tjmedicine.emergency.common.bean;

public class MapDataBeen {

    /**
     * address : 内蒙古自治区呼和浩特市赛罕区金桥开发区电子商务产业园510
     * phone : 186****7229
     * lng : 120.8
     * lat : 37.42195
     * type : 1
     */

    private String address;
    private String phone;
    private double lng;
    private double lat;
    private int type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
