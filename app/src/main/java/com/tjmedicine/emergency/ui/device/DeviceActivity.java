package com.tjmedicine.emergency.ui.device;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;

import butterknife.BindView;

public class DeviceActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.ll_recorder)
    LinearLayout recorder;
    @BindView(R.id.ll_stethoscope)
    LinearLayout stethoscope;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_device;
    }

    @Override
    protected void initView() {
        mTitle.setText("我的设备");
    }
}
