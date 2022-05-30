package com.tjmedicine.emergency.ui.device;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.reflect.TypeToken;
import com.helowin.ecg.sdk.util.EcgSdk;
import com.kl.minttisdk.ble.BleManager;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.OpenidBean;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.HttpsUtils;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.device.ecg.EcgMainActivity;
import com.tjmedicine.emergency.ui.device.ecg.ceshi;
import com.tjmedicine.emergency.ui.device.ecg.ceshiBean;
import com.tjmedicine.emergency.ui.device.steth.ScanFragment;
import com.tjmedicine.emergency.ui.main.MainActivity;
import com.tjmedicine.emergency.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;

public class DeviceActivity extends BaseActivity implements View.OnClickListener {
    protected final static UUID UART_SERVICE_UUID = UUID.fromString("00000001-0000-1000-8000-00805f9b34fb");
    ScanFragment dialog;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.ll_recorder)
    LinearLayout recorder;
    @BindView(R.id.ll_stethoscope)
    LinearLayout stethoscope;
    private static final int REQUEST_ENABLE_BT = 1;
    public String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    public List<String> permissionList = new ArrayList<String>();
    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_device;
    }

    @Override
    protected void initView() {
        mTitle.setText("我的设备");
        checkPermission();
        stethoscope.setOnClickListener(this);
        recorder.setOnClickListener(this);
        //TODO
        ceshi ceshi = new ceshi();

        ceshi.querySSData(null, new IBaseModel.OnCallbackListener() {
            @Override
            public void callback(ResponseEntity res) {
                if (HttpProvider.isSuccessful(res.getCode())) {
                    ceshiBean data = (ceshiBean) res.getData();
                    Logger.d("数据源："+data.getList());
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_recorder:
                onBtnxindian();
                break;
            case R.id.ll_stethoscope:
                onBtnScanClicked();
                break;
        }
    }

    private void onBtnxindian() {
        HttpProvider.doGet(GlobalConstants.APP_USER_ME_STUDY_GETREMOTEECG, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                Log.e("----------", "callback: "+responseText );
                ResponseEntity<OpenidBean> res = GsonUtils.parseJsonWithClass(responseText,
                        new TypeToken<ResponseEntity<OpenidBean>>() {
                        }.getType());
                if (res.getCode() == 200) {
                    OpenidBean aa =(OpenidBean)res.getData();
                    EcgSdk.Companion.getInstance().setUserId(aa.getOpenId());
                    Bundle bundle = new Bundle();
                    bundle.putString("openid",aa.getOpenId());
                    bundle.putString("uid",String.valueOf(aa.getUserId()));
                    startActivity(EcgMainActivity.class,bundle);
                } else {

                }
            }
        });
    }


    private void onBtnScanClicked(){
        if (!BleManager.getInstance().isSupportBle()){
            Toast.makeText(this,"Ble is not supported",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = ScanFragment.getInstance(UART_SERVICE_UUID);
        dialog.show(getSupportFragmentManager(), "scan_fragment");
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(DeviceActivity.this, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (!permissionList.isEmpty()) {
                String[] requestPermission = permissionList.toArray(new String[permissionList
                        .size()]);
                ActivityCompat.requestPermissions(DeviceActivity.this, requestPermission, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        Toast.makeText(DeviceActivity.this, "Deny permission program will not run", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK){
            onBtnScanClicked();
        }
    }
}
