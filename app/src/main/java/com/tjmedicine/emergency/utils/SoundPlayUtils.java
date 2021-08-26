package com.tjmedicine.emergency.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.tjmedicine.emergency.R;


/**
 * Created by SXJ on 2017/3/1 0001.
 */

public class SoundPlayUtils {
    // SoundPool对象
    private static SoundPool mSoundPlayer;
    // 上下文
    private Context mContext;

    public SoundPlayUtils(Context context) {
        mContext = context;
        init();
    }


    private void init() {
        if (Build.VERSION.SDK_INT < 21) {
            mSoundPlayer = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        } else {
            mSoundPlayer = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build())
                    .build();
        }
        // 初始化声音
        mSoundPlayer.load(mContext, R.raw.award, 1);// 1
        mSoundPlayer.load(mContext, R.raw.button, 1);// 2
        mSoundPlayer.load(mContext, R.raw.flag, 1);// 3
        mSoundPlayer.load(mContext, R.raw.score, 1);// 4
        mSoundPlayer.load(mContext, R.raw.start, 1);// 5
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }
}
