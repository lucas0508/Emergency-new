package com.tjmedicine.emergency.ui.teach.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.bean.TeachingBean;
import com.tjmedicine.emergency.model.widget.CustomWebView;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import butterknife.BindView;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.GestureView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.player.VideoView;

public class TeachingDetailActivity extends BaseActivity {


    @BindView(R.id.detail_player)
    VideoView detailPlayer;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.tv_time)
    TextView mTime;
    @BindView(R.id.tv_Caption)
    TextView mCaption;


    private TeachingBean.ListBean teachingBean;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_teaching_detail;
    }

    @Override
    protected void initView() {
        mTitle.setText("??????");
        teachingBean = (TeachingBean.ListBean) getIntent().getSerializableExtra("teachingBean");

        mCaption.setText(teachingBean.getTitle());
        mTime.setText(teachingBean.getCreateAt());

        if (!TextUtils.isEmpty(teachingBean.getPlayUrl())) {
            detailPlayer.setVisibility(View.VISIBLE);
            playVideo(teachingBean.getPlayUrl());
        }

        RichText.initCacheDir(this);
        RichText.debugMode = true;
        RichText.fromHtml(teachingBean.getContent())
                .urlClick(new OnUrlClickListener() {
                    @Override
                    public boolean urlClicked(String url) {
                        if (url.startsWith("code://")) {
                            Toast.makeText(TeachingDetailActivity.this, url.replaceFirst("code://", ""), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        return false;
                    }
                })
                .into(mContent);
        //       DownLoadUtil.setHtmlText(mContent,teachingBean.getContent(),200,200);
    }

    private void playVideo(String playUrl) {
        detailPlayer.setUrl(playUrl); //??????????????????
        StandardVideoController controller = new StandardVideoController(this);
        controller.addDefaultControlComponent(teachingBean.getTitle(), false);
        //??????????????????????????????/????????????
        controller.setEnableOrientation(true);
        PrepareView prepareView = new PrepareView(this);//??????????????????
        prepareView.setClickStart();
        //????????????
        ImageView imageView = new ImageView(this);
        loadCover(imageView, playUrl);
        controller.addControlComponent(prepareView);

        controller.addControlComponent(new CompleteView(this));//????????????????????????

        controller.addControlComponent(new ErrorView(this));//????????????

        TitleView titleView = new TitleView(this);//?????????
        controller.addControlComponent(titleView);
        VodControlView vodControlView = new VodControlView(this);//???????????????
        //??????????????????????????????????????????
//                vodControlView.showBottomProgress(false);
        controller.addControlComponent(vodControlView);

        GestureView gestureControlView = new GestureView(this);//??????????????????
        controller.addControlComponent(gestureControlView);
        //?????????????????????????????????????????????????????????
        controller.setCanChangePosition(true);
        detailPlayer.setVideoController(controller); //???????????????
        //??????????????????
        detailPlayer.setProgressManager(new ProgressManagerImpl());
    }


    @Override
    protected void onPause() {
        super.onPause();
        detailPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailPlayer.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailPlayer.release();
    }


    @Override
    public void onBackPressed() {
        if (!detailPlayer.onBackPressed()) {
            super.onBackPressed();
        }
    }


    private void loadCover(ImageView imageView, String url) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.upload_defaut);
        Glide.with(this.getApplicationContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(3000000)
                                .centerCrop()
                                .error(R.mipmap.upload_defaut)
                                .placeholder(R.mipmap.upload_defaut))
                .load(url)
                .into(imageView);
    }

}
