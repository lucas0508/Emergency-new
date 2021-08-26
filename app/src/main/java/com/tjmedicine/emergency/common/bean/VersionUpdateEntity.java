package com.tjmedicine.emergency.common.bean;

public class VersionUpdateEntity {


    /**
     * id : 1
     * version : 0.0.1
     * createdAt : 2021-01-04 11:12:28
     * url : htts://www.baidu.com
     */

    private int id;
    private String version;
    private String createdAt;
    private String url;
    private int ifUp;
    private String content;

    public int getIfUp() {
        return ifUp;
    }

    public void setIfUp(int ifUp) {
        this.ifUp = ifUp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
