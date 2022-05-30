package com.tjmedicine.emergency.ui.device.steth;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kl.minttisdk.bean.EchoMode;
import com.kl.minttisdk.ble.BleManager;
import com.kl.minttisdk.ble.callback.IAudioDataCallback;
import com.kl.minttisdk.ble.callback.IAudioPacketLossListener;
import com.kl.minttisdk.ble.callback.IBleConnectStatusListener;
import com.kl.minttisdk.ble.callback.IDeviceBatteryListener;
import com.kl.minttisdk.ble.callback.IFirmwareInfoCallback;
import com.kl.minttisdk.ble.callback.IModeSwitchListener;
import com.kl.minttisdk.utils.ArrayUtils;
import com.kl.minttisdk.utils.LogUtils;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.QiNiuUtils;
import com.tjmedicine.emergency.ui.device.view.AudioTrackPlayer;
import com.tjmedicine.emergency.ui.device.view.AudioWaveView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.huawei.hms.framework.common.PackageUtils.getVersionName;

public class StethoscopeActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = StethoscopeActivity.class.getSimpleName();
    @BindView(R.id.tv_title)
    TextView mTitle;
    private TextView tvPower;
    private TextView tvHeartRate;
    private TextView tvMode;
    private TextView tvFirmware;
    private TextView tvSoftware;
    //    private Button btnConnect;
    private Button btnReceiveData;
    private Button btnModeSwitch;
    private Button btnFirmwateUpdate;
    private String bleAddress;
    private File rootFile;
    private File pcmFile;
    private FileOutputStream fos;
    private AudioTrackPlayer audioTrackPlayer;
    private Button btnReadPower;
    private AudioWaveView waveView;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

