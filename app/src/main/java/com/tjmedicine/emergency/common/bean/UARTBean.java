package com.tjmedicine.emergency.common.bean;

import java.util.List;

public class UARTBean {


    /**
     * total : 14
     * list : [{"id":"13b298fb-76eb-4914-931c-aa95022fb817","score":20,"pressTime":5,"breatheTime":1,"strengthAverage":24.2,"createAt":"2020-12-28 12:00:06","uid":2,"type":1},{"id":"504f32a1-219e-4deb-bb8e-86476b324520","score":null,"pressTime":4,"breatheTime":1,"strengthAverage":13.5,"createAt":"2020-12-28 12:17:12","uid":2,"type":2},{"id":"5a8df99d-3f23-4aab-86eb-4f4aa5e2203e","score":20,"pressTime":5,"breatheTime":1,"strengthAverage":24.2,"createAt":"2020-12-24 00:00:00","uid":2,"type":1},{"id":"622f7fdf-32d6-4a6d-a155-b96c432290a1","score":20,"pressTime":5,"breatheTime":1,"strengthAverage":24.2,"createAt":"2020-12-24 00:00:00","uid":2,"type":1},{"id":"67dd8af4-2202-41ba-b383-349c46055415","score":20,"pressTime":5,"breatheTime":1,"strengthAverage":24.2,"createAt":"2020-12-28 12:15:50","uid":2,"type":1},{"id":"6ded2af4-ca9e-431e-a4f5-76821dceccca","score":null,"pressTime":5,"breatheTime":1,"strengthAverage":24.2,"createAt":"2020-12-24 00:00:00","uid":2,"type":2},{"id":"7ff1a433-be73-4055-a035-5b4e4ef6b8a9","score":20,"pressTime":5,"breatheTime":1,"strengthAverage":1,"createAt":"2020-12-24 00:00:00","uid":2,"type":1},{"id":"8246c082-c4e0-406d-be86-016b03bc65a0","score":20,"pressTime":3,"breatheTime":1,"strengthAverage":1,"createAt":"2020-12-24 00:00:00","uid":2,"type":1},{"id":"868bd3db-9779-434a-890e-579e0d43edab","score":null,"pressTime":5,"breatheTime":1,"strengthAverage":24.2,"createAt":"2020-12-24 00:00:00","uid":2,"type":2},{"id":"9a63e80b-c48e-4ab4-96ec-559a20a8d36d","score":20,"pressTime":5,"breatheTime":1,"strengthAverage":1,"createAt":"2020-12-24 00:00:00","uid":2,"type":1}]
     * pageNum : 1
     * pageSize : 10
     * size : 10
     * startRow : 1
     * endRow : 10
     * pages : 2
     * prePage : 0
     * nextPage : 2
     * isFirstPage : true
     * isLastPage : false
     * hasPreviousPage : false
     * hasNextPage : true
     * navigatePages : 8
     * navigatepageNums : [1,2]
     * navigateFirstPage : 1
     * navigateLastPage : 2
     * firstPage : 1
     * lastPage : 2
     */

    private int total;
    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int firstPage;
    private int lastPage;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * id : 13b298fb-76eb-4914-931c-aa95022fb817
         * score : 20
         * pressTime : 5
         * breatheTime : 1
         * strengthAverage : 24.2
         * createAt : 2020-12-28 12:00:06
         * uid : 2
         * type : 1
         */

        private String id;
        private int score;
        private int pressTime;
        private int breatheTime;
        private double strengthAverage;
        private String createAt;
        private int uid;
        private int type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getPressTime() {
            return pressTime;
        }

        public void setPressTime(int pressTime) {
            this.pressTime = pressTime;
        }

        public int getBreatheTime() {
            return breatheTime;
        }

        public void setBreatheTime(int breatheTime) {
            this.breatheTime = breatheTime;
        }

        public double getStrengthAverage() {
            return strengthAverage;
        }

        public void setStrengthAverage(double strengthAverage) {
            this.strengthAverage = strengthAverage;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
