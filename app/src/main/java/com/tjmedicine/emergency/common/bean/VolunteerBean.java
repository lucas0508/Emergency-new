package com.tjmedicine.emergency.common.bean;

public class VolunteerBean {


    /**
     * uid : null
     * phone : 17180106555
     * username : xrf
     * volunteerAddress : null
     * idUpUrl : https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1.gtimg.com%2Ffinance%2Fpics%2Fhv1%2F61%2F0%2F634%2F41225911.jpg&refer=http%3A%2F%2Fimg1.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611821938&t=fb0830a4b02e3b7480f5abfce706df4a
     * idDownUrl : https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2349422245,3515729816&fm=26&gp=0.jpg
     * bookUrl : null
     * lng : -122.08398
     * lat : 37.42195
     * createAt : 2020-12-29 16:26:38
     * updateAt : null
     * type : 1
     * userType : 2
     */

    private Object uid;
    private String phone;
    private String username;
    private Object volunteerAddress;
    private String idUpUrl;
    private String idDownUrl;
    private Object bookUrl;
    private double lng;
    private double lat;
    private String createAt;
    private Object updateAt;
    private int type;
    private int userType;
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getUid() {
        return uid;
    }

    public void setUid(Object uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getVolunteerAddress() {
        return volunteerAddress;
    }

    public void setVolunteerAddress(Object volunteerAddress) {
        this.volunteerAddress = volunteerAddress;
    }

    public String getIdUpUrl() {
        return idUpUrl;
    }

    public void setIdUpUrl(String idUpUrl) {
        this.idUpUrl = idUpUrl;
    }

    public String getIdDownUrl() {
        return idDownUrl;
    }

    public void setIdDownUrl(String idDownUrl) {
        this.idDownUrl = idDownUrl;
    }

    public Object getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(Object bookUrl) {
        this.bookUrl = bookUrl;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Object getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Object updateAt) {
        this.updateAt = updateAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
