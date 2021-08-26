package com.tjmedicine.emergency.ui.uart.data.presenter;

public class PDScoreData {


    private String pd;
    private String pd_rebound;
    private String pd_depth;
    private String pd_time;
    private String bl_pd;
    private String result;

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getPd_rebound() {
        return pd_rebound;
    }

    public void setPd_rebound(String pd_rebound) {
        this.pd_rebound = pd_rebound;
    }

    public String getPd_depth() {
        return pd_depth;
    }

    public void setPd_depth(String pd_depth) {
        this.pd_depth = pd_depth;
    }

    public String getPd_time() {
        return pd_time;
    }

    public void setPd_time(String pd_time) {
        this.pd_time = pd_time;
    }

    public String getBl_pd() {
        return bl_pd;
    }

    public void setBl_pd(String bl_pd) {
        this.bl_pd = bl_pd;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
