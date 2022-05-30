package com.tjmedicine.emergency.ui.other;

import android.graphics.PointF;
import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;

import java.io.File;

import butterknife.BindView;

public class MnrHelpActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.image_big)
    SubsamplingScaleImageView image_big;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_mnrhelp;
    }

    @Override
    protected void initView() {
        tv_title.setText("使用说明");

        String URL = "http://qiniu.app.mengyuanyiliao.com/wawa.jpg";

       // image_big.setImage(ImageSource.resource(R.mipmap.wawa));

//下载图片保存到本地
        Glide.with(this)
                .load(URL).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                image_big.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(0F, new PointF(0, 0), 0));
            }

           });
    }
}
