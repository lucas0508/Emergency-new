package com.tjmedicine.emergency;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.helowin.sdk.cache.XCache;
import com.kl.minttisdk.ble.BleManager;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.IOSStyle;
import com.mob.MobSDK;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.List;

import no.nordicsemi.android.dfu.DfuServiceInitiator;
import xyz.doikki.videoplayer.ijk.IjkPlayerFactory;
import xyz.doikki.videoplayer.player.VideoViewConfig;
import xyz.doikki.videoplayer.player.VideoViewManager;

import static com.tjmedicine.emergency.common.global.Constants.LOGGER_TAG;

import org.litepal.LitePal;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/11
 */

public class EmergencyApplication extends MultiDexApplication {

    public static final String CONNECTED_DEVICE_CHANNEL = "connected_device_channel";
    public static final String FILE_SAVED_CHANNEL = "file_saved_channel";
    public static final String PROXIMITY_WARNINGS_CHANNEL = "proximity_warnings_channel";

    private static EmergencyApplication context;
    private Handler handler;

    public static EmergencyApplication getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        XCache.INSTANCE.init(this);
//        //播放器配置，注意：此为全局配置，按需开启
//        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
//                .setLogEnabled(BuildConfig.DEBUG) //调试的时候请打开日志，方便排错
//                /** 软解，支持格式较多，可通过自编译so扩展格式，结合 {@link xyz.doikki.dkplayer.widget.videoview.IjkVideoView} 使用更佳 */
//                .setPlayerFactory(IjkPlayerFactory.create())
////                .setPlayerFactory(AndroidMediaPlayerFactory.create()) //不推荐使用，兼容性较差
//                /** 硬解，支持格式看手机，请使用CpuInfoActivity检查手机支持的格式，结合 {@link xyz.doikki.dkplayer.widget.videoview.ExoVideoView} 使用更佳 */
////                .setPlayerFactory(ExoMediaPlayerFactory.create())
//                // 设置自己的渲染view，内部默认TextureView实现
////                .setRenderViewFactory(SurfaceRenderViewFactory.create())
//                // 根据手机重力感应自动切换横竖屏，默认false
////                .setEnableOrientation(true)
//                // 监听系统中其他播放器是否获取音频焦点，实现不与其他播放器同时播放的效果，默认true
////                .setEnableAudioFocus(false)
//                // 视频画面缩放模式，默认按视频宽高比居中显示在VideoView中
////                .setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT)
//                // 适配刘海屏，默认true
////                .setAdaptCutout(false)
//                // 移动网络下提示用户会产生流量费用，默认不提示，
//                // 如果要提示则设置成false并在控制器中监听STATE_START_ABORT状态，实现相关界面，具体可以参考PrepareView的实现
////                .setPlayOnMobileNetwork(false)
//                // 进度管理器，继承ProgressManager，实现自己的管理逻辑
////                .setProgressManager(new ProgressManagerImpl())
//                .build());
        //-----------------

        context = this;
        /*测试数据库*/
        //Stetho.initializeWithDefaults(this);
        /*日志*/
        initLogger();
        /*初始化mob*/
//        MobSDK.init(this);
        /*初始化LeakCanary*/
        // initLeakCanary();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DfuServiceInitiator.createDfuNotificationChannel(this);

            final NotificationChannel channel = new NotificationChannel(CONNECTED_DEVICE_CHANNEL, getString(R.string.channel_connected_devices_title), NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(getString(R.string.channel_connected_devices_description));
            channel.setShowBadge(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            final NotificationChannel fileChannel = new NotificationChannel(FILE_SAVED_CHANNEL, getString(R.string.channel_files_title), NotificationManager.IMPORTANCE_LOW);
            fileChannel.setDescription(getString(R.string.channel_files_description));
            fileChannel.setShowBadge(false);
            fileChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            final NotificationChannel proximityChannel = new NotificationChannel(PROXIMITY_WARNINGS_CHANNEL, getString(R.string.channel_proximity_warnings_title), NotificationManager.IMPORTANCE_LOW);
            proximityChannel.setDescription(getString(R.string.channel_proximity_warnings_description));
            proximityChannel.setShowBadge(false);
            proximityChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(fileChannel);
            notificationManager.createNotificationChannel(proximityChannel);
        }

        //防止多进程注册多次  可以在MainActivity或者其他页面注册MobPushReceiver
        String processName = getProcessName(this);
        if (getPackageName().equals(processName)) {
            MobPush.addPushReceiver(new MobPushReceiver() {
                @Override
                public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
                    //接收自定义消息(透传)
                    System.out.println("onCustomMessageReceive:" + message.toString());
                }
                @Override
                public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
                    //接收通知消
                    System.out.println("MobPush onNotifyMessageReceive:" + message.toString());
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = "Message Receive:" + message.toString();
                    handler.sendMessage(msg);
                }

                @Override
                public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
                    //接收通知消息被点击事件
                    System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.toString());
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = "Click Message:" + message.toString();
                    handler.sendMessage(msg);
                }

