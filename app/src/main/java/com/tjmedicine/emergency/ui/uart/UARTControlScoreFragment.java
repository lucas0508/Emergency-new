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
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.dialog.CustomDjsFullScreenPopup;
import com.tjmedicine.emergency.common.dialog.DialogManage;
import com.tjmedicine.emergency.common.dialog.TaskSucDialog;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.model.widget.BGAProgressBar;
import com.tjmedicine.emergency.model.widget.BatteryView;
import com.tjmedicine.emergency.ui.uart.data.presenter.IUARTControlView;
import com.tjmedicine.emergency.ui.uart.data.presenter.PDScoreData;
import com.tjmedicine.emergency.ui.uart.data.presenter.UARTControlPresenter;
import com.tjmedicine.emergency.ui.uart.domain.UartConfiguration;
import com.tjmedicine.emergency.ui.uart.profile.UARTInterface;
import com.tjmedicine.emergency.utils.GsonUtils;
import com.tjmedicine.emergency.utils.LCIMAudioHelper;
import com.tjmedicine.emergency.utils.SimpleCountDownTimer;
import com.tjmedicine.emergency.utils.SoundPlayUtils;
import com.tjmedicine.emergency.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UARTControlScoreFragment extends Fragment implements UARTActivity.ConfigurationListener, IUARTControlView {
    private final static String TAG = "UARTControlScore";
    private final static String SIS_EDIT_MODE = "sis_edit_mode";
    private final static String ACTION_RECEIVE = "no.nordicsemi.android.nrftoolbox.uart.ACTION_RECEIVE";
    private UARTInterface uartInterface;
    private boolean editMode;
    private MyReceiver myReceiver;
    TextView tv_connect, mTime, mHistoricalData, tv_Battery, tv_bga_pdcount, tv_bga_blow;
    BatteryView batteryview;
    Button mStartRobot;
    ImageView iv_setting,mmr;
    List<String> liscount;
    List<String> lisdata;
    private Handler handler;
    private ImageButton ib_close;
    DialogManage mApp;
    private UARTService.UARTBinder bleService;
    private UARTControlPresenter uartControlPresenter = new UARTControlPresenter(this);
    List<String> serverDataList;
    private CustomDjsFullScreenPopup customDjsFullScreenPopup;

    LineChart mChart1;
    private DynamicLineChartManager dynamicLineChartManager1;
    private BGAProgressBar bga_pdcount_progress;
    private BGAProgressBar bga_blow_progress;
    List<PDData> listPD1 = new ArrayList<>();
    //所有数据点组成集合
    List<String> listPD = new ArrayList<>();
    private int g = 0;//吹去次数
    private int j = 0;//按压次数
    List<String> listPD_trough = new ArrayList<>();

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

        //注册广播接收器
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE);
        requireActivity().registerReceiver(myReceiver, intentFilter);
        mApp = new DialogManage(requireActivity());
    }


    public void onServiceStarted() {
        // The service has been started, bind to it
        final Intent service = new Intent(requireActivity(), UARTService.class);
        requireActivity().bindService(service, serviceConnection, 0);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            bleService = (UARTService.UARTBinder) service;
            uartInterface = bleService;
            tv_connect.setText("关闭连接");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            uartInterface = null;
            tv_connect.setText("连接模拟人");
        }
    };


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(SIS_EDIT_MODE, editMode);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_feature_uart_control_score, container, false);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tv_connect = view.findViewById(R.id.action_connect);
        ib_close = view.findViewById(R.id.ib_close);
        mStartRobot = view.findViewById(R.id.start_robot);
        mTime = view.findViewById(R.id.tv_time);
        mHistoricalData = view.findViewById(R.id.tv_historical_data);
        batteryview = view.findViewById(R.id.batteryview);
        tv_Battery = view.findViewById(R.id.tv_Battery);
        mChart1 = view.findViewById(R.id.dynamic_chart1);
        bga_pdcount_progress = view.findViewById(R.id.bga_pdcount_progress);
        bga_blow_progress = view.findViewById(R.id.bga_blow_progress);
        iv_setting = view.findViewById(R.id.iv_setting);
        tv_bga_pdcount = view.findViewById(R.id.tv_bga_pdcount);
        tv_bga_blow = view.findViewById(R.id.tv_bga_blow);
        mmr = view.findViewById(R.id.mmr);
        iv_setting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });
        handler = new Handler();
        new SoundPlayUtils(requireActivity());
        initData();//初始化数据
        initChart();//数据设置
        initListeners();
        return view;
    }

    private void initListeners() {
        mHistoricalData.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(requireActivity(), UARTRobotActivity.class);
                intent.putExtra("", "");
                startActivity(intent);
            }
        });
        mStartRobot.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                startUART();
            }
        });
    }


    /**
     * 开启模拟人
     */
    private void startUART() {
        if (null != uartInterface) {
            mStartRobot.setVisibility(View.GONE);
            customDjsFullScreenPopup = new CustomDjsFullScreenPopup(requireActivity(), new CustomDjsFullScreenPopup.OnMyCompletionListener() {
                @Override
                public void onClick() {
                    customDjsFullScreenPopup.dismiss();
                    //播放完成回调
                    if (null != uartInterface) {
                        uartInterface.send("<TestStart>");
                        serverDataList = new ArrayList<>();
                        listPD1 = new ArrayList<>();
                        listPD_trough = new ArrayList<>();
                        g = 0;
                        j = 0;
                        bga_pdcount_progress.setProgress(j);
                        bga_blow_progress.setProgress(g);
                        initData();
                        startTimer();
                    }
                    liscount = new ArrayList<>();
                }
            });
            new XPopup.Builder(requireActivity()).asCustom(customDjsFullScreenPopup).show();

        } else {
            ToastUtils.showTextToas(requireActivity(), "请连接模拟人后在点击开始测试哦~");
            //startTimer();
        }
    }

    /**
     * 开始倒计时
     */
    private void startTimer() {
        long totalTime = 60000;
//        long totalTime = 20000;
        // 初始化并启动倒计时
        new SimpleCountDownTimer(totalTime, mTime).setOnFinishListener(new SimpleCountDownTimer.OnFinishListener() {
            @Override
            public void onFinish() {
                if (null != uartInterface) {
                    uartInterface.send("<TestStop>");
                    for (int i = 1; i < listPD1.size() - 1; i++) {
                        if (listPD1.get(i).getP_num() == -1) {
                            serverDataList.add("Blow" + "|" + listPD1.get(i).getP_time());
                        } else {
                            if (listPD1.get(i).getP_num() > listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() >= listPD1.get(i + 1).getP_num()) {
                                if (listPD1.get(i).getP_num() > 10) {
//                                bga_pdcount_progress.setProgress(j);
                                    Log.e(TAG, " 最后总次数--maxj-------> " + j);
                                    Log.e(TAG, " --最后总次数---maxj-time---> " + new Gson().toJson(listPD1.get(i)));
                                    serverDataList.add(listPD1.get(i).getP_num() + "|" + listPD1.get(i).getP_time());
                                }

                            }
                            //n-1 =n  n< n+1				n-1<n  n<n+1		  n-1  >n   n=n+1
                            if (listPD1.get(i).getP_num() <= listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() < listPD1.get(i + 1).getP_num()) {
                                Log.e(TAG, " --min-------> " + new Gson().toJson(listPD1.get(i)));
                            }
                        }
                    }
                    bga_pdcount_progress.setProgress(j);
                    bga_blow_progress.setProgress(g);

                    //清除数据
                    //断开模拟人
                    // TODO: 2020-12-23 弹出评分

                    Log.e(TAG, " --传递服务器数据-------> " + new Gson().toJson(serverDataList));
                    uartControlPresenter.postUARTData(serverDataList, "1", listPD_trough);
                }
            }
        }).start();
    }

    /**
     * 按压速度
     */
    private void PDSpeed() {
        String music = "android.resource://" + requireActivity().getPackageName() + "/" + R.raw.music;

        LCIMAudioHelper.getInstance().playAudio(music);
        Log.d("TAG", "handleMessage: timer  lisdata-->" + lisdata.size());
        // 20次/10S
        for (int i = 0; i < lisdata.size(); i++) {
            if (lisdata.get(i).contains("PD")) {
                liscount.add(lisdata.get(i));
            }
        }
        if (liscount.size() < 16) {
            liscount.clear();
            lisdata.clear();
            Log.d("TAG", "handleMessage: timer 按压太慢慢慢了" + liscount.size());
//                ToastUtils.showImageToas(requireActivity(), "按压太慢慢慢了");
            LCIMAudioHelper.getInstance().playAudio(music);
//                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
            //用prepare方法，会报错误java.lang.IllegalStateExceptio
            //mediaPlayer.prepare();
//                mediaPlayer.start();
        } else if (liscount.size() > 20) {
//                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
            //用prepare方法，会报错误java.lang.IllegalStateExceptio
            //mediaPlayer.prepare();
//                mediaPlayer.start();
            LCIMAudioHelper.getInstance().playAudio(music);
            liscount.clear();
            lisdata.clear();
            Log.d("TAG", "handleMessage: timer 按压太快快快了" + liscount.size());
//            ToastUtils.showImageToas(requireActivity(), "按压太快快快了");
        }
    }


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

    @Override
    public void onConfigurationModified() {

    }

    @Override
    public void onConfigurationChanged(@NonNull final UartConfiguration configuration) {
    }


    @Override
    public void postUARTDataSuccess(PDScoreData data) {
        mApp.getLoadingDialog().hide();
        final TaskSucDialog taskSucDialog = new TaskSucDialog(requireActivity());
        Logger.d("数据+++》" + new Gson().toJson(data));
        Logger.d("数据+++》" + data.getBl_pd());
        taskSucDialog.initData(data.getPd(), data.getPd_depth(), data.getBl_pd(), data.getPd_time(), data.getPd_rebound(), data.getResult());
        taskSucDialog.setTaskClose(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskSucDialog.mScaleSpring.setEndValue(1);
                SoundPlayUtils.play(2);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        taskSucDialog.mScaleSpring.setEndValue(0);
                    }
                }, 100);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        taskSucDialog.dismiss();
                        mStartRobot.setVisibility(View.VISIBLE);
                    }
                }, 300);
            }
        });
        taskSucDialog.setTaskRestart(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskSucDialog.dismiss();
                startUART();
            }
        });
    }

    @Override
    public void postUARTDataFail(String info) {
        mApp.shortToast(info);
        mApp.getLoadingDialog().hide();
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra(Intent.EXTRA_TEXT);

//            Map<String, Object> stringObjectMap = new HashMap<>();
//            stringObjectMap.put("id", System.currentTimeMillis());
//            stringObjectMap.put("data", data);
//            SQLiteHelper.getInstance().mUartDB.insertData(SQLiteHelper.getInstance().getDBInstance(), stringObjectMap);
            String s = GsonUtils.returnFormatText2(data);
            // String s1 = GsonUtils.returnStatus(s);//最终要展示的数据
            Message message = Message.obtain();
            message.obj = s;
            mHandler.sendMessage(message);
        }
    }


    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String s = msg.obj.toString();
            Log.e(TAG, "onReceive: 所有数据：---->" + s);
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
                    return;
                }
                assert s != null;
                if (s.startsWith("Battery=")) {
                    String[] split = s.split("=");
                    tv_Battery.setText("当前电量:" + "\n\n" + split[1] + "%");
                    batteryview.setPower(Integer.parseInt(split[1]));
                } else if (s.equals("Blow")) {
                    g++;
                    Log.e(TAG, "onReceive: 吹气：---->" + s);
                    //实时统计吹气次数

                    bga_blow_progress.setProgress(g);
                    tv_bga_blow.setText("人工呼吸" + g + "次");
                    mmrAnim();
                    listPD1.add(new PDData(-1, System.currentTimeMillis()));
                } else if (s.contains("FWVer") || s.contains("HWVer")) {
                    return;
                } else {
                    Log.e(TAG, "onReceive: 显示所有点：---->" + s);
                    String[] split = s.split(",");
                    for (int i = 0; i < split.length; i++) {
                        dynamicLineChartManager1.addEntry(Integer.parseInt(split[i]));
//                        listPD.add(Integer.parseInt(split[i]));
                        listPD.add(Integer.parseInt(split[i]) + "|" + System.currentTimeMillis());
                        PDData pdData = new PDData(Integer.parseInt(split[i]), System.currentTimeMillis());
                        listPD1.add(pdData);
                    }
                }
                //按压次数
                j = 0;
                if (listPD1.size() > 0) {
                    Log.e(TAG, " --实际总次数-----listPD1--> " + listPD1.size());
                    for (int i = 1; i < listPD1.size() - 1; i++) {
                        if (listPD1.get(i).getP_num() > listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() >= listPD1.get(i + 1).getP_num()) {
                            if (listPD1.get(i).getP_num() > 10) {
                                j++;
                                //实时统计按压次数
//                                bga_pdcount_progress.setProgress(j);
                                Log.e(TAG, " 实际总次数--maxj-------> " + j);
                                Log.e(TAG, " --实际总次数---maxj-time---> " + new Gson().toJson(listPD1.get(i)));
                                //serverDataList.add(listPD1.get(i).getP_num()+"|"+listPD1.get(i).getP_time());
                            }


                            Log.e(TAG, " 实际总次数--maxj-------> " + j);
                        }
                        //n-1 =n  n< n+1				n-1<n  n<n+1		  n-1  >n   n=n+1
                        if (listPD1.get(i).getP_num() <= listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() < listPD1.get(i + 1).getP_num()) {
                            Log.e(TAG, " --min-------> " + new Gson().toJson(listPD1.get(i)));
                            listPD_trough.add(listPD1.get(i).getP_num() + "");
                        }
                    }
                    //serverDataList.add(s+"|"+System.currentTimeMillis()); 发送完整数据+时间戳
                }
            }
            //---------本地数据库存储--------------------------
