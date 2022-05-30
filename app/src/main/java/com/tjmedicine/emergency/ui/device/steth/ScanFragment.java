package com.tjmedicine.emergency.ui.device.steth;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.kl.minttisdk.ble.BleDevice;
import com.kl.minttisdk.ble.BleManager;
import com.kl.minttisdk.ble.callback.IBleConnectStatusListener;
import com.kl.minttisdk.ble.callback.IBleScanCallback;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.lxj.xpopup.XPopup;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.dialog.CustomFullScreenPopup;
import com.tjmedicine.emergency.common.dialog.CustomLoadingFullScreenPopup;
import com.tjmedicine.emergency.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class ScanFragment extends DialogFragment implements IBleConnectStatusListener {


    private final static String TAG = "ScanFragment";
    private final static String PARAM_UUID = "param_uuid";
    private final static long SCAN_DURATION = 5000;
    private LeDeviceListAdapter adapter;
    private final Handler handler = new Handler();
    private Button scanButton;
    LottieAnimationView lav;
    private View permissionRationale;

    private ParcelUuid uuid;

    private boolean scanning = false;
    AlertDialog dialogBe = null;
    CustomFullScreenPopup customFullScreenPopup;
    CustomLoadingFullScreenPopup customLoadingFullScreenPopup;

    // 0.9秒内防止多次点击
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public static ScanFragment getInstance(final UUID uuid) {

        ScanFragment fragment = null;
        if (fragment == null) {
            fragment = new ScanFragment();
        }
        final Bundle args = new Bundle();
        if (uuid != null)
            args.putParcelable(PARAM_UUID, new ParcelUuid(uuid));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL,R.style.fullscreen_dialog);
        final Bundle args = getArguments();
        if (args != null && args.containsKey(PARAM_UUID)) {
            uuid = args.getParcelable(PARAM_UUID);
        }

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()
                , R.style.fullscreen_dialog);
        final View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_device_selection, null);
        final ListView listview = dialogView.findViewById(android.R.id.list);
        final ImageButton close = dialogView.findViewById(R.id.ib_close);
        lav = dialogView.findViewById(R.id.lav);
        listview.setEmptyView(dialogView.findViewById(android.R.id.empty));
        listview.setAdapter(adapter = new LeDeviceListAdapter());


        // title.setText(R.string.scanner_title);
        dialogBe = builder.setView(dialogView).create();
        listview.setOnItemClickListener((parent, view, position, id) -> {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                listview.post(new Runnable() {
                    @Override
                    public void run() {
                        BleDevice bleDevice = adapter.getDevice(position);

                        if (bleDevice == null)
                            return;

                        stopScanBLE();
                        WaitDialog.show("正在连接中。。。");
//                        customFullScreenPopup = new CustomFullScreenPopup(requireActivity());
//                        new XPopup.Builder(requireActivity()).asCustom(customFullScreenPopup).show();
                        BleManager.getInstance().connect(bleDevice);
                    }
                });
            }
        });
        Window window = dialogBe.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        permissionRationale = dialogView.findViewById(R.id.permission_rationale); // this is not null only on API23+

        BleManager.getInstance().addConnectionListener(this);
        dialogBe.setCancelable(false);
        close.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        scanButton = dialogView.findViewById(R.id.action_cancel);
        scanButton.setOnClickListener(v -> {
            if (v.getId() == R.id.action_cancel) {
                if (scanning) {
                    dialogBe.cancel();
                } else {
                    lav.setVisibility(View.VISIBLE);
                    startScanBLE();
                }
            }
        });
        startScanBLE();

        return dialogBe;
    }

    private void startScanBLE() {
        lav.setVisibility(View.VISIBLE);
        customLoadingFullScreenPopup = new CustomLoadingFullScreenPopup(requireActivity());
        new XPopup.Builder(requireActivity()).asCustom(customLoadingFullScreenPopup).show();

        BleManager.getInstance().startScan(new IBleScanCallback() {
            @Override
            public void onScanResult(BleDevice bleDevice) {
                String deviceName = bleDevice.getName();
                scanning = true;
                if (!TextUtils.isEmpty(deviceName) && deviceName.equals("Mintti")) {
                    adapter.addDevice(bleDevice);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScanFailed(int i) {

            }
        });
        handler.postDelayed(() -> {
            if (scanning) {
                customLoadingFullScreenPopup.dismiss();
                stopScanBLE();
            }
        }, SCAN_DURATION);
    }

    private void stopScanBLE() {
        if (BleManager.getInstance().isScanning()) {
            BleManager.getInstance().stopScan();
            lav.setVisibility(View.GONE);
            scanning = false;
        }
    }

    @Override
    public void onConnectFail(String s, int i) {

        TipDialog.show("连接失败", WaitDialog.TYPE.ERROR);
    }

    @Override
    public void onConnectSuccess(String s) {
        WaitDialog.show("正在更新参数");
    }

    @Override
    public void onUpdateParamsSuccess() {

        TipDialog.show("连接成功", WaitDialog.TYPE.SUCCESS).setDialogLifecycleCallback(new DialogLifecycleCallback<WaitDialog>() {
            @Override
            public void onDismiss(WaitDialog dialog) {
                super.onDismiss(dialog);
                Intent intent = new Intent(requireActivity(), StethoscopeActivity.class);
                intent.putExtra("BLE_ADDRESS", BleManager.getInstance().getBluetoothDevice().getAddress());
                startActivity(intent);
                dialogBe.dismiss();
            }
        });

    }

    @Override
    public void onUpdateParamsFail() {

        TipDialog.show("更新参数失败", WaitDialog.TYPE.ERROR);
    }

    @Override
    public void onDisConnected(String s, boolean b, int i) {

    }

    class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BleDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BleDevice>();
            mInflator = getLayoutInflater();
        }

        public void addDevice(BleDevice dev) {
            int i = 0;
            int listSize = mLeDevices.size();
            for (i = 0; i < listSize; i++) {
                if (mLeDevices.get(i).getBluetoothDevice().equals(dev.getBluetoothDevice())) {
                    mLeDevices.get(i).setRssi(dev.getRssi());
                    break;
                }
            }

            if (i >= listSize) {
                mLeDevices.add(dev);
            }

        }

        public BleDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.item_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.deviceSignal = (TextView) view.findViewById(R.id.signal);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BleDevice bleDevice = mLeDevices.get(i);
            final String deviceName = bleDevice.getName();

            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
            } else {
                viewHolder.deviceName.setText("Unknow device");
            }

            viewHolder.deviceAddress.setText(bleDevice.getMac());
            viewHolder.deviceSignal.setText(bleDevice.getRssi() + "dBm");
            return view;
        }
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceSignal;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dismiss();
    }

    @Override
    public void onDestroyView() {
        stopScanBLE();
        dismiss();
        super.onDestroyView();
    }


}
