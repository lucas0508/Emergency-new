package com.tjmedicine.emergency.ui.device.ecg;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ceshiBean implements Serializable {

    @SerializedName("total")
    private Integer total;
    @SerializedName("list")
    public List<ListBean> list;
    @SerializedName("pageNum")
    private Integer pageNum;
    @SerializedName("pageSize")
    private Integer pageSize;
    @SerializedName("size")
    private Integer size;
    @SerializedName("startRow")
    private Integer startRow;
    @SerializedName("endRow")
    private Integer endRow;
    @SerializedName("pages")
    private Integer pages;
    @SerializedName("prePage")
    private Integer prePage;
    @SerializedName("nextPage")
    private Integer nextPage;
    @SerializedName("isFirstPage")
    private Boolean isFirstPage;
    @SerializedName("isLastPage")
    private Boolean isLastPage;
    @SerializedName("hasPreviousPage")
    private Boolean hasPreviousPage;
    @SerializedName("hasNextPage")
    private Boolean hasNextPage;
    @SerializedName("navigatePages")
    private Integer navigatePages;
    @SerializedName("navigatepageNums")
    private List<Integer> navigatepageNums;
    @SerializedName("navigateFirstPage")
    private Integer navigateFirstPage;
    @SerializedName("navigateLastPage")
    private Integer navigateLastPage;
    @SerializedName("firstPage")
    private Integer firstPage;
    @SerializedName("lastPage")
    private Integer lastPage;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Boolean getIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(Boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public Boolean getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public Integer getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(Integer navigatePages) {
        this.navigatePages = navigatePages;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public Integer getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(Integer navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public Integer getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(Integer navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public Integer getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public static class ListBean implements Serializable{


        @SerializedName("uuid")
        private String uuid;
        @SerializedName("id")
        private Integer id;
        @SerializedName("abnorAnalysis")
        private Object abnorAnalysis;
        @SerializedName("avg")
        private String avg;
        @SerializedName("ecgResult")
        private Object ecgResult;
        @SerializedName("ecgResultTz")
        private String ecgResultTz;
        @SerializedName("ecgUrl")
        private String ecgUrl;
        @SerializedName("fatigue")
        private Object fatigue;
        @SerializedName("fatigueStatus")
        private Object fatigueStatus;
        @SerializedName("fatigueValue")
        private Object fatigueValue;
        @SerializedName("fileImagePath")
        private String fileImagePath;
        @SerializedName("filePath")
        private String filePath;
        @SerializedName("hdrisk")
        private Object hdrisk;
        @SerializedName("hdriskStatus")
        private Object hdriskStatus;
        @SerializedName("hdriskValue")
        private Object hdriskValue;
        @SerializedName("healthCareAdvice")
        private Object healthCareAdvice;
        @SerializedName("heartRate")
        private Integer heartRate;
        @SerializedName("isNormal")
        String isNormal ;// 是否是正常心电 0-是 1-否 number

        @SerializedName("heartbeatRate")
        private Integer heartbeatRate;
        @SerializedName("hrv")
        private Object hrv;
        @SerializedName("hrvStatus")
        private Object hrvStatus;
        @SerializedName("hrvValue")
        private Object hrvValue;
        @SerializedName("ifPayment")
        private Integer ifPayment;
        @SerializedName("interpretationReportId")
        private Object interpretationReportId;
        @SerializedName("knowledge")
        private Object knowledge;
        @SerializedName("length")
        private String length;
        @SerializedName("max")
        private Integer max;
        @SerializedName("mentalPressure")
        private Object mentalPressure;
        @SerializedName("mentalStatus")
        private Object mentalStatus;
        @SerializedName("mentalValue")
        private Object mentalValue;
        @SerializedName("min")
        private Integer min;
        @SerializedName("normalRate")
        private Integer normalRate;
        @SerializedName("slowRate")
        private Integer slowRate;
        @SerializedName("state")
        private String state;
        @SerializedName("suggestion")
        private Object suggestion;
        @SerializedName("takeTime")
        private String takeTime;

        @SerializedName("title")
        private String title;
        @SerializedName("uid")
        private Integer uid;
        @SerializedName("extList")
        private Object extList;
        @SerializedName("normalList")
        private Object normalList;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Object getAbnorAnalysis() {
            return abnorAnalysis;
        }

        public void setAbnorAnalysis(Object abnorAnalysis) {
            this.abnorAnalysis = abnorAnalysis;
        }

        public String getAvg() {
            return avg;
        }

        public String getIsNormal() {
            return isNormal;
        }

        public void setIsNormal(String isNormal) {
            this.isNormal = isNormal;
        }

        public void setAvg(String avg) {
            this.avg = avg;
        }

        public Object getEcgResult() {
            return ecgResult;
        }

        public void setEcgResult(Object ecgResult) {
            this.ecgResult = ecgResult;
        }

        public String getEcgResultTz() {
            return ecgResultTz;
        }

        public void setEcgResultTz(String ecgResultTz) {
            this.ecgResultTz = ecgResultTz;
        }

        public String getEcgUrl() {
            return ecgUrl;
        }

        public void setEcgUrl(String ecgUrl) {
            this.ecgUrl = ecgUrl;
        }

        public Object getFatigue() {
            return fatigue;
        }

        public void setFatigue(Object fatigue) {
            this.fatigue = fatigue;
        }

        public Object getFatigueStatus() {
            return fatigueStatus;
        }

        public void setFatigueStatus(Object fatigueStatus) {
            this.fatigueStatus = fatigueStatus;
        }

        public Object getFatigueValue() {
            return fatigueValue;
        }

        public void setFatigueValue(Object fatigueValue) {
            this.fatigueValue = fatigueValue;
        }

        public String getFileImagePath() {
            return fileImagePath;
        }

        public void setFileImagePath(String fileImagePath) {
            this.fileImagePath = fileImagePath;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Object getHdrisk() {
            return hdrisk;
        }

        public void setHdrisk(Object hdrisk) {
            this.hdrisk = hdrisk;
        }

        public Object getHdriskStatus() {
            return hdriskStatus;
        }

        public void setHdriskStatus(Object hdriskStatus) {
            this.hdriskStatus = hdriskStatus;
        }

        public Object getHdriskValue() {
            return hdriskValue;
        }

        public void setHdriskValue(Object hdriskValue) {
            this.hdriskValue = hdriskValue;
        }

        public Object getHealthCareAdvice() {
            return healthCareAdvice;
        }

        public void setHealthCareAdvice(Object healthCareAdvice) {
            this.healthCareAdvice = healthCareAdvice;
        }

        public Integer getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(Integer heartRate) {
            this.heartRate = heartRate;
        }

        public Integer getHeartbeatRate() {
            return heartbeatRate;
        }

        public void setHeartbeatRate(Integer heartbeatRate) {
            this.heartbeatRate = heartbeatRate;
        }

        public Object getHrv() {
            return hrv;
        }

        public void setHrv(Object hrv) {
            this.hrv = hrv;
        }

        public Object getHrvStatus() {
            return hrvStatus;
        }

        public void setHrvStatus(Object hrvStatus) {
            this.hrvStatus = hrvStatus;
        }

        public Object getHrvValue() {
            return hrvValue;
        }

        public void setHrvValue(Object hrvValue) {
            this.hrvValue = hrvValue;
        }

        public Integer getIfPayment() {
            return ifPayment;
        }

        public void setIfPayment(Integer ifPayment) {
            this.ifPayment = ifPayment;
        }

        public Object getInterpretationReportId() {
            return interpretationReportId;
        }

        public void setInterpretationReportId(Object interpretationReportId) {
            this.interpretationReportId = interpretationReportId;
        }

        public Object getKnowledge() {
            return knowledge;
        }

        public void setKnowledge(Object knowledge) {
            this.knowledge = knowledge;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public Integer getMax() {
            return max;
        }

        public void setMax(Integer max) {
            this.max = max;
        }

        public Object getMentalPressure() {
            return mentalPressure;
        }

        public void setMentalPressure(Object mentalPressure) {
            this.mentalPressure = mentalPressure;
        }

        public Object getMentalStatus() {
            return mentalStatus;
        }

        public void setMentalStatus(Object mentalStatus) {
            this.mentalStatus = mentalStatus;
        }

        public Object getMentalValue() {
            return mentalValue;
        }

        public void setMentalValue(Object mentalValue) {
            this.mentalValue = mentalValue;
        }

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }

        public Integer getNormalRate() {
            return normalRate;
        }

        public void setNormalRate(Integer normalRate) {
            this.normalRate = normalRate;
        }

        public Integer getSlowRate() {
            return slowRate;
        }

        public void setSlowRate(Integer slowRate) {
            this.slowRate = slowRate;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Object getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(Object suggestion) {
            this.suggestion = suggestion;
        }

        public String getTakeTime() {
            return takeTime;
        }

        public void setTakeTime(String takeTime) {
            this.takeTime = takeTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public Object getExtList() {
            return extList;
        }

        public void setExtList(Object extList) {
            this.extList = extList;
        }

        public Object getNormalList() {
            return normalList;
        }

        public void setNormalList(Object normalList) {
            this.normalList = normalList;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}