package com.tjmedicine.emergency.common.bean;

public class BannerBean {


    /**
     * id : 1
     * activityId : 1
     * httpUrl : 2222
     * imgUrl : 3333
     * createAt : 2020-12-31 14:15:34
     * sort : 1111
     * ifDel : 1
     */

    private int id;
    private int activityId;
    private String httpUrl;
    private String imgUrl;
    private String createAt;
    private int sort;
    private int ifDel;
    /**
     * 1.线下活动，2.直播，3.其他
     */
    private String type;

    private String playUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIfDel() {
        return ifDel;
    }

    public void setIfDel(int ifDel) {
        this.ifDel = ifDel;
    }
}