                @Override
                public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
                    //接收tags的增改删查操作
                    System.out.println("onTagsCallback:" + operation + "  " + errorCode);
                }

                @Override
                public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
                    //接收alias的增改删查操作
                    System.out.println("onAliasCallback:" + alias + "  " + operation + "  " + errorCode);
                }
            });

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == 1) {
                        Toast.makeText(MobSDK.getContext(), "回调信息\n" + (String) msg.obj, Toast.LENGTH_LONG).show();
                        System.out.println("Callback Data:" + msg.obj);
                    }
                    return false;
                }
            });
        }
        MobPushReceiver();
        initBle(this);
        initDialogX();
//        init();
    }

    private void initDialogX() {

        //初始化
        DialogX.init(this);

        //开启调试模式，在部分情况下会使用 Log 输出日志信息
        DialogX.DEBUGMODE = true;

        DialogX.implIMPLMode= DialogX.IMPL_MODE.DIALOG_FRAGMENT;

        //设置主题样式
        DialogX.globalStyle = new IOSStyle();

        //设置亮色/暗色（在启动下一个对话框时生效）
        DialogX.globalTheme = DialogX.THEME.AUTO;

//        //设置对话框最大宽度（单位为像素）
//        DialogX.dialogMaxWidth = 1920;
//
//        //设置 InputDialog 自动弹出键盘
//        DialogX.autoShowInputKeyboard = true;
//
        //限制 PopTip 一次只显示一个实例（关闭后可以同时弹出多个 PopTip）
        DialogX.onlyOnePopTip = true;
//
//
//        //设置默认对话框背景颜色（值为ColorInt，为-1不生效）
//        DialogX.backgroundColor = Color.WHITE;
//
//        //设置默认对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（TipDialog）
//        DialogX.cancelable = true;

        //设置默认提示框及等待框（WaitDialog、TipDialog）默认是否可以关闭
        DialogX.cancelableTipDialog = false;








    }

    //初始化听诊器SDK
    private void initBle(EmergencyApplication emergencyApplication) {
        BleManager.getInstance().init(emergencyApplication);
    }

    //初始化 蒲公英SDK
//    private static void init(){
//        new PgyerSDKManager.InitSdk()
//                .setContext(context) //设置上下问对象
//                .build();
//    }

    /**
     * 初始化日志
     * (Optional) Whether to show thread info or not. Default true
     * (Optional) How many method line to show. Default 2
     * (Optional) Custom tag for each log. Default PRETTY_LOGGER
     */
    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(2)
                .tag(LOGGER_TAG)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }


    private void MobPushReceiver() {
        //防止多进程注册多次  可以在MainActivity或者其他页面注册MobPushReceiver
        String processName = getProcessName(this);
        if (getPackageName().equals(processName)) {
            MobPush.addPushReceiver(new MobPushReceiver() {
                @Override
                public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
                    //接收自定义消息(透传)
                    System.out.println("onCustomMessageReceive:" + message.toString());
                }

                @Override
                public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
                    //接收通知消
                    System.out.println("MobPush onNotifyMessageReceive:" + message.toString());
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = "Message Receive:" + message.toString();
                    handler.sendMessage(msg);
                }

                @Override
                public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
                    //接收通知消息被点击事件
                    System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.toString());
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = "Click Message:" + message.toString();
                    handler.sendMessage(msg);
                }

                @Override
                public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
                    //接收tags的增改删查操作
                    System.out.println("onTagsCallback:" + operation + "  " + errorCode);
                }

                @Override
                public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
                    //接收alias的增改删查操作
                    System.out.println("onAliasCallback:" + alias + "  " + operation + "  " + errorCode);
                }
            });

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == 1) {
                        //Toast.makeText(MobSDK.getContext(), "回调信息\n" + (String) msg.obj, Toast.LENGTH_LONG).show();
                        System.out.println("Callback Data:" + msg.obj);
                    }
                    return false;
                }
            });
        }
    }

    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }
}
