package com.tjmedicine.emergency.common.bean;


import android.text.TextUtils;

import com.tjmedicine.emergency.common.net.ResponseEntity;

public class UserInfoEntity {
    /**
     * phone : 17180106555
     * code : 888888
     * token : eyJUeXBlIjoiSnd0IiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE2MDg3OTYzMjAsInVzZXJJZCI6M30.jf5nsdB7LXRiRzBRgoCiQMgLHh-B4LHDcOTcM7CGuU8
     * uid : 3
     * nickname : 用户17180106555
     * avatar : null
     * personSign : null
     */

    private String phone;
    private int code;
    private String token;
    private int uid;
    private String nickname;
    private String avatar;
    private String personSign;
    private String sex;
    private String birthday;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        if (TextUtils.isEmpty(sex)) {
            return sex = "保密";
        }
        if (sex.equals("1")) {
            sex = "男";
        } else if (sex.equals("2")) {
            sex = "女";
        } else {
            sex = "保密";
        }
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCodes() {
        return code;
    }

    public void setCodes(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPersonSign() {
        return personSign;
    }

    public void setPersonSign(String personSign) {
        this.personSign = personSign;
    }

    /**
     * code : 200
     * msg : 成功.
     * data : {"phone":"17180106555","code":888888,"token":"eyJUeXBlIjoiSnd0IiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE2MDg3OTYzMjAsInVzZXJJZCI6M30.jf5nsdB7LXRiRzBRgoCiQMgLHh-B4LHDcOTcM7CGuU8","uid":3,"nickname":"用户17180106555","avatar":null,"personSign":null}
     */


}
