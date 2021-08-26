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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.lxj.xpopup.XPopup;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.dialog.CustomDjsFullScreenPopup;
import com.tjmedicine.emergency.common.dialog.DialogManage;
import com.tjmedicine.emergency.common.dialog.TaskSucDialog;
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
import java.util.List;

public class UARTControlExamFragment extends Fragment implements UARTActivity.ConfigurationListener, IUARTControlView {



    private final static String TAG = "UARTControlScore";
    private final static String SIS_EDIT_MODE = "sis_edit_mode";
    private final static String ACTION_RECEIVE = "no.nordicsemi.android.nrftoolbox.uart.ACTION_RECEIVE";
    private UARTInterface uartInterface;
    private boolean editMode;
    private MyReceiver myReceiver;
    TextView tv_connect, mTime, tv_notice,mHistoricalData,tv_Battery;
    BatteryView batteryview;
    Button mStartRobot;
    ImageView iv_setting;
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
    //所有数据点组成集合
    List<Integer> listPD = new ArrayList<>();
    private int g = 0;//吹去次数
    private int j = 0;//按压次数
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

        final View view = inflater.inflate(R.layout.fragment_feature_uart_control_exam, container, false);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tv_connect = view.findViewById(R.id.action_connect);
        ib_close = view.findViewById(R.id.ib_close);
        mStartRobot = view.findViewById(R.id.start_robot);
        mTime = view.findViewById(R.id.tv_time);
        mHistoricalData = view.findViewById(R.id.tv_historical_data);
        batteryview = view.findViewById(R.id.batteryview);
        tv_Battery = view.findViewById(R.id.tv_Battery);
        mChart1 = view.findViewById(R.id.dynamic_chart1);
        iv_setting=view.findViewById(R.id.iv_setting);
        iv_setting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),SettingActivity.class);
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
                Intent intent = new Intent(requireActivity(),UARTRobotActivity.class);
                intent.putExtra("","");
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
            tv_notice.setVisibility(View.GONE);
            customDjsFullScreenPopup = new CustomDjsFullScreenPopup(requireActivity(), new CustomDjsFullScreenPopup.OnMyCompletionListener() {
                @Override
                public void onClick() {
                    customDjsFullScreenPopup.dismiss();
                    //播放完成回调
                    if (null != uartInterface) {
                        uartInterface.send("<TestStart>");
                        serverDataList = new ArrayList<>();
                        listPD1 = new ArrayList<>();
                        g = 0;
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
        long totalTime = 6000;
        // 初始化并启动倒计时
        new SimpleCountDownTimer(totalTime, mTime).setOnFinishListener(new SimpleCountDownTimer.OnFinishListener() {
            @Override
            public void onFinish() {

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

                bga_pdcount_progress.setProgress(j);
                bga_blow_progress.setProgress(g);

                //清除数据
                //断开模拟人
                // TODO: 2020-12-23 弹出评分
//                uartControlPresenter.postUARTData(serverDataList, "1");
                if (null != uartInterface) {
                    uartInterface.send("<TestStop>");
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

        dynamicLineChartManager1.setYAxis(130, 0, 10);

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
    public void postUARTDataSuccess(PDScoreData pdScoreData) {
        mApp.getLoadingDialog().hide();
        final TaskSucDialog taskSucDialog = new TaskSucDialog(requireActivity());
        //taskSucDialog.initData("110","80%","符合标准","合格","还需加油，多加练习，再接再厉哦！");
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
                startUART();
            }
        });
    }

    @Override
    public void postUARTDataFail(String info) {
        mApp.shortToast(info);
//        mApp.getLoadingDialog().hide();
//        final TaskSucDialog taskSucDialog = new TaskSucDialog(requireActivity());
//        taskSucDialog.initData("110","80%","符合标准","合格","还需加油，多加练习，再接再厉哦！");
//        taskSucDialog.setTaskClose(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                taskSucDialog.mScaleSpring.setEndValue(1);
//                SoundPlayUtils.play(2);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        taskSucDialog.mScaleSpring.setEndValue(0);
//                    }
//                }, 100);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        taskSucDialog.dismiss();
//                        mStartRobot.setVisibility(View.VISIBLE);
//                    }
//                }, 300);
//            }
//        });
//        taskSucDialog.setTaskRestart(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startUART();
//            }
//        });
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra(Intent.EXTRA_TEXT);

//            Map<String, Object> stringObjectMap = new HashMap<>();
//            stringObjectMap.put("id", System.currentTimeMillis());
//            stringObjectMap.put("data", data);
//            SQLiteHelper.getInstance().mUartDB.insertData(SQLiteHelper.getInstance().getDBInstance(), stringObjectMap);
            String s = GsonUtils.returnFormatText(data);
            // String s1 = GsonUtils.returnStatus(s);//最终要展示的数据
            Message message = Message.obtain();
            message.obj = s;
            mHandler.sendMessage(message);
        }
    }




    List<Integer> listPD1 = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String s = msg.obj.toString();
            if (!TextUtils.isEmpty(s)) {
                assert s != null;
                if (s.startsWith("Battery=")) {
                    String[] split = s.split("=");
                    tv_Battery.setText("当前电量:" + "\n\n" + split[1] + "%");
                    batteryview.setPower(Integer.parseInt(split[1]));
                } else if (s.equals("Blow")) {
                    g++;
                    Log.e(TAG, "onReceive: 吹气：---->" + s);
                    //bga_blow_progress.setProgress(g);
                } else {
                    String[] split = s.split(",");
                    for (int i = 0; i < split.length; i++) {
                        dynamicLineChartManager1.addEntry(Integer.parseInt(split[i]));
                        listPD.add(Integer.parseInt(split[i]));
                        listPD1.addAll(Collections.singleton(Integer.parseInt(split[i])));
                    }
                }
                //按压次数
                j = 0;
                if (listPD1.size() > 0) {
                    for (int i = 1; i < listPD1.size() - 1; i++) {
                        if (listPD1.get(i) > listPD1.get(i - 1) && listPD1.get(i) >= listPD1.get(i + 1)) {
                            if (listPD1.get(i) > 30) {
                                j++;
                            }
                        }
                        //n-1 =n  n< n+1				n-1<n  n<n+1		  n-1  >n   n=n+1
                        if (listPD1.get(i) <= listPD1.get(i - 1) && listPD1.get(i) < listPD1.get(i + 1)) {
                            Log.e(TAG, " --min-------> " + listPD1.get(i));
                        }

                    }

                }
            }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((UARTActivity) requireActivity()).setConfigurationListener(null);
        if (myReceiver != null) {
            requireActivity().unregisterReceiver(myReceiver);
        }
        if (uartInterface != null) {
            uartInterface.send("<TestStop>");
            uartInterface = null;
        }
        if (bleService != null) {
            try {
                bleService.disconnect();
            }catch (Exception e){
            }
        }
    }
}
