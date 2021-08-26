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
package com.tjmedicine.emergency.ui.uart.scanner;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.lxj.xpopup.XPopup;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.dialog.CustomFullScreenPopup;
import com.tjmedicine.emergency.common.dialog.CustomLoadingFullScreenPopup;
import com.tjmedicine.emergency.ui.main.MainActivity;
import com.tjmedicine.emergency.ui.uart.UARTService;
import com.tjmedicine.emergency.ui.uart.profile.BleProfileService;
import com.tjmedicine.emergency.ui.uart.profile.UARTInterface;
import com.tjmedicine.emergency.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/**
 * ScannerFragment class scan required BLE devices and shows them in a list. This class scans and filter
 * devices with standard BLE Service UUID and devices with custom BLE Service UUID. It contains a
 * list and a button to scan/cancel. There is a interface {@link OnDeviceSelectedListener} which is
 * implemented by activity in order to receive selected device. The scanning will continue to scan
 * for 5 seconds and then stop.
 */
public class ScannerFragment extends DialogFragment {
    private final static String TAG = "ScannerFragment";
    private final static String PARAM_UUID = "param_uuid";
    private final static long SCAN_DURATION = 5000;

    private final static int REQUEST_PERMISSION_REQ_CODE = 34; // any 8-bit number

    private BluetoothAdapter bluetoothAdapter;
    private OnDeviceSelectedListener listener;
    private DeviceListAdapter adapter;
    private final Handler handler = new Handler();
    private Button scanButton;
    LottieAnimationView lav;
    private View permissionRationale;

    private ParcelUuid uuid;

    private boolean scanning = false;
    AlertDialog dialog = null;
    CustomFullScreenPopup customFullScreenPopup;
    CustomLoadingFullScreenPopup customLoadingFullScreenPopup;

    private static IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleProfileService.BROADCAST_CONNECTION_STATE);
        intentFilter.addAction(BleProfileService.BROADCAST_SERVICES_DISCOVERED);
        intentFilter.addAction(BleProfileService.BROADCAST_DEVICE_READY);
        intentFilter.addAction(BleProfileService.BROADCAST_BOND_STATE);
        intentFilter.addAction(BleProfileService.BROADCAST_ERROR);
        return intentFilter;
    }
