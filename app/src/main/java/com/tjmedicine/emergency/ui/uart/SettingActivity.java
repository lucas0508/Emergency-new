package com.tjmedicine.emergency.ui.uart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.lxj.xpopup.XPopup;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.ui.main.MainActivity;
import com.tjmedicine.emergency.ui.uart.profile.UARTInterface;
import com.tjmedicine.emergency.utils.GsonUtils;
import com.tjmedicine.emergency.utils.ToastUtils;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.tjmedicine.emergency.R.id.ll_uart_update;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ProgressDialog dialog;
    private String TAG = "SettingActivity";
    private final static String ACTION_RECEIVE = "no.nordicsemi.android.nrftoolbox.uart.ACTION_RECEIVE";

    @BindView(R.id.btn_jz_min)
    Button btn_jz_min;

    @BindView(R.id.btn_jz_max)
    Button btn_jz_max;

    @BindView(R.id.ll_uart_update)
    LinearLayout ll_uart_update;


    @BindView(R.id.tv_FWVer)
    TextView tv_FWVer;


    @BindView(R.id.tv_HWVer)
    TextView tv_HWVer;


    private String strFWVersion = null;
    private UARTService.UARTBinder bleService;
    private UARTInterface uartInterface;
    private UpdateFW updateFW;
    private MyReceiver myReceiver;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

        //注册广播接收器
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE);
        registerReceiver(myReceiver, intentFilter);
        btn_jz_min.setOnClickListener(this);
        btn_jz_max.setOnClickListener(this);
        ll_uart_update.setOnClickListener(this);


    }

    /**
     * Method called when user selected a device on the scanner dialog after the service has been started.
     * Here we may bind this fragment to it.
     */
    public void onServiceStarted() {
        // The service has been started, bind to it
        final Intent service = new Intent(this, UARTService.class);
        bindService(service, serviceConnection, 0);
    }

    @Override
    public void onStart() {
        super.onStart();

        /*
         * If the service has not been started before the following lines will not start it. However, if it's running, the Activity will be bound to it
         * and notified via serviceConnection.
         */
        final Intent service = new Intent(this, UARTService.class);
        bindService(service, serviceConnection, 0); // we pass 0 as a flag so the service will not be created if not exists


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_jz_min:
                new SweetAlertDialog(SettingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确定使用零点校准吗?")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                if (uartInterface != null) {
                                    uartInterface.send("<ZeroPressCal>");
                                }
                                sDialog
                                        .setTitleText("校准完成!")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        }).show();
                break;
            case R.id.btn_jz_max:
                new SweetAlertDialog(SettingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("使用最大校准之前，首先把模拟人放平，按压5秒之后松开")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                mApp.shortToast("校准中······");
                                if (uartInterface != null) {
                                    uartInterface.send("<MaxPressCal>");
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sDialog.setTitleText("校准完成!")
                                                .setConfirmText("确定")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                }, 7000);

                            }
                        }).show();
                break;

            case R.id.ll_uart_update:
                updateUART();
                break;
        }
    }

    private void updateUART() {
        dialog = new ProgressDialog(SettingActivity.this);
        dialog.setTitle("下载提示");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("Update FW Start!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Thread() {
            @Override
            public void run() {
                Log.e(TAG, "Update FW Start!");
                updateFW = new UpdateFW(SettingActivity.this, strFWVersion, uartInterface, progress -> runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setProgress(progress);
                    }
                }));
                if (updateFW.isUpdateFW()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            ToastUtils.showTextToas(SettingActivity.this, "FW Update Success!");
                            new SweetAlertDialog(SettingActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("版本升级成功")
                                    .setContentText("FW Update Success!请重新开启模拟人并连接手机")
                                    .setConfirmText("确认")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(MainActivity.class);
                                            finish();
                                        }
                                    }).show();
                        }
                    });
                    Log.e(TAG, "FW Update Success!");
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            dialog.setMessage("FW Update Failed!");
                            ToastUtils.showTextToas(SettingActivity.this, "FW Update Failed!");
                            //dialog.dismiss();
                        }
                    });
                    Log.e(TAG, "FW Update Failed!");
                }
            }
        }.start();
    }

    public void setData(int progress) {
        dialog.setProgress(progress);
    }


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
                    return;
                }
                assert s != null;
                if (s.startsWith("Battery=")) {
                    String[] split = s.split("=");
//                    tv_Battery.setText("电量:" + split[1] + "%");
//                    batteryview.setPower(Integer.parseInt(split[1]));
                } else if (s.startsWith("FWVer=")) {
                    String[] split = s.split("=");
                    strFWVersion = split[1];
                    tv_FWVer.setText("软件版本号:" + split[1]);
                } else if (s.startsWith("HWVer=")) {
                    String[] split = s.split("=");
                    tv_HWVer.setText("硬件版本号:" + split[1]);
                }
            }
        }
    };


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.e(TAG, " --onReceive:-------> " + data);
            String s = GsonUtils.returnFormatText2(data.trim());

            Message message = Message.obtain();
            message.obj = s;
            mHandler.sendMessage(message);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            bleService = (UARTService.UARTBinder) service;
            uartInterface = bleService;
            if (uartInterface != null) {
                uartInterface.send("<Battery?>");
                uartInterface.send("<FWVer?>");
                uartInterface.send("<HWVer?>");
            }


            Log.e(TAG, " --UARTControlFragment->" + bleService.isConnected());
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            uartInterface = null;
        }
    };


}
