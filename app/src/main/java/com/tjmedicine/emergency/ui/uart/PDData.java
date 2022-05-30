package com.tjmedicine.emergency.ui.uart;

/**
 *
 */
public class PDData {

    private int p_name;
    private int p_num;
    private Long p_time;

    public PDData(int p_name,int p_num, Long p_time) {
        this.p_num = p_num;
        this.p_time = p_time;
        this.p_name = p_name;
    }


    public int getP_num() {
        return p_num;
    }

    public void setP_num(int p_num) {
        this.p_num = p_num;
    }

    public Long getP_time() {
        return p_time;
    }

    public void setP_time(Long p_time) {
        this.p_time = p_time;
    }

    /**
     *  1:按压
     *
     *  -1：吹气
     * @return
     */
    public int getP_name() {
        return p_name;
    }

    public void setP_name(int p_name) {
        this.p_name = p_name;
    }
}

