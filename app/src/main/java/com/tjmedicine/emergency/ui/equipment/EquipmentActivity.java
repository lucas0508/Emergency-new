package com.tjmedicine.emergency.ui.equipment;

import android.view.View;
import android.widget.LinearLayout;

import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;

import butterknife.BindView;

public class EquipmentActivity extends BaseActivity {

    @BindView(R.id.ll_xd_sb)
    LinearLayout ll_xd_sb;
    @BindView(R.id.ll_xf_sb)
    LinearLayout ll_xf_sb;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_equipment;
    }

    @Override
    protected void initView() {
        ll_xd_sb.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {

            }
        });

        ll_xf_sb.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {

            }
        });
    }
}
