package com.tjmedicine.emergency.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthAllFileBeen {


    /**
     * birthday : 1995-10-23
     * id : 1
     * age : 25岁
     * username : 1213
     */

    private String birthday;
    private int id;
    private String age;
    private String username;
    /**
     * bloodType : 1
     * height : 172.0
     * weight : 80.5
     * phone : 18698437229
     * idNum : 150241199510230116
     * uid : 3
     * tags : [{"id":1,"value":"发反馈反馈反馈","childs":[{"id":2,"value":"测试测试的等待","childs":null}]},{"id":3,"value":"大三大四的撒的撒的撒","childs":[{"id":4,"value":"心脏病","childs":null}]}]
     * list : null
     */

    private String bloodType;
    private double height;
    private double weight;
    private String phone;
    private String idNum;
    private int uid;
    private Object list;
    private List<TagsBean> tags;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        /**
         * id : 1
         * value : 发反馈反馈反馈
         * childs : [{"id":2,"value":"测试测试的等待","childs":null}]
         */

        @SerializedName("id")
        private int idX;
        private String value;
        private List<ChildsBean> childs;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<ChildsBean> getChilds() {
            return childs;
        }

        public void setChilds(List<ChildsBean> childs) {
            this.childs = childs;
        }

        public static class ChildsBean {
            /**
             * id : 2
             * value : 测试测试的等待
             * childs : null
             */

            private int id;
            private String value;
            private Object childs;

            public int getId() {
                return id;
            }

            public void setIdX(int id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public Object getChilds() {
                return childs;
            }

            public void setChilds(Object childs) {
                this.childs = childs;
            }
        }
    }
}