//
//    /**
//     * 用volatile修饰的变量，
//     * 线程在每次使用变量的时候，都会读取变量修改后的最的值。
//     * volatile很容易被误用，用来进行原子性操作。
//     */
//    private static volatile ScannerFragment scannerFragment = null;
//
//    /**
//     * 单例模式：创建  Fragment：
//     *
//     * @return
//     */
//    public static ScannerFragment getInstance(final UUID uuid) {
//        if (scannerFragment == null) {
//            synchronized (ScannerFragment.class) {
//                if (scannerFragment == null) {
//                    scannerFragment = new ScannerFragment();
//                }
//            }
//        }
//        final Bundle args = new Bundle();
//        if (uuid != null)
//            args.putParcelable(PARAM_UUID, new ParcelUuid(uuid));
//        scannerFragment.setArguments(args);
//        return scannerFragment;
//    }


    public static ScannerFragment getInstance(final UUID uuid) {

        ScannerFragment fragment = null;
        if (fragment == null) {
            fragment = new ScannerFragment();
        }
        final Bundle args = new Bundle();
        if (uuid != null)
            args.putParcelable(PARAM_UUID, new ParcelUuid(uuid));
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Interface required to be implemented by activity.
     */
    public interface OnDeviceSelectedListener {
        /**
         * Fired when user selected the device.
         *
         * @param device the device to connect to
         * @param name   the device name. Unfortunately on some devices {@link BluetoothDevice#getName()}
         *               always returns <code>null</code>, i.e. Sony Xperia Z1 (C6903) with Android 4.3.
         *               The name has to be parsed manually form the Advertisement packet.
         */
        void onDeviceSelected(@NonNull final BluetoothDevice device, @Nullable final String name);

        /**
         * Fired when scanner dialog has been cancelled without selecting a device.
         */
        void onDialogCanceled();
    }

    /**
     * This will make sure that {@link OnDeviceSelectedListener} interface is implemented by activity.
     */
    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
        try {
            this.listener = (OnDeviceSelectedListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDeviceSelectedListener");
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL,R.style.fullscreen_dialog);
        final Bundle args = getArguments();
        if (args != null && args.containsKey(PARAM_UUID)) {
            uuid = args.getParcelable(PARAM_UUID);
        }

        final BluetoothManager manager = (BluetoothManager) requireContext().getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            bluetoothAdapter = manager.getAdapter();
        }

    }


    @Override
    public void onDestroyView() {
        stopScan();
        dismiss();
        super.onDestroyView();
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
        listview.setAdapter(adapter = new DeviceListAdapter());

        // title.setText(R.string.scanner_title);
        dialog = builder.setView(dialogView).create();
        listview.setOnItemClickListener((parent, view, position, id) -> {


            listview.post(new Runnable() {
                @Override
                public void run() {
                    //stopScan();
                    customLoadingFullScreenPopup.dismiss();
                    final ExtendedBluetoothDevice d = (ExtendedBluetoothDevice) adapter.getItem(position);
                    listener.onDeviceSelected(d.device, d.name);
                    customFullScreenPopup = new CustomFullScreenPopup(requireActivity());
                    new XPopup.Builder(requireActivity()).asCustom(customFullScreenPopup).show();
                }
            });
        });
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        permissionRationale = dialogView.findViewById(R.id.permission_rationale); // this is not null only on API23+

        scanButton = dialogView.findViewById(R.id.action_cancel);
        scanButton.setOnClickListener(v -> {
            if (v.getId() == R.id.action_cancel) {
                if (scanning) {
                    dialog.cancel();
                } else {
                    lav.setVisibility(View.VISIBLE);
                    startScan();
                }
            }
        });
        addBoundDevices();
        if (savedInstanceState == null) {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(commonBroadcastReceiver, makeIntentFilter());
            startScan();
            lav.setVisibility(View.VISIBLE);
        }
        dialog.setCancelable(false);
        close.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return dialog;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        listener.onDialogCanceled();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have been granted the Manifest.permission.ACCESS_FINE_LOCATION permission. Now we may proceed with scanning.
                    startScan();
                } else {
                    permissionRationale.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "没有权限.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /**
     * Scan for 5 seconds and then stop scanning when a BluetoothLE device is found then lEScanCallback
     * is activated This will perform regular scan for custom BLE Service UUID and then filter out.
     * using class ScannerServiceParser
     */
    private void startScan() {
        // Since Android 6.0 we need to obtain Manifest.permission.ACCESS_FINE_LOCATION to be able to scan for
        // Bluetooth LE devices. This is related to beacons as proximity devices.
        // On API older than Marshmallow the following code does nothing.
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // When user pressed Deny and still wants to use this functionality, show the rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) && permissionRationale.getVisibility() == View.GONE) {
                permissionRationale.setVisibility(View.VISIBLE);
                return;
            }

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }

        // Hide the rationale message, we don't need it anymore.
        if (permissionRationale != null)
            permissionRationale.setVisibility(View.GONE);

        adapter.clearDevices();
        scanButton.setText(R.string.scanner_action_cancel);
        customLoadingFullScreenPopup = new CustomLoadingFullScreenPopup(requireActivity());
        new XPopup.Builder(requireActivity()).asCustom(customLoadingFullScreenPopup).show();

        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        final ScanSettings settings = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(1000).setUseHardwareBatchingIfSupported(false).build();
        final List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setServiceUuid(uuid).build());
        scanner.startScan(filters, settings, scanCallback);

        scanning = true;
        handler.postDelayed(() -> {
            if (scanning) {
                customLoadingFullScreenPopup.dismiss();
                stopScan();
            }
        }, SCAN_DURATION);
    }

    /**
     * Stop scan if user tap Cancel button
     */
    private void stopScan() {
        if (scanning) {
            scanButton.setText(R.string.scanner_action_scan);
            lav.setVisibility(View.GONE);
            final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            scanner.stopScan(scanCallback);
            scanning = false;
        }
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, @NonNull final ScanResult result) {
            // do nothing
        }

        @Override
        public void onBatchScanResults(@NonNull final List<ScanResult> results) {
            adapter.update(results);
        }

        @Override
        public void onScanFailed(final int errorCode) {


            // should never be called
        }
    };

    private void addBoundDevices() {
        final Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        adapter.addBondedDevices(devices);
    }


    private final BroadcastReceiver commonBroadcastReceiver = new BroadcastReceiver() {

        private boolean flag = true;//加个标志，否则onReceive方法会重复接收通知

        @Override
        public void onReceive(final Context context, final Intent intent) {
//            if (flag) {
//                flag = false;
            // Check if the broadcast applies the connected device
            if (!isBroadcastForThisDevice(intent))
                return;

            final BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BleProfileService.EXTRA_DEVICE);
            if (bluetoothDevice == null)
                return;

            final String action = intent.getAction();
            switch (action) {
                case BleProfileService.BROADCAST_CONNECTION_STATE: {
                    final int state = intent.getIntExtra(BleProfileService.EXTRA_CONNECTION_STATE, BleProfileService.STATE_DISCONNECTED);

                    switch (state) {
                        case BleProfileService.STATE_CONNECTED: {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    ToastUtils.showTextToas(EmergencyApplication.getContext(), "设备连接成功~");
                                    Log.e(TAG, " --BleProFileService---onReceive:-------> " + "设备连接success");
                                    final UARTInterface uart = (UARTInterface) requireActivity();
                                    uart.send("<Battery?>");
                                    uart.send("<ZeroPressCal?>");
//                                    uart.send("<FWVer?>");
//                                    uart.send("<HWVer?>");
                                    if (null != customFullScreenPopup) {
                                        customFullScreenPopup.dismiss();
                                    }
                                    dialog.dismiss();
                                    //requireActivity().unregisterReceiver(commonBroadcastReceiver);
                                }
                            }, 2000);
                            break;
                        }
                        case BleProfileService.STATE_DISCONNECTED: {
                            ToastUtils.showTextToas(EmergencyApplication.getContext(), "设备断开连接~");
                            Log.e(TAG, " --BleProFileService---onReceive:-------> " + "设备断开连接");
                            if (null != customFullScreenPopup) {
                                customFullScreenPopup.dismiss();
                            }
                            break;
                        }
                        case BleProfileService.STATE_LINK_LOSS: {
                            Log.e(TAG, " --BleProFileService---onReceive:------STATE_LINK_LOSS-> ");
                            break;
                        }
                        case BleProfileService.STATE_CONNECTING: {
//                            ToastUtils.showTextToas(EmergencyApplication.getContext(), "设备正在连接中~");
                            Log.e(TAG, " --BleProFileService---onReceive:-------> " + "设备正在连接");
                            break;
                        }
                        case BleProfileService.STATE_DISCONNECTING: {
                            Log.e(TAG, " --BleProFileService---onReceive:------STATE_DISCONNECTING-> ");
//                            customFullScreenPopup.dismiss();
                            break;
                        }
                        default:
                            // there should be no other actions
                            break;
                    }
                    break;
                }
