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
import android.view.WindowManager;
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
    private String strHWVersion = null;

    private UARTService.UARTBinder bleService;
    private UARTInterface uartInterface;
    private UpdateFW updateFW;
    private MyReceiver myReceiver;
    private UpdateFW.DataCallback  dataCallback ;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //?????????????????????
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
                        .setTitleText("??????")
                        .setContentText("????????????????????????????")
                        .setCancelText("??????")
                        .setConfirmText("??????")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                if (uartInterface != null) {
                                    uartInterface.send("<ZeroPressCal>");
                                }
                                sDialog
                                        .setTitleText("????????????!")
                                        .setConfirmText("??????")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        }).show();
                break;
            case R.id.btn_jz_max:
                new SweetAlertDialog(SettingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("??????")
                        .setContentText("????????????????????????????????????????????????????????????5???????????????")
                        .setConfirmText("??????")
                        .setCancelText("??????")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                mApp.shortToast("?????????????????????");
                                if (uartInterface != null) {
                                    uartInterface.send("<MaxPressCal>");
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sDialog.setTitleText("????????????!")
                                                .setConfirmText("??????")
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

                updateFW = new UpdateFW(SettingActivity.this, strFWVersion,strHWVersion, uartInterface,  dataCallback);

                String s = updateFW.updateRes();
                if (!TextUtils.isEmpty(s)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showTextToas(SettingActivity.this, s);
                        }
                    });
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = new ProgressDialog(SettingActivity.this);
                        dialog.setTitle("????????????");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setMessage("Update FW Start!");
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });



                if (updateFW.isUpdateFW()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            ToastUtils.showTextToas(SettingActivity.this, "FW Update Success!");
                            new SweetAlertDialog(SettingActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("??????????????????")
                                    .setContentText("FW Update Success!???????????????????????????????????????")
                                    .setConfirmText("??????")
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
                            dialog.dismiss();
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
//                    tv_Battery.setText("??????:" + split[1] + "%");
//                    batteryview.setPower(Integer.parseInt(split[1]));
                } else if (s.startsWith("FWVer=")) {
                    String[] split = s.split("=");
                    strFWVersion = split[1];
                    tv_FWVer.setText("???????????????:" + split[1]);
                } else if (s.startsWith("HWVer=")) {
                    String[] split = s.split("=");
                    strHWVersion = split[1];
                    tv_HWVer.setText("???????????????:" + split[1]);
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
