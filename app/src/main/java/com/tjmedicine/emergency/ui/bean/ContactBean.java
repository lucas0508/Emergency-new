package com.tjmedicine.emergency.ui.bean;

import java.io.Serializable;

public class ContactBean implements Serializable {


    /**
     * id : 1
     * username : 张非
     * phone : 13876541235
     * relation : yeye
     * createAt : 2020-12-28 14:49:17
     * ifDel : 1
     * uid : 2
     */

    private int id;
    private String username;
    private String phone;
    private String relation;
    private String createAt;
    private int ifDel;
    private int uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getIfDel() {
        return ifDel;
    }

    public void setIfDel(int ifDel) {
        this.ifDel = ifDel;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
