package com.tjmedicine.emergency.ui.device.ecg.helowin.sdk.scan;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class ScanDev extends ScanCallback implements Runnable {
	public static ScanDev getInstance(ScanResultCallback mScanResult) {
			return new ScanDev(mScanResult);
	}
	{
		BluetoothLeScanner bluetoothLeScanner;
	}

	BluetoothLeScannerCompat bls = BluetoothLeScannerCompat.getScanner();

	volatile boolean isScanning = false;
	HashSet<String> devs = new HashSet<String>();
	Handler handler = new Handler(Looper.getMainLooper());
	ScanResultCallback mScanResult;
	public ScanDev(ScanResultCallback mScanResult) {
		this.mScanResult = mScanResult;
	}	

	public synchronized void startScan() {
		if(!isScanning){
			devs.clear();
			isScanning = true;
			bls.startScan(this);
			handler.postDelayed(this, 8000);
			
		}
	}

	public synchronized void startScan(UUID uuid) {
		if(!isScanning){
			devs.clear();
			isScanning = true;
			List<ScanFilter> filters = new ArrayList<ScanFilter>();
			ParcelUuid serviceUuid = new ParcelUuid(uuid);
			filters.add(new ScanFilter.Builder().setServiceUuid(serviceUuid).build());
			bls.startScan(filters , new ScanSettings.Builder().build(), this);
			handler.postDelayed(this, 8000);
			
		}
	}
	@Override
	public void run() {
		stopScan();
	}

	public synchronized void stopScan() {
		if(isScanning){
			isScanning = false;
			bls.stopScan(this);
			mScanResult.find(null);
		}
	}

	@Override
	public void onBatchScanResults(List<ScanResult> results) {
		if(results!=null)
		for(ScanResult sr:results){
			onScanResult(0, sr);
		}
	}

	@Override
	public void onScanResult(int callbackType, ScanResult result) {
		final BluetoothDevice device;
		if (result != null && (device = result.getDevice()) != null) {


			String devName = device.getAddress();
			System.out.println(devName+">>>>>>>>>>>>>>>>>>>>> address");

			if(devName!=null&&!devs.contains(devName)){
				System.out.println(device.getName()+">>>>>>>>>>>>>>>>>>>>> devName");
				devs.add(devName);
				if(device.getName()!=null&&device.getName().startsWith("T")) {
					mScanResult.find(device);
				}
			}
		}
	}
}
