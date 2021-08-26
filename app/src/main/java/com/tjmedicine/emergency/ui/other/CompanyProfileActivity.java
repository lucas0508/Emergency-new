package com.tjmedicine.emergency.ui.other;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;


import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;

import butterknife.BindView;

public class CompanyProfileActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.textView3)
    TextView textView;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        mTitle.setText("公司简介");
        String ServiceAgreement = "https://www.qujiali.com";
        SpannableString spannableString = new SpannableString(Html.fromHtml("\u3000\u3000登录网址："+ServiceAgreement+"了解更多公司信息"));


        spannableString.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(ServiceAgreement);//此处填链接
                intent.setData(content_url);
                startActivity(intent);
            }
        }), 7, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    class Clickable extends ClickableSpan implements View.OnClickListener {

        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            //设置是否有下划线
            ds.setColor(Color.parseColor("#2d8cf0"));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
