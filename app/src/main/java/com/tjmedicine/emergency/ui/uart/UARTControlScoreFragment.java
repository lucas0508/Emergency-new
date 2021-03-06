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
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.tjmedicine.emergency.EmergencyApplication;
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
import com.tjmedicine.emergency.ui.main.MainActivity;
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
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UARTControlScoreFragment extends Fragment implements UARTActivity.ConfigurationListener, IUARTControlView {
    private final static String TAG = "UARTControlScore";
    private final static String SIS_EDIT_MODE = "sis_edit_mode";
    private final static String ACTION_RECEIVE = "no.nordicsemi.android.nrftoolbox.uart.ACTION_RECEIVE";
    private UARTInterface uartInterface;
    private boolean editMode;
    private MyReceiver myReceiver;
    TextView tv_connect, mTime, mHistoricalData, tv_Battery, tv_bga_pdcount, tv_bga_blow, tv_bga_count;
    BatteryView batteryview;
    Button mStartRobot;
    ImageView iv_setting, mmr;
    List<String> liscount;
    List<String> lisdata;
    private Handler handler;
    private ImageButton ib_closeceshi;
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
    //???????????????????????????
    List<String> listPD = new ArrayList<>();
    private int g = 0;//????????????
    private int j = 0;//????????????
    List<String> listPD_trough = new ArrayList<>();
    private CountDownTimer timer;

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

        //?????????????????????
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
            tv_connect.setText("????????????");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            uartInterface = null;
            tv_connect.setText("???????????????");
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
        ib_closeceshi = view.findViewById(R.id.ib_closeceshi);
        mStartRobot = view.findViewById(R.id.start_robot);
        mTime = view.findViewById(R.id.tv_time);
        tv_bga_count = view.findViewById(R.id.tv_bga_count);
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
        initData();//???????????????
        initChart();//????????????
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

        ib_closeceshi.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (uartInterface != null) {
                    HttpProvider.doGet(GlobalConstants.APP_BURIED_POINT + Constants.B_BLE_END + "," + bleService.getDeviceAddress(), null);
                    uartInterface.send("<TestStop>");
                    uartInterface = null;
                }
                ((UARTActivity) requireActivity()).setConfigurationListener(null);
                if (myReceiver != null) {
                    requireActivity().unregisterReceiver(myReceiver);
                    myReceiver=null;
                }
                if (timer != null) {
                    timer.cancel();
                    timer.onFinish();
                    timer = null;
                }

                if (bleService != null) {
                    try {
                        bleService.disconnect();
                    } catch (Exception e) {
                    }
                }
                requireActivity().finish();
            }
        });
    }


    /**
     * ???????????????
     */
    private void startUART() {
        if (null != uartInterface) {
            mStartRobot.setVisibility(View.GONE);
            customDjsFullScreenPopup = new CustomDjsFullScreenPopup(requireActivity(), new CustomDjsFullScreenPopup.OnMyCompletionListener() {
                @Override
                public void onClick() {
                    customDjsFullScreenPopup.dismiss();
                    //??????????????????
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
            ToastUtils.showTextToas(requireActivity(), "?????????????????????????????????????????????~");
            //startTimer();
        }
    }


    /**
     * ???????????????
     */
    private void startTimer() {
        long totalTime = 60000;
//        long totalTime = 30000;
        // ???????????????????????????
        timer = new SimpleCountDownTimer(totalTime, mTime).setOnFinishListener(new SimpleCountDownTimer.OnFinishListener() {
            @Override
            public void onFinish() {
                if (null != uartInterface) {
                    uartInterface.send("<TestStop>");
                    for (int i = 1; i < listPD1.size() - 1; i++) {
                        if (listPD1.get(i).getP_name() == -1) {
                            serverDataList.add("Blow" + "|" + listPD1.get(i).getP_num() + "|" + listPD1.get(i).getP_time());
                        } else {
                            if (listPD1.get(i).getP_num() > listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() >= listPD1.get(i + 1).getP_num()) {
                                if (listPD1.get(i).getP_num() > 10) {
//                                bga_pdcount_progress.setProgress(j);
                                    Log.e(TAG, " ???????????????--maxj-------> " + j);
                                    Log.e(TAG, " --???????????????---maxj-time---> " + new Gson().toJson(listPD1.get(i)));
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

                    //????????????
                    //???????????????
                    // TODO: 2020-12-23 ????????????
                    Log.e(TAG, " --?????????????????????-------> " + new Gson().toJson(serverDataList));
                    uartControlPresenter.postUARTData(serverDataList, "1", listPD_trough);
                }
            }
        });
        timer.start();
    }

    /**
     * ????????????
     */
    private void PDSpeed() {
        String music = "android.resource://" + requireActivity().getPackageName() + "/" + R.raw.music;

        LCIMAudioHelper.getInstance().playAudio(music);
        Log.d("TAG", "handleMessage: timer  lisdata-->" + lisdata.size());
        // 20???/10S
        for (int i = 0; i < lisdata.size(); i++) {
            if (lisdata.get(i).contains("PD")) {
                liscount.add(lisdata.get(i));
            }
        }
        if (liscount.size() < 16) {
            liscount.clear();
            lisdata.clear();
            Log.d("TAG", "handleMessage: timer ?????????????????????" + liscount.size());
//                ToastUtils.showImageToas(requireActivity(), "?????????????????????");
            LCIMAudioHelper.getInstance().playAudio(music);
//                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
            //???prepare?????????????????????java.lang.IllegalStateExceptio
            //mediaPlayer.prepare();
//                mediaPlayer.start();
        } else if (liscount.size() > 20) {
//                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
            //???prepare?????????????????????java.lang.IllegalStateExceptio
            //mediaPlayer.prepare();
//                mediaPlayer.start();
            LCIMAudioHelper.getInstance().playAudio(music);
            liscount.clear();
            lisdata.clear();
            Log.d("TAG", "handleMessage: timer ?????????????????????" + liscount.size());
//            ToastUtils.showImageToas(requireActivity(), "?????????????????????");
        }
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

                updateFW = new UpdateFW(getActivity(), strFWVersion, strHWVersion, uartInterface, dataCallback);

                String s = updateFW.updateRes();
                if (!TextUtils.isEmpty(s)) {

                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = new ProgressDialog(getActivity());
                        dialog.setTitle("????????????");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setMessage("Update FW Start!");
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });


                if (updateFW.isUpdateFW()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            ToastUtils.showTextToas(getActivity(), "FW Update Success!");
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("??????????????????")
                                    .setContentText("FW Update Success!???????????????????????????????????????")
                                    .setConfirmText("??????")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }).show();
                        }
                    });
                    Log.e(TAG, "FW Update Success!");
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            dialog.setMessage("FW Update Failed!");
                            ToastUtils.showTextToas(getActivity(), "FW Update Failed!");
                            dialog.dismiss();
                        }
                    });
                    Log.e(TAG, "FW Update Failed!");
                }
            }
        }.start();
    }

    private void initChart() {
        LineData lineData = new LineData();// //???????????????
        mChart1.setData(lineData);//????????????????????????lineChart???
        mChart1.invalidate();//??????
    }


    private void initData() {
        //????????????
        List<String> names = new ArrayList();
        List<Integer> colour = new ArrayList();
        List<Integer> list = new ArrayList<>();
        names.add("??????");
        names.add("??????");
        names.add("??????");
        //????????????
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
        Logger.d("??????+++???" + new Gson().toJson(data));
        Logger.d("??????+++???" + data.getBl_pd());
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
            // String s1 = GsonUtils.returnStatus(s);//????????????????????????
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
            Log.e(TAG, "onReceive: ???????????????---->" + s);
            if (!TextUtils.isEmpty(s)) {
                if (s.equals("OK")) {
                    return;
                }
                if (s.startsWith("Errno=")) {
                    /**
                     * 1	???????????????
                     * 2	????????????
                     * 3	????????????
                     * 11	????????????
                     * 12	????????????????????????
                     * 13	???????????????
                     */
                    String errorMsg = "";
                    String[] split = s.split("=");
                    String[] split1 = split[1].split("\\|");
                    if (split1[0].equals("1")) {
                        errorMsg = "???????????????";
                    } else if (split1[0].equals("2")) {
                        errorMsg = "????????????";
                    } else if (split1[0].equals("3")) {
                        errorMsg = "????????????";
                    } else if (split1[0].equals("11")) {
                        errorMsg = "????????????";
                    } else if (split1[0].equals("12")) {
                        errorMsg = "????????????????????????,??????????????????????????????";
                    } else if (split1[0].equals("13")) {
                        errorMsg = "???????????????";
                    }
                    mApp.longToast("????????? code: " + split1[0] + ",????????????msg:" + errorMsg);
                    return;
                }
                assert s != null;
                if (s.startsWith("Battery=")) {
                    String[] split = s.split("=");
                    tv_Battery.setText("????????????:" + "\n\n" + split[1] + "%");
                    batteryview.setPower(Integer.parseInt(split[1]));
                } else if (s.startsWith("Blow=")) {
                    g++;
                    String[] split = s.split("=");
                    Log.e(TAG, "onReceive: ?????????---->" + s);
                    //????????????????????????

                    bga_blow_progress.setProgress(g);
                    tv_bga_blow.setText("????????????" + g + "???");
                    //?????????????????????
                    tv_bga_count.setText("?????????" + split[1] + "ml");
                    mmrAnim();
                    listPD1.add(new PDData(-1, Integer.parseInt(split[1]), System.currentTimeMillis()));
                } else if (s.contains("FWVer")) {
                    String[] split = s.split("=");
                    strFWVersion = split[1];
                    isUpdataFW();
                } else if (s.contains("HWVer")) {
                    String[] split = s.split("=");
                    strHWVersion = split[1];
                } else {
                    if (!s.contains("Blow=")) {
                        Log.e(TAG, "onReceive: ??????????????????---->" + s);
                        String[] split = s.split(",");
                        for (int i = 0; i < split.length; i++) {
                            dynamicLineChartManager1.addEntry(Integer.parseInt(split[i]));
                            listPD.add(Integer.parseInt(split[i]) + "|" + System.currentTimeMillis());
                            PDData pdData = new PDData(1, Integer.parseInt(split[i]), System.currentTimeMillis());
                            listPD1.add(pdData);
                        }
                    }
                }
                //????????????
                j = 0;
                if (listPD1.size() > 0) {
                    Log.e(TAG, " --???????????????-----listPD1--> " + listPD1.size());
                    for (int i = 1; i < listPD1.size() - 1; i++) {
                        if (listPD1.get(i).getP_name() == 1) {
                            if (listPD1.get(i).getP_num() > listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() >= listPD1.get(i + 1).getP_num()) {
                                if (listPD1.get(i).getP_num() > 10) {
                                    j++;
                                    //????????????????????????

                                    bga_pdcount_progress.setProgress(j);
                                    tv_bga_pdcount.setText("????????????" + j + "???");
                                    Log.e(TAG, " ???????????????--maxj-------> " + j);
                                    Log.e(TAG, " --???????????????---maxj-time---> " + new Gson().toJson(listPD1.get(i)));
                                    //serverDataList.add(listPD1.get(i).getP_num()+"|"+listPD1.get(i).getP_time());
                                }
                                Log.e(TAG, " ???????????????--maxj-------> " + j);
                            }
                            //n-1 =n  n< n+1				n-1<n  n<n+1		  n-1  >n   n=n+1
                            if (listPD1.get(i).getP_num() <= listPD1.get(i - 1).getP_num() && listPD1.get(i).getP_num() < listPD1.get(i + 1).getP_num()) {
                                Log.e(TAG, " --min-------> " + new Gson().toJson(listPD1.get(i)));
                                listPD_trough.add(listPD1.get(i).getP_num() + "");
                            }
                        }
                    }
                    //serverDataList.add(s+"|"+System.currentTimeMillis()); ??????????????????+?????????
                }
            }
            //---------?????????????????????--------------------------
//            List<Map<String, Object>> maps = SQLiteHelper.getInstance().mUartDB.queryUserInfo(SQLiteHelper.getInstance().getDBInstance(), null);
//            Logger.d("?????????????????????" + new Gson().toJson(maps));
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
     * ??????  ?????????????????????
     */
    private void mmrAnim() {
        try {
            mmr.startAnimation(AnimationUtils.loadAnimation(EmergencyApplication.getContext(), R.anim.mmr_scale));
        }catch (Exception e){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (uartInterface != null) {
            HttpProvider.doGet(GlobalConstants.APP_BURIED_POINT + Constants.B_BLE_END + "," + bleService.getDeviceAddress(), null);
            uartInterface.send("<TestStop>");
            uartInterface = null;
        }
        ((UARTActivity) requireActivity()).setConfigurationListener(null);
        if (myReceiver != null) {
            requireActivity().unregisterReceiver(myReceiver);
            myReceiver=null;
        }
        if (timer != null) {
            timer.cancel();
            timer.onFinish();
            timer = null;
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








