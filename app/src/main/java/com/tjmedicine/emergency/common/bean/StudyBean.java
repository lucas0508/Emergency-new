package com.tjmedicine.emergency.common.bean;

import java.io.Serializable;
import java.util.List;

public class StudyBean implements Serializable {


    /**
     * total : 3
     * list : [{"id":2,"title":"视频","type":1,"content":"<p>哈哈<\/p>","playUrl":"http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4","createAt":"2022-03-01 10:31:58","updateAt":"2022-03-01 14:22:48","completeStudy":1,"uid":371,"timeLength":null,"studyCourseProgress":null},{"id":3,"title":"测试图文","type":2,"content":"<p>图文<\/p>","playUrl":"","createAt":"2022-03-01 10:31:58","updateAt":"2022-03-01 10:31:58","completeStudy":1,"uid":371,"timeLength":null,"studyCourseProgress":null},{"id":4,"title":"测试图文","type":2,"content":"<p>测试图文<img src=\"http://qiniu.mengyuanyiliao.com/20220228144515/1634116265(1).jpg\" style=\"width: 409px;\"><\/p>","playUrl":"","createAt":"2022-03-01 10:31:58","updateAt":"2022-03-01 10:31:58","completeStudy":1,"uid":371,"timeLength":null,"studyCourseProgress":null}]
     * pageNum : 1
     * pageSize : 3
     * size : 3
     * startRow : 0
     * endRow : 2
     * pages : 1
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
     * navigateFirstPage : 1
     * navigateLastPage : 1
     * lastPage : 1
     * firstPage : 1
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
    private int lastPage;
    private int firstPage;
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

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
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

    public static class ListBean implements Serializable{
        /**
         * id : 2
         * title : 视频
         * type : 1
         * content : <p>哈哈</p>
         * playUrl : http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4
         * createAt : 2022-03-01 10:31:58
         * updateAt : 2022-03-01 14:22:48
         * completeStudy : 1
         * uid : 371
         * timeLength : null
         * studyCourseProgress : null
         */

        private int id;
        private String title;
        private int type;
        private String content;
        private String playUrl;
        private String createAt;
        private String updateAt;
        private int completeStudy;
        private int uid;
        private String timeLength;
        private int studyCourseProgress;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }

        public int getCompleteStudy() {
            return completeStudy;
        }

        public void setCompleteStudy(int completeStudy) {
            this.completeStudy = completeStudy;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getTimeLength() {
            return timeLength;
        }

        public void setTimeLength(String timeLength) {
            this.timeLength = timeLength;
        }

        public int getStudyCourseProgress() {
            return studyCourseProgress;
        }

        public void setStudyCourseProgress(int studyCourseProgress) {
            this.studyCourseProgress = studyCourseProgress;
        }
    }
}
