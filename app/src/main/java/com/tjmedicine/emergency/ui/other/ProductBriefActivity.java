package com.tjmedicine.emergency.ui.other;

import android.widget.TextView;


import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;

import butterknife.BindView;

public class ProductBriefActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitle;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_product_brief;
    }

    @Override
    protected void initView() {
        mTitle.setText("产品简介");
    }
}