//


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_stethoscope;
    }

    @Override
    protected void initView() {

        mTitle.setText("详情");
        bleAddress = getIntent().getStringExtra("BLE_ADDRESS");
        initViews();
        BleManager.getInstance().addConnectionListener(bleConnectStatusListener);
        BleManager.getInstance().setAudioDataCallback(audioDataCallback);
        BleManager.getInstance().notifyBattery(deviceBatteryListener);
        BleManager.getInstance().readBattery(deviceBatteryListener);
        BleManager.getInstance().notifyModeSwitch(modeSwitchListener);
        BleManager.getInstance().setPacketLossListener(packetLossListener);
        BleManager.getInstance().readFirmwareInfo(firmwareInfoCallback);
        rootFile = new File(Environment.getExternalStorageDirectory(), "121emergency-audio");
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        audioTrackPlayer = new AudioTrackPlayer();
        bleAddress = getIntent().getStringExtra("BLE_ADDRESS");

    }

    private void initViews() {
        tvPower = findViewById(R.id.tv_power);
        tvHeartRate = findViewById(R.id.tv_heart_rate);
        tvMode = findViewById(R.id.tv_echo_mode);
//        btnConnect = findViewById(R.id.btn_connect);
        btnReceiveData = findViewById(R.id.btn_receive_data);
        btnModeSwitch = findViewById(R.id.btn_echo_mode);
//        btnFirmwateUpdate = findViewById(R.id.btn_firmware_update);
        tvFirmware = findViewById(R.id.tv_firmware);
        tvSoftware = findViewById(R.id.tv_software);
//        btnFirmwateUpdate.setOnClickListener(this);
//        btnConnect.setOnClickListener(this);
        btnReceiveData.setOnClickListener(this);
        btnModeSwitch.setOnClickListener(this);
        tvPower.setText("0%");
        tvHeartRate.setText("0 BPM");
        tvMode.setText("钟型");
        waveView = findViewById(R.id.wave_view);
        btnReadPower = findViewById(R.id.btn_read_power);
        btnReadPower.setOnClickListener(this);
        tvSoftware.setText(" " + getVersionName(this));
    }

    private final IDeviceBatteryListener deviceBatteryListener = new IDeviceBatteryListener() {
        @Override
        public void onBattery(int battery) {
            mMainHandler.post(() -> {
                tvPower.setText(battery + "%");
            });

        }
    };
    private final IModeSwitchListener modeSwitchListener = new IModeSwitchListener() {
        @Override
        public void onModeSwitch(EchoMode echoMode) {
            Log.d(TAG, "onModeSwitch = " + echoMode.name());
            mMainHandler.post(() -> {
                if (echoMode == EchoMode.MODE_BELL_ECHO) {
                    tvMode.setText("钟型");
                } else {
                    tvMode.setText("膜型");
                }

            });
        }
    };

    private final IAudioDataCallback audioDataCallback = new IAudioDataCallback() {
        @Override
        public void onSpkData(short[] shorts) {

        }

        @Override
        public void onMicData(short[] shorts) {

        }

        @Override
        public void onProcessData(short[] processData) {
            if (audioTrackPlayer != null) {
                audioTrackPlayer.writeData(processData);
                waveView.addWaveData(processData.clone());
            }
            try {
                if (fos != null) {

                    byte[] data = ArrayUtils.short2ByteArray(processData);
                    fos.write(data, 0, data.length);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onHeartRate(int heartRate) {
            mMainHandler.post(() -> tvHeartRate.setText(heartRate + " BPM"));
        }
    };

    private final IAudioPacketLossListener packetLossListener = new IAudioPacketLossListener() {
        @Override
        public void onPacketLoss() {
            mMainHandler.post(() -> {
                mApp.longToast("数据丢失，请靠近听诊器设备");
            });
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_receive_data:
                if (!BleManager.getInstance().isConnected()) {
                    mApp.shortToast("设备未连接");
                    return;
                }
                if (!btnReceiveData.isSelected()) {
                    btnReceiveData.setSelected(true);
                    btnReceiveData.setText("停止接收数据");
                    createFile();
                    audioTrackPlayer.play();
                    BleManager.getInstance().notifyAudioData();

                } else {
                    btnReceiveData.setSelected(false);
                    btnReceiveData.setText("开始接收数据");
                    audioTrackPlayer.stop();
                    closeFile();
                    //上传骑七牛云
                    QiNiuUtils.getInstance().upload(pcmFile.getAbsolutePath(), new QiNiuUtils.UploadResult() {
                        @Override
                        public void success(String path) {
                            mApp.getLoadingDialog().hide();
                            Map<String, Object> map = new HashMap<>();
                            map.put("auscultaUrl", path);
                            map.put("type", tvMode.getText().toString().trim());
                            HttpProvider.doPost(GlobalConstants.APP_USER_ME_STUDY_LUNGAUSCULTA, map, null);
                            Logger.d(path);
                        }

                        @Override
                        public void fail() {
                            mApp.getLoadingDialog().hide();
                        }
                    });

                    BleManager.getInstance().unNotifyAudioData();
                    waveView.clearDatas();
                }
                break;
            case R.id.btn_echo_mode:
                if (!BleManager.getInstance().isConnected()) {
                    mApp.shortToast("设备未连接");
                    return;
                }
                if (!btnModeSwitch.isSelected()) {
                    btnModeSwitch.setSelected(true);
                    BleManager.getInstance().setEchoModeSwitch(EchoMode.MODE_DIAPHRAGM_ECHO);
                } else {
                    btnModeSwitch.setSelected(false);
                    BleManager.getInstance().setEchoModeSwitch(EchoMode.MODE_BELL_ECHO);
                }

                break;
//            case R.id.btn_firmware_update:
//                Intent intent = new Intent(this, FirmwareListActivity.class);
//                intent.putExtra("BLE_ADDRESS", bleAddress);
//                startActivity(intent);
//                break;
            case R.id.btn_read_power:
                BleManager.getInstance().readBattery(new IDeviceBatteryListener() {
                    @Override
                    public void onBattery(int i) {
                        tvPower.setText(i + "%");
                    }
                });
                break;
        }
    }

    private void createFile() {
        try {
            pcmFile = new File(rootFile, System.currentTimeMillis() + ".pcm");
            Logger.d(pcmFile.getAbsolutePath());
            fos = new FileOutputStream(pcmFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeFile() {
        try {
            if (fos != null) {
                fos.close();
                fos = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final IBleConnectStatusListener bleConnectStatusListener = new IBleConnectStatusListener() {
        @Override
        public void onConnectFail(String s, int i) {

        }

        @Override
        public void onConnectSuccess(String s) {

        }

        @Override
        public void onUpdateParamsSuccess() {

        }

        @Override
        public void onUpdateParamsFail() {

        }

        @Override
        public void onDisConnected(String s, boolean isActive, int i) {
            if (!isActive) {//是否主动断开连接
                MessageDialog.show("提示", "蓝牙已断开连接").setCancelable(false)
                        .setOkButton("确认", new OnDialogButtonClickListener<MessageDialog>() {
                            @Override
                            public boolean onClick(MessageDialog messageDialog, View view) {
                                BleManager.getInstance().disconnect();
                                finish();
                                return false;
                            }
                        });

            }

        }
    };


    private final IFirmwareInfoCallback firmwareInfoCallback = new IFirmwareInfoCallback() {
        @Override
        public void onFirmwareInfo(String s) {
            LogUtils.d("MeasureActivity", "onFirmwareInfo: " + s);
            mMainHandler.post(() -> {
                tvFirmware.setText(" " + s);
            });
        }
    };

    @Override
    public void onBackPressed() {
        if (BleManager.getInstance().isConnected()) {
            MessageDialog.show("提示", "是否断开连接")
                    .setOkButton("确认", new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog messageDialog, View view) {
                            BleManager.getInstance().disconnect();
                            finish();
                            return false;
                        }
                    }).setCancelButton("取消");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().removeConnectionListener(bleConnectStatusListener);
    }


}
