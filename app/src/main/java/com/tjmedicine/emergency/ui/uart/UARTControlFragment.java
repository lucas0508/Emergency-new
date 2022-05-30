/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tjmedicine.emergency.ui.uart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.wifi.aware.AttachCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.lxj.xpopup.XPopup;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.Adapter;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.base.ViewHolder;
import com.tjmedicine.emergency.common.dialog.CustomDjsFullScreenPopup;
import com.tjmedicine.emergency.common.dialog.DialogManage;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.model.widget.BGAProgressBar;
import com.tjmedicine.emergency.model.widget.BatteryView;
import com.tjmedicine.emergency.ui.main.MainActivity;
import com.tjmedicine.emergency.ui.other.MnrHelpActivity;
import com.tjmedicine.emergency.ui.uart.data.presenter.IUARTControlView;
import com.tjmedicine.emergency.ui.uart.data.presenter.PDScoreData;
import com.tjmedicine.emergency.ui.uart.data.presenter.UARTControlPresenter;
import com.tjmedicine.emergency.ui.uart.domain.UartConfiguration;
import com.tjmedicine.emergency.ui.uart.profile.UARTInterface;
import com.tjmedicine.emergency.utils.GsonUtils;
import com.tjmedicine.emergency.utils.SoundPlayUtils;
import com.tjmedicine.emergency.utils.ToastUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 练习模式
 */
public class UARTControlFragment extends Fragment implements UARTActivity.ConfigurationListener, IUARTControlView {
    private final static String TAG = "UARTControlFragment";
    private final static String SIS_EDIT_MODE = "sis_edit_mode";
    private final static String ACTION_RECEIVE = "no.nordicsemi.android.nrftoolbox.uart.ACTION_RECEIVE";
    private UARTInterface uartInterface;
    private boolean editMode;
    private MyReceiver myReceiver;
    TextView tv_connect, tv_Battery, mHistoricalData, tv_bga_pdcount, tv_bga_blow, tv_bga_count, button_big;
    BatteryView batteryview;
    Button mStartRobot;
    Chronometer chronometer;
    ImageView mCountdownView, iv_setting, iv_point, mmr;


    long startTime;
    boolean incline = true;//是否点击开始模拟人
    List<String> liscount;
    List<String> lisdata;
    List<String> serverDataList;
    List<String> blowList = new ArrayList<>();

    //所有数据点组成集合
    List<Integer> listPD = new ArrayList<>();

    LineChart mChart1;
    private DynamicLineChartManager dynamicLineChartManager1;
    Timer timer;
    private UARTService.UARTBinder bleService;
    private UARTControlPresenter uartControlPresenter = new UARTControlPresenter(this);
    DialogManage mApp;
    //是否吹气   每个15次按压检测是否有吹气
    int blowCount = 0;
    private CustomDjsFullScreenPopup customDjsFullScreenPopup;
    private BGAProgressBar bga_pdcount_progress;
    private BGAProgressBar bga_blow_progress;
    private int g = 0;//吹去次数
    private int j = 0;//按压次数
    private AnimationDrawable animationDrawable;
    private MediaPlayer mediaPlayer;
    private ImageButton ib_closelianxi;

