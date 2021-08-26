package com.tjmedicine.emergency.common.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.KeyEvent;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.model.widget.KeepCountdownView;

/**
 * 连接
 */
public class CustomDjsFullScreenPopup extends FullScreenPopupView {

    private VideoView mVideoView;
    private Context mContext;
    public OnMyCompletionListener onCompletionListener;


    public interface OnMyCompletionListener {
        public void onClick();
    }

    public CustomDjsFullScreenPopup(@NonNull Context context,OnMyCompletionListener onCompletionListenerl) {
        super(context);
        mContext = context;
        this.onCompletionListener=onCompletionListenerl;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_djsfullscreen_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        mVideoView = findViewById(R.id.videoView);
        // 加载视频资源
        mVideoView.setVideoURI(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.djs));

        mVideoView.setZOrderOnTop(true);
        //播放
        mVideoView.start();
        //循环播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //一轮视频结束后
                onCompletionListener.onClick();
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}