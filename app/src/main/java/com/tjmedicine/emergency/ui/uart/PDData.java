package com.tjmedicine.emergency.ui.uart;

/**
 *
 */
public class PDData {

    private int p_num;
    private Long p_time;

    public PDData(int p_num, Long p_time) {
        this.p_num = p_num;
        this.p_time = p_time;
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
}

