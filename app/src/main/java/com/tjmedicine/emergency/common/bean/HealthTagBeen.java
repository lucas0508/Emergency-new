package com.tjmedicine.emergency.common.bean;

import java.util.List;

public class HealthTagBeen {


    /**
     * childs : [{"id":2,"value":"2"},{"id":5,"value":"5"},{"id":6,"value":"56"}]
     * id : 1
     * value : 父级1
     */

    private int id;
    private String value;
    private List<ChildsBean> childs;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
         * value : 2
         */

        private int id;
        private String value;
        private int  type;//标识这条Item属于布局一或是布局二

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