    private ProgressDialog dialog;
    private UpdateFW.DataCallback dataCallback;
    private UpdateFW updateFW;
    private String strFWVersion, strHWVersion;

    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);

        try {
            ((UARTActivity) context).setConfigurationListener(this);
        } catch (final ClassCastException e) {
            Log.e(TAG, "The parent activity must implement EditModeListener");
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            editMode = savedInstanceState.getBoolean(SIS_EDIT_MODE);
        }



        /*
         * If the service has not been started before the following lines will not start it. However, if it's running, the Activity will be bound to it
         * and notified via serviceConnection.
         */
        final Intent service = new Intent(EmergencyApplication.getContext(), UARTService.class);
        requireActivity().bindService(service, serviceConnection, 0); // we pass 0 as a flag so the service will not be created if not exists

        //注册广播接收器
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE);
        requireActivity().registerReceiver(myReceiver, intentFilter);
        mApp = new DialogManage(requireActivity());
        new SoundPlayUtils(requireActivity());

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            bleService = (UARTService.UARTBinder) service;
            uartInterface = bleService;
            Log.e(TAG, " --UARTControlFragment1111->" + bleService.isConnected());
            bleService.send("dsadsadsadsa");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            uartInterface = null;
        }
    };


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(SIS_EDIT_MODE, editMode);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_feature_uart_control, container, false);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tv_connect = view.findViewById(R.id.action_connect);
        tv_bga_count = view.findViewById(R.id.tv_bga_count);
        mStartRobot = view.findViewById(R.id.start_robot);
        chronometer = view.findViewById(R.id.chronometer);
        batteryview = view.findViewById(R.id.batteryview);
        tv_Battery = view.findViewById(R.id.tv_Battery);
        mChart1 = view.findViewById(R.id.dynamic_chart1);
        iv_setting = view.findViewById(R.id.iv_setting);
        ib_closelianxi = view.findViewById(R.id.ib_closelianxi);
        iv_point = view.findViewById(R.id.iv_point);
        button_big = view.findViewById(R.id.button_big);
        tv_bga_pdcount = view.findViewById(R.id.tv_bga_pdcount);
        tv_bga_blow = view.findViewById(R.id.tv_bga_blow);
        mHistoricalData = view.findViewById(R.id.tv_historical_data);
        bga_pdcount_progress = view.findViewById(R.id.bga_pdcount_progress);
        bga_blow_progress = view.findViewById(R.id.bga_blow_progress);
        mmr = view.findViewById(R.id.mmr);


        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyApplication.getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        ib_closelianxi.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Log.e(TAG, "onMultiClick:练习模式-- ");

                if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                if (null != animationDrawable) {
                    animationDrawable.stop();
                }
                if (null != uartInterface) {
                    Log.e(TAG, "onMultiClick:2222-- " + incline);
                    mStartRobot.setText("开启模拟人");
                    uartInterface.send("<TestStop>");
                    chronometer.setFormat("计时结束：%s");
                    chronometer.stop();
                }
                if (timer != null) {
                    timer.cancel();
                }
                if (myReceiver != null) {
                    requireActivity().unregisterReceiver(myReceiver);
                    myReceiver = null;
                }
                ((UARTActivity) requireActivity()).setConfigurationListener(null);
                if (uartInterface != null) {
                    HttpProvider.doGet(GlobalConstants.APP_BURIED_POINT + Constants.B_BLE_END + "," + bleService.getDeviceAddress(), null);
//            uartInterface.send("<TestStop>");
                    uartInterface = null;
                }
                if (serviceConnection!=null){
                    requireActivity().unbindService(serviceConnection);
                    serviceConnection=null;
                }
                if (bleService != null) {
                    try {
                        bleService.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                requireActivity().finish();
            }
        });
        chronometer.setFormat("练习时长：%s");
        mCountdownView = view.findViewById(R.id.iv_countdown);
        if (System.currentTimeMillis() - startTime == 5000) {
            Log.e(TAG, " --UARTControlFragment->" + String.valueOf(System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();
        }
        startUARTRobot();
        initData();//初始化数据
        initChart();//数据设置

        mHistoricalData.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(requireActivity(), UARTRobotActivity.class);
                intent.putExtra("", "");
                startActivity(intent);
            }
        });

        button_big.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(requireActivity(), MnrHelpActivity.class);
                startActivity(intent);
            }
        });

        //检测模拟人是不是最新


        return view;
    }

    private void isUpdataFW() {

        new Thread() {
            @Override
            public void run() {
                Log.e(TAG, "Update FW Start!");

//                String strRsp = null;
//
//                strRsp = updateFW.sendChecksumCommand("NewVer=" + versionInVersionFile);
//                if (strRsp == null)
//                    return false;
//                if (!strRsp.equals("<OK>"))
//                    return false;


//                updateFW = new UpdateFW(SettingActivity.this, strFWVersion,strHWVersion, uartInterface, progress -> runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.setProgress(progress);
//                    }
//                }));

                dataCallback = new UpdateFW.DataCallback() {
                    @Override
                    public void setDataProgress(int progress) {

                        dialog.setProgress(progress);
                    }
                };

                updateFW = new UpdateFW(EmergencyApplication.getContext(), strFWVersion, strHWVersion, uartInterface, dataCallback);

                String s = updateFW.updateRes();
                if (!TextUtils.isEmpty(s)) {

                    return;
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = new ProgressDialog(EmergencyApplication.getContext());
                        dialog.setTitle("下载提示");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setMessage("Update FW Start!");
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });


                if (updateFW.isUpdateFW()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            ToastUtils.showTextToas(EmergencyApplication.getContext(), "FW Update Success!");
                            new SweetAlertDialog(EmergencyApplication.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("版本升级成功")
                                    .setContentText("FW Update Success!请重新开启模拟人并连接手机")
                                    .setConfirmText("确认")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            Intent intent = new Intent(EmergencyApplication.getContext(), MainActivity.class);
                                            startActivity(intent);
                                            requireActivity().finish();
                                        }
                                    }).show();
                        }
                    });
                    Log.e(TAG, "FW Update Success!");
                } else {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            dialog.setMessage("FW Update Failed!");
                            ToastUtils.showTextToas(EmergencyApplication.getContext(), "FW Update Failed!");
                            dialog.dismiss();
                        }
                    });
                    Log.e(TAG, "FW Update Failed!");
                }
            }
        }.start();
    }


    /**
     * 开启模拟人
     */
    private void startUARTRobot() {
        mStartRobot.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (null != uartInterface) {
                    if (incline) {
                        customDjsFullScreenPopup = new CustomDjsFullScreenPopup(requireActivity(), new CustomDjsFullScreenPopup.OnMyCompletionListener() {
                            @Override
                            public void onClick() {
                                customDjsFullScreenPopup.dismiss();
                                //播放完成回调
                                initData();//初始化数据
//                                initChart();//数据设置

                                //final String text = field.getText().toString();
                                Log.e(TAG, "onMultiClick:111-- " + incline);
                                if (null != uartInterface) {
                                    uartInterface.send("<TestStart>");
//                                    uartInterface.send("<MaxPressCal>");
//                                    uartInterface.send("<NewVer=v0.1.0|F7>");
                                    starttime();
                                }
                            }
                        });
                        new XPopup.Builder(requireActivity()).asCustom(customDjsFullScreenPopup).show();
                    } else {
                        stoptime();
                    }
                } else {
                    ToastUtils.showTextToas(requireActivity(), "请连接模拟人后在点击开始练习哦~");
                    //starttime();
                }
            }
        });

    }

    private void stoptime() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        animationDrawable.stop();
        Log.e(TAG, "onMultiClick:2222-- " + incline);
        mStartRobot.setText("开启模拟人");
        uartInterface.send("<TestStop>");
        chronometer.setFormat("计时结束：%s");
        chronometer.stop();
        if (timer != null) {
            timer.cancel();
        }
        Log.e(TAG, " --max-------> " + new Gson().toJson(listPD));
        for (int i = 1; i < listPD.size() - 1; i++) {
            if (listPD.get(i) > listPD.get(i - 1) && listPD.get(i) >= listPD.get(i + 1)) {
                if (listPD.get(i) > 30) {
                    Log.e(TAG, " --max-------> " + listPD.get(i));
                }
            }

            //n-1 =n  n< n+1				n-1<n  n<n+1		  n-1  >n   n=n+1
            if (listPD.get(i) <= listPD.get(i - 1) && listPD.get(i) < listPD.get(i + 1)) {
                Log.e(TAG, " --min-------> " + listPD.get(i));
            }
        }

