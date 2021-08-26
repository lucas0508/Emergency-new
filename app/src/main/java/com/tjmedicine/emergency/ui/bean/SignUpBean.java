package com.tjmedicine.emergency.ui.bean;

public class SignUpBean {


    /**
     * uid : 2
     * createAt : 2020-12-31 14:33:53
     * activityId : 1
     * type : 1
     * title : 1
     * httpUrl : 12132
     * imgUrl : 12131
     * startTime : 2020-12-31 14:42:26
     * endTime : 2021-12-31 14:42:28
     * content : 11213
     */

    private int uid;
    private String createAt;
    private int activityId;
    private int type;
    private String title;
    private String httpUrl;
    private String imgUrl;
    private String startTime;
    private String endTime;
    private String content;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
