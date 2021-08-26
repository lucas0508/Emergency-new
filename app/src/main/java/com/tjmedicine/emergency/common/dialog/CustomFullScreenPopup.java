package com.tjmedicine.emergency.common.dialog;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.tjmedicine.emergency.R;

/**
 * 连接
 */
public class CustomFullScreenPopup extends FullScreenPopupView {

    public CustomFullScreenPopup(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_fullscreen_popup;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        //初始化
        LottieAnimationView mAnimationView = findViewById(R.id.av);
        mAnimationView.setAnimation("anim.json");
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
}