//        mApp.getLoadingDialog().show();
        HttpProvider.doGet(GlobalConstants.APP_BURIED_POINT + Constants.B_BLE_END + "," + bleService.getDeviceAddress(), null);
        Log.e(TAG, " --UARTControlFragment111---传递服务器数据:-------> " + new Gson().toJson(serverDataList));

        //服务器传递数据
        //uartControlPresenter.postUARTData(serverDataList, "2", null);

        bga_pdcount_progress.setProgress(j);
        bga_blow_progress.setProgress(g);
        tv_bga_pdcount.setText("按压次数" + j + "次");
        tv_bga_blow.setText("人工呼吸" + g + "次");
        incline = true;

    }

    private void starttime() {

        HttpProvider.doGet(GlobalConstants.APP_BURIED_POINT + Constants.B_BLE_START + "," + bleService.getDeviceAddress(), null);
        startTimer();
        liscount = new ArrayList<>();
        lisdata = new ArrayList<>();
        listPD = new ArrayList<>();


        listPD1 = new ArrayList<>();
        bga_pdcount_progress.setProgress(0);
        bga_blow_progress.setProgress(0);
        tv_bga_pdcount.setText("按压次数" + 0 + "次");
        tv_bga_blow.setText("人工呼吸" + 0 + "次");
        tv_bga_count.setText("吹气量" + 0 + "ml");

        g = 0;
        incline = false;
        serverDataList = new ArrayList<>();
        mediaPlayer = MediaPlayer.create(EmergencyApplication.getContext(), R.raw.pd);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }


    private void startTimer() {
        iv_point.setImageResource(R.drawable.animati_redpoint);
        animationDrawable = ((AnimationDrawable) iv_point.getDrawable());
        animationDrawable.start();

        startTime = System.currentTimeMillis();
        // 启动计时器，延迟 1s 执行，每 10s 执行一次
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = "";
                msg.sendToTarget();

            }
        }, 10000, 10000);
        //ToastUtils.showImageToas(requireActivity(), "开始模拟人操作,请按压模拟人");
        mStartRobot.setText("结束");
        chronometer.setFormat("练习时长：%s");
        chronometer.setBase(SystemClock.elapsedRealtime());// 去掉可实现暂停功能
        chronometer.start();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // PDSpeed();
        }
    };

    private void initChart() {
        LineData lineData = new LineData();// //线的总管理
        mChart1.setData(lineData);//把线条设置给你的lineChart上
        mChart1.invalidate();//刷新


    }

    private void initData() {
        //折线名字
        List<String> names = new ArrayList();
        List<Integer> colour = new ArrayList();
        List<Integer> list = new ArrayList<>();
        names.add("温度");
        names.add("压强");
        names.add("其他");
        //折线颜色
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);
        colour.add(Color.BLUE);

        dynamicLineChartManager1 = new DynamicLineChartManager(mChart1, names.get(0), colour.get(0));
        dynamicLineChartManager1.setYAxis(80, 0, 10);
        dynamicLineChartManager1.setHightLimitLine(60f, "", Color.GREEN);
        dynamicLineChartManager1.setHightLimitLine(50f, "", Color.GREEN);
    }

    /**
     * 频率
     * 按压速度
     * 30次按压之后+2次呼吸
     */
    private void PDSpeed() {
        Log.d("TAG", "handleMessage: timer  lisdata-->" + lisdata.size());
        // 20次/10S
        for (int i = 0; i < lisdata.size(); i++) {
            if (lisdata.get(i).contains("PD")) {
                liscount.add(lisdata.get(i));  // 10s内 blow的次数
            }
        }
        //x < 16
        // 10S 按压16-20次
        //x> 20
        if (liscount.size() < 16) {
            liscount.clear();
            lisdata.clear();
            MediaPlayer mediaPlayer = MediaPlayer.create(EmergencyApplication.getContext(), R.raw.pd_slow);
            mediaPlayer.start();
            Log.d("TAG", "handleMessage: timer 按压太慢慢慢了");
        } else if (liscount.size() > 20) {
            MediaPlayer mediaPlayer = MediaPlayer.create(EmergencyApplication.getContext(), R.raw.pd_fast);
            mediaPlayer.start();
            liscount.clear();
            lisdata.clear();
            Log.d("TAG", "handleMessage: timer 按压太快快快了");
        }
    }


    @Override
    public void onConfigurationModified() {
    }

    @Override
    public void onConfigurationChanged(@NonNull final UartConfiguration configuration) {

    }

    @Override
    public void postUARTDataSuccess(PDScoreData data) {
        mApp.getLoadingDialog().hide();
    }

    @Override
    public void postUARTDataFail(String info) {
        mApp.shortToast(info);
        mApp.getLoadingDialog().hide();
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收MainActivity传过来的数据
//            Toast.makeText(context, intent.getStringExtra(Intent.EXTRA_TEXT), Toast.LENGTH_SHORT).show();
            String data = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.e(TAG, " --UARTControlFragment111---onReceive:-------> " + data);
            String s = GsonUtils.returnFormatText2(data.trim());
            Log.e(TAG, "onReceive: handleMessage1111：---->" + s);
//            Map<String, Object> stringObjectMap = new HashMap<>();
//            stringObjectMap.put("id", System.currentTimeMillis());
//            stringObjectMap.put("data", data);
            //SQLiteHelper.getInstance().mUartDB.insertData(SQLiteHelper.getInstance().getDBInstance(), stringObjectMap);
//            String s = GsonUtils.returnFormatText(data);
            // String s1 = GsonUtils.returnStatus(s);//最终要展示的数据
            Message message = Message.obtain();
            message.obj = s;
            mHandler.sendMessage(message);
        }
    }

    List<PDData> listPD1 = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String s = msg.obj.toString();
            if (!TextUtils.isEmpty(s)) {
                if (s.equals("OK")) {
                    return;
                }
                if (s.startsWith("Errno=")) {
                    /**
                     * 1	未识别指令
                     * 2	参数错误
                     * 3	校验错误
                     * 11	系统错误
                     * 12	压力传感器未校准
                     * 13	数据不完整
                     */
                    String errorMsg = "";
                    String[] split = s.split("=");
                    String[] split1 = split[1].split("\\|");
                    if (split1[0].equals("1")) {
                        errorMsg = "未识别指令";
                    } else if (split1[0].equals("2")) {
                        errorMsg = "参数错误";
                    } else if (split1[0].equals("3")) {
                        errorMsg = "校验错误";
                    } else if (split1[0].equals("11")) {
                        errorMsg = "系统错误";
                    } else if (split1[0].equals("12")) {
                        errorMsg = "压力传感器未校准,请到设置中校准模拟人";
                    } else if (split1[0].equals("13")) {
                        errorMsg = "数据不完整";
                    }
                    mApp.longToast("错误码 code: " + split1[0] + ",错误信息msg:" + errorMsg);
                    stoptime();
                    return;
                }

                if (s.startsWith("Battery=")) {
                    String[] split = s.split("=");
                    tv_Battery.setText("当前电量:" + "\n\n" + split[1] + "%");
                    batteryview.setPower(Integer.parseInt(split[1]));
                } else if (s.startsWith("Blow=")) {
                    String[] split = s.split("=");
                    g++;
                    Log.e(TAG, "onReceive: 吹气：---->" + s);
                    //实时统计吹气次数
                    bga_blow_progress.setProgress(g);
                    tv_bga_blow.setText("人工呼吸" + g + "次");
                    tv_bga_count.setText("吹气量" + split[1] + "ml");
                    mmrAnim();
                } else if (s.contains("FWVer")) {
                    String[] split = s.split("=");
                    strFWVersion = split[1];
                    isUpdataFW();
                } else if (s.contains("HWVer")) {
                    String[] split = s.split("=");
                    strHWVersion = split[1];
                } else {
                    Log.e(TAG, "onReceive: 显示所有点：---->" + s);
                    if (!s.contains("Blow")) {
                        String[] split = s.split(",");
                        for (int i = 0; i < split.length; i++) {
                            dynamicLineChartManager1.addEntry(Integer.parseInt(split[i]));
                            listPD.add(Integer.parseInt(split[i]));
                            PDData pdData = new PDData(-1, Integer.parseInt(split[i]), System.currentTimeMillis());
                            listPD1.add(pdData);
                        }
                    }
                }
                //按压次数
                j = 0;
                if (listPD1.size() > 0) {

                    for (int i = 1; i < listPD1.size() - 1; i++) {
                        if (listPD1.get(i).getP_num() > listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() >= listPD1.get(i + 1).getP_num()) {
                            if (listPD1.get(i).getP_num() > 30) {
                                j++;
                                //实时统计按压次数
                                bga_pdcount_progress.setProgress(j);
                                tv_bga_pdcount.setText("按压次数" + j + "次");
                                Log.e(TAG, " 实际总次数--maxj-------> " + j);
                            }
                        }
                        //n-1 =n  n< n+1				n-1<n  n<n+1		  n-1  >n   n=n+1
                        if (listPD1.get(i).getP_num() <= listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() < listPD1.get(i + 1).getP_num()) {
                            Log.e(TAG, " --min-------> " + new Gson().toJson(listPD1.get(i)));
                        }
                    }
                }
//                serverDataList.add(s);
            }

            //---------本地数据库存储--------------------------
