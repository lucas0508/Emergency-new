package com.tjmedicine.emergency.ui.device.ecg;

import android.view.View;
import android.widget.TextView;


import com.helowin.ecg.sdk.widget.CountDownCircleView;
import com.tjmedicine.emergency.R;


public class HisView{
    CountDownCircleView aiv2;
    TextView time;
    View show;
    View view;

    public View getV() {
        return view;
    }

    public void init(View view) {
        aiv2 = view.findViewById(R.id.aiv2);
        time = view.findViewById(R.id.time);
        show = view.findViewById(R.id.show);
        this.view = view;
    }

    public void show(String banfeibi, String time, int index, int max) {
        this.view.setVisibility(View.VISIBLE);
        aiv2.setValue(banfeibi);
        aiv2.setMax(max);
        aiv2.loading(index);
        this.time.setText("预计剩余时间 " + time);
    }

    public void showLowEng(boolean flag) {

    }

    public void update() {
        aiv2.setValue("上传中");
        aiv2.loading(0);
        this.show.setVisibility(View.GONE);
    }

    public void updateFail() {
        aiv2.setValue("上传失败");
        aiv2.loading(0);
        this.show.setVisibility(View.GONE);
    }

    public void updateSuccess() {
        aiv2.setValue("上传成功");
        aiv2.loading(0);
        this.show.setVisibility(View.GONE);
    }

    public void showUpdateFile(int index, int max) {
        double d = 100f * index / max;
        String result = String.format("%.2f", d) + "%";
        aiv2.setValue(result);
        aiv2.setMax(max);
        aiv2.loading(index);
        this.time.setText("上传中");
        this.show.setVisibility(View.GONE);
    }

}
