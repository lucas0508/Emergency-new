package com.tjmedicine.emergency.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import static android.view.animation.Animation.INFINITE;

/**
 * Created by SXJ on 2017/3/6 0006.
 */

public class AnimUtil {
    public static void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(1);
        objectAnimator.start();
    }

    public static void scaceAnim(View view) {
        ObjectAnimator scaleAnim1 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.5f, 1.0f, 1.5f, 1.0f);
        // 设置ScaleAnim1动画持续时间
        scaleAnim1.setDuration(1000);
        // 设置ScaleAnim1动画的插值方式：减速插值
        scaleAnim1.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator scaleAnim2 = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.5f, 1.0f, 1.5f, 1.0f);
        // 设置ScaleAnim2动画持续时间
        scaleAnim2.setDuration(1000);
        // 设置ScaleAnim2动画的插值方式：减速插值
        scaleAnim2.setInterpolator(new DecelerateInterpolator());
        scaleAnim1.start();
        scaleAnim2.start();
    }

    public static void arrowAnim(View view) {
        ObjectAnimator scaleAnim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 0.8f);
        // 设置ScaleAnim1动画持续时间
        scaleAnim1.setDuration(800);
        // 设置ScaleAnim1动画的插值方式：减速插值
        scaleAnim1.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator scaleAnim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 0.8f);
        // 设置ScaleAnim2动画持续时间
        scaleAnim2.setDuration(800);
        // 设置ScaleAnim2动画的插值方式：减速插值
        scaleAnim2.setInterpolator(new DecelerateInterpolator());
        scaleAnim1.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim2.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim1.setRepeatCount(INFINITE);
        scaleAnim2.setRepeatCount(INFINITE);
        scaleAnim1.start();
        scaleAnim2.start();
    }

    public static void starAnim(View view) {
        ObjectAnimator scaleAnim1 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.0f);
        // 设置ScaleAnim1动画持续时间
        scaleAnim1.setDuration(500);
        // 设置ScaleAnim1动画的插值方式：减速插值
        scaleAnim1.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator scaleAnim2 = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        // 设置ScaleAnim2动画持续时间
        scaleAnim2.setDuration(500);
        // 设置ScaleAnim2动画的插值方式：减速插值
        scaleAnim2.setInterpolator(new DecelerateInterpolator());
        scaleAnim1.start();
        scaleAnim2.start();
    }

    public static void starAnim2(View view) {
        ObjectAnimator scaleAnim1 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.0f);
        // 设置ScaleAnim1动画持续时间
        scaleAnim1.setDuration(1000);
        // 设置ScaleAnim1动画的插值方式：减速插值
        scaleAnim1.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator scaleAnim2 = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        // 设置ScaleAnim2动画持续时间
        scaleAnim2.setDuration(1200);
        // 设置ScaleAnim2动画的插值方式：减速插值
        scaleAnim2.setInterpolator(new DecelerateInterpolator());
        scaleAnim1.start();
        scaleAnim2.start();
    }

    public static void scaleUpDown(View view, float start) {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(view, "translationY", start);
        // 设置ScaleAnim2动画持续时间
        scaleAnim.setDuration(500);
        // 设置ScaleAnim2动画的插值方式：减速插值
        scaleAnim.setInterpolator(new DecelerateInterpolator());
        scaleAnim.start();
    }

    public static void transX(View view, float distance) {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(view, "translationX", distance);
        // 设置ScaleAnim2动画持续时间
        scaleAnim.setDuration(1000);
        // 设置ScaleAnim2动画的插值方式：减速插值
        scaleAnim.setInterpolator(new DecelerateInterpolator());
        scaleAnim.start();
    }
}