//                case BleProfileService.BROADCAST_SERVICES_DISCOVERED: {
//                    final boolean primaryService = intent.getBooleanExtra(BleProfileService.EXTRA_SERVICE_PRIMARY, false);
//                    final boolean secondaryService = intent.getBooleanExtra(BleProfileService.EXTRA_SERVICE_SECONDARY, false);
//
//                    if (primaryService) {
//                        onServicesDiscovered(bluetoothDevice, secondaryService);
//                    } else {
//                        onDeviceNotSupported(bluetoothDevice);
//                    }
//                    break;
//                }
//                case BleProfileService.BROADCAST_DEVICE_READY: {
//                    onDeviceReady(bluetoothDevice);
//                    break;
//                }
//                case BleProfileService.BROADCAST_BOND_STATE: {
//                    final int state = intent.getIntExtra(BleProfileService.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
//                    switch (state) {
//                        case BluetoothDevice.BOND_BONDING:
//                            onBondingRequired(bluetoothDevice);
//                            break;
//                        case BluetoothDevice.BOND_BONDED:
//                            onBonded(bluetoothDevice);
//                            break;
//                    }
//                    break;
//                }
//                case BleProfileService.BROADCAST_ERROR: {
//                    final String message = intent.getStringExtra(BleProfileService.EXTRA_ERROR_MESSAGE);
//                    final int errorCode = intent.getIntExtra(BleProfileService.EXTRA_ERROR_CODE, 0);
//                    onError(bluetoothDevice, message, errorCode);
//                    break;
//                }
            }
        }
//        }
    };

    /**
     * Checks the {@link BleProfileService#EXTRA_DEVICE} in the given intent and compares it with the connected BluetoothDevice object.
     *
     * @param intent intent received via a broadcast from the service
     * @return true if the data in the intent apply to the connected device, false otherwise
     */
    protected boolean isBroadcastForThisDevice(final Intent intent) {
        final BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BleProfileService.EXTRA_DEVICE);
        return bluetoothDevice != null && bluetoothDevice.equals(bluetoothDevice);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        try {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(commonBroadcastReceiver);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
