package com.tjmedicine.emergency.ui.device.ecg.helowin.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tjmedicine.emergency.R;


public class HrJinDuView extends FrameLayout {
    View weight0, weight1;
    TextView title, value, tv_value;
    FrameLayout all;

    public HrJinDuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_hr_jindu, this, true);
        weight0 = findViewById(R.id.weight0);
        weight1 = findViewById(R.id.weight1);
        title = findViewById(R.id.head);
        value = findViewById(R.id.value);
        tv_value = findViewById(R.id.tv_value);
        all = findViewById(R.id.all);

    }

    public void setTitle(String str, int value) {
        title.setText(str);
        tv_value.setText(value + "");
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 1, 605 - value);
        if (value != 0) {
            ll.addView(new View(getContext()), lp);
        }
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(R.mipmap.icon_triangle);
        lp = new LinearLayout.LayoutParams(-1, -2);
        ll.addView(iv, lp);
        if (value != 600) {
            lp = new LinearLayout.LayoutParams(-1, 1, value);
            ll.addView(new View(getContext()), lp);
        }
        all.addView(ll, -2, -1);
    }

    public void setValue(String str) {
        value.setText(str);
    }
}
