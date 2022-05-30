package com.tjmedicine.emergency.ui.teach.view;


import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;

import butterknife.BindView;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.GestureView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.player.VideoView;

public class ceshishis extends BaseActivity {

    @BindView(R.id.player)
    VideoView player1;


    private static final String THUMB = "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg";

    private static final String LIVE_URL = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4";

    ProgressManagerImpl progressManager = new ProgressManagerImpl();


    private StandardVideoController mController;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.ceshi;
    }

    @Override
    protected void initView() {

        video();
    }

    private void video() {
        player1.setUrl(LIVE_URL); //设置视频地址
        StandardVideoController controller = new StandardVideoController(this);
        controller.addDefaultControlComponent("标题", false);

        //根据屏幕方向自动进入/退出全屏
        controller.setEnableOrientation(true);

        PrepareView prepareView = new PrepareView(this);//准备播放界面
        prepareView.setClickStart();
        ImageView thumb = prepareView.findViewById(R.id.thumb);//封面图
        Glide.with(this).load(THUMB).into(thumb);
        controller.addControlComponent(prepareView);

        controller.addControlComponent(new CompleteView(this));//自动完成播放界面

        controller.addControlComponent(new ErrorView(this));//错误界面

        TitleView titleView = new TitleView(this);//标题栏
        controller.addControlComponent(titleView);
        VodControlView vodControlView = new VodControlView(this);//点播控制条
        //是否显示底部进度条。默认显示
//                vodControlView.showBottomProgress(false);
        controller.addControlComponent(vodControlView);

        GestureView gestureControlView = new GestureView(this);//滑动控制视图
        controller.addControlComponent(gestureControlView);
        //根据是否为直播决定是否需要滑动调节进度
        controller.setCanChangePosition(true);
        player1.setVideoController(controller); //设置控制器
        //保存播放进度
        player1.setProgressManager(new ProgressManagerImpl());
    }


    @Override
    protected void onPause() {
        super.onPause();
        player1.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player1.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player1.release();
    }


    @Override
    public void onBackPressed() {
        if (!player1.onBackPressed()) {
            super.onBackPressed();
        }


    }
}
