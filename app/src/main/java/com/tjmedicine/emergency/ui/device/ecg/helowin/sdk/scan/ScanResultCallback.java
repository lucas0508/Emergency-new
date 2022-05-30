package com.tjmedicine.emergency.ui.device.ecg.helowin.sdk.scan;

import android.bluetooth.BluetoothDevice;

public interface ScanResultCallback {
	boolean find(BluetoothDevice device);
}