//            List<Map<String, Object>> maps = SQLiteHelper.getInstance().mUartDB.queryUserInfo(SQLiteHelper.getInstance().getDBInstance(), null);
//            Logger.d("数据库中数据：" + new Gson().toJson(maps));
            //
        }
    };


    @Override
    public void onStart() {
        super.onStart();

        /*
         * If the service has not been started before the following lines will not start it. However, if it's running, the Activity will be bound to it
         * and notified via serviceConnection.
         */
        final Intent service = new Intent(getActivity(), UARTService.class);
        requireActivity().bindService(service, serviceConnection, 0); // we pass 0 as a flag so the service will not be created if not exists

    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            requireActivity().unbindService(serviceConnection);
            uartInterface = null;
        } catch (final IllegalArgumentException e) {
            // do nothing, we were not connected to the sensor
        }
    }
    /**
     * 呼气  模拟人缩放动画
     */
    private void mmrAnim() {
        mmr.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.mmr_scale));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((UARTActivity) requireActivity()).setConfigurationListener(null);
        if (myReceiver != null) {
            requireActivity().unregisterReceiver(myReceiver);
        }
        if (uartInterface != null) {
            HttpProvider.doGet(GlobalConstants.APP_BURIED_POINT + Constants.B_BLE_END + "," + bleService.getDeviceAddress(), null);
            uartInterface.send("<TestStop>");
            uartInterface = null;
        }
        if (bleService != null) {
            try {
                bleService.disconnect();
            } catch (Exception e) {

            }
        }
        requireActivity().finish();
    }
}
