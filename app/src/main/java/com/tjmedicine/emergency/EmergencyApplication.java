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

import static com.tjmedicine.emergency.common.global.Constants.LOGGER_TAG;

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

//        init();
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
