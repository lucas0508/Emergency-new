package com.tjmedicine.emergency.ui.transport;

import android.widget.TextView;

import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;

import butterknife.BindView;

public class transportActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitle;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_transport;
    }

    @Override
    protected void initView() {
        mTitle.setText("预约用车");
    }
}