//            List<Map<String, Object>> maps = SQLiteHelper.getInstance().mUartDB.queryUserInfo(SQLiteHelper.getInstance().getDBInstance(), null);
//            Logger.d("数据库中数据：" + new Gson().toJson(maps));
            //
        }
    };


    /**
     * 每到16次的时候 检查是否呼气 没有呼气则语音提示
     *
     * @param msg
     */
    private void isBlow(Message msg) {
        blowCount++;
        blowList.add(msg.obj.toString());
        if (blowCount == 16) {
            for (int i = 0; i < blowList.size(); i++) {
                if (!blowList.contains("Blow")) {
                    //吹气播放
                    MediaPlayer mediaPlayer = MediaPlayer.create(EmergencyApplication.getContext(), R.raw.blow);
                    mediaPlayer.start();
                    blowCount = 0;
                    blowList.clear();
                    Log.d("TAG", "handleMessage: timer 吹气吹气吹气吹气吹气");
                }
            }
        }
    }

    /**
     * 呼气  模拟人缩放动画
     */
    private void mmrAnim() {
        try {
            mmr.startAnimation(AnimationUtils.loadAnimation(EmergencyApplication.getContext(), R.anim.mmr_scale));
        } catch (Exception e) {

        }

    }

    /**
     * 每到31次的时候 检查是否呼气 没有呼气则语音提示
     * 每到32次的时候 检查是否呼气 没有呼气则语音提示
     *
     * @param msg
     */
    private void isBlow30(Message msg) {
        blowCount++;
        blowList.add(msg.obj.toString());

        if (blowCount == 31) {
            if (!blowList.get(30).contains("Blow")) {
                Log.e("TAG", "onCreate: 第 " + blowCount + " 次吹气一次");
                //吹气播放
                MediaPlayer mediaPlayer = MediaPlayer.create(EmergencyApplication.getContext(), R.raw.blow);
                mediaPlayer.start();
            }
        }
        if (blowCount == 32) {
            if (!blowList.get(31).contains("Blow")) {
                Log.e("TAG", "onCreate: 第 " + blowCount + " 次吹气一次");
                MediaPlayer mediaPlayer = MediaPlayer.create(EmergencyApplication.getContext(), R.raw.blow);
                mediaPlayer.start();
            }
            blowCount = 0;
            blowList.clear();
        }
    }


    @Override
    public void onDestroy() {

        Log.e(TAG, "onMultiClick:练习模式-- ");

        if (null != mediaPlayer && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (null != animationDrawable) {
            animationDrawable.stop();
        }
        if (null != uartInterface) {
            Log.e(TAG, "onMultiClick:2222-- " + incline);
            mStartRobot.setText("开启模拟人");
            uartInterface.send("<TestStop>");
            chronometer.setFormat("计时结束：%s");
            chronometer.stop();
        }
        if (timer != null) {
            timer.cancel();
        }
        if (myReceiver != null) {
            requireActivity().unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        ((UARTActivity) requireActivity()).setConfigurationListener(null);
        if (uartInterface != null) {
            HttpProvider.doGet(GlobalConstants.APP_BURIED_POINT + Constants.B_BLE_END + "," + bleService.getDeviceAddress(), null);
//            uartInterface.send("<TestStop>");
            uartInterface = null;
        }
        if (serviceConnection != null) {
            requireActivity().unbindService(serviceConnection);
            serviceConnection = null;
        }

        if (bleService != null) {
            try {
                bleService.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        requireActivity().finish();
        super.onDestroy();
    }

}


