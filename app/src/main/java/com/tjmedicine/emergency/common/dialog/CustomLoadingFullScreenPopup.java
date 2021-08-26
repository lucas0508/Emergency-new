package com.tjmedicine.emergency.common.dialog;

import android.content.Context;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.tjmedicine.emergency.R;

/**
 * 加载
 */
public class CustomLoadingFullScreenPopup extends FullScreenPopupView {

    public CustomLoadingFullScreenPopup(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_loading_fullscreen_popup;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        //初始化
        LottieAnimationView mAnimationView = findViewById(R.id.av);
        mAnimationView.setAnimation("data.zip");
        mAnimationView.playAnimation();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return  true;
        }
        return  super.onKeyDown(keyCode, event);
    }
}