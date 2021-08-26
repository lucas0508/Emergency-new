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

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.lxj.xpopup.XPopup;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.dialog.CustomFullScreenPopup;
import com.tjmedicine.emergency.ui.uart.domain.UartConfiguration;
import com.tjmedicine.emergency.ui.uart.profile.BleProfileService;
import com.tjmedicine.emergency.ui.uart.profile.BleProfileServiceReadyActivity;
import com.tjmedicine.emergency.ui.uart.profile.UARTConfigurationSynchronizer;
import com.tjmedicine.emergency.ui.uart.profile.UARTConfigurationsAdapter;
import com.tjmedicine.emergency.ui.uart.profile.UARTInterface;
import com.tjmedicine.emergency.ui.uart.scanner.ScannerFragment;

import java.util.UUID;


public class UARTActivity extends BleProfileServiceReadyActivity<UARTService.UARTBinder> implements UARTInterface, UARTConfigurationsAdapter.ActionListener, AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks {
    private final static String TAG = "UARTActivity";


    private SharedPreferences preferences;
    private UARTConfigurationsAdapter configurationsAdapter;
    private ClosableSpinner configurationSpinner;
    private SlidingPaneLayout slider;
    private View container;

    private ConfigurationListener configurationListener;
    private UARTService.UARTBinder serviceBinder;
    private String modes;
    UARTControlFragment uartControlFragment;
    UARTControlScoreFragment uartControlScoreFragment;
    UARTControlExamFragment uartControlExamFragment;
     ScannerFragment dialog;

    public interface ConfigurationListener {
        void onConfigurationModified();

        void onConfigurationChanged(@NonNull final UartConfiguration configuration);

    }

    public void setConfigurationListener(final ConfigurationListener listener) {
        configurationListener = listener;
    }

    @Override
    protected Class<? extends BleProfileService> getServiceClass() {
        return UARTService.class;
    }

    @Override
    protected int getLoggerProfileTitle() {
        return R.string.uart_feature_title;
    }

    @Override
    protected Uri getLocalAuthorityLogger() {
        return UARTLocalLogContentProvider.AUTHORITY_URI;
    }

    @Override
    protected void setDefaultUI() {
        // empty
    }

    @Override
    protected void onServiceBound(final UARTService.UARTBinder binder) {
        serviceBinder = binder;
    }

    @Override
    protected void onServiceUnbound() {
        serviceBinder = null;
    }


    /**
     * Method called when Google API Client connects to Wearable.API.
     */
    @Override
    public void onConnected(final Bundle bundle) {
        // Ensure the Wearable API was connected


    }



    /**
     * Method called then Google API client connection was suspended.
     *
     * @param cause the cause of suspension
     */
    @Override
    public void onConnectionSuspended(final int cause) {
        // dp nothing
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_feature_uart;
    }


    @Override
    protected void initView() {
        modes = getIntent().getStringExtra("mode");
        container = findViewById(R.id.container);


//        TextView title = findViewById(R.id.tv_title);
//        title.setText("模拟人");
        // In onInitialize method a final class may register local broadcast receivers that will listen for events from the service

        if (isBLEEnabled()) {
            // isBLEEnabled()
            if (service == null) {

//                setDefaultUI();
                //x搜索所有的可连接蓝牙，用户自行连接
                // showDeviceScanningDialog(getFilterUUID());
                //x根据UUID搜索可用蓝牙
                //showDeviceScanningDialog(UART_SERVICE_UUID);
//                x自动根据UUID自动连接脸呀
//                connectBLE();

//                CustomFullScreenPopup customFullScreenPopup = new CustomFullScreenPopup(this);
//                new XPopup.Builder(this).asCustom(customFullScreenPopup).show();
                showDeviceScanningDialog(UART_SERVICE_UUID);
            } else {
                if (serviceBinder != null) {
                    serviceBinder.send("<TestStop>");
                }
                service.disconnect();
            }
        } else {
            showBLEDialog();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (modes.equals("1")) {
            if (uartControlFragment == null) {
                uartControlFragment = new UARTControlFragment();
                fragmentTransaction.add(R.id.content, uartControlFragment);
            } else {
                fragmentTransaction.show(uartControlFragment);
            }

        } else if (modes.equals("2")) {
            if (uartControlFragment == null) {
                uartControlScoreFragment = new UARTControlScoreFragment();
                fragmentTransaction.add(R.id.content, uartControlScoreFragment);
            } else {
                fragmentTransaction.show(uartControlScoreFragment);
            }

        } else if (modes.equals("3")) {
            if (uartControlFragment == null) {
                uartControlScoreFragment = new UARTControlScoreFragment();
                fragmentTransaction.add(R.id.content, uartControlScoreFragment);
            } else {
                fragmentTransaction.show(uartControlScoreFragment);
            }

        }
        fragmentTransaction.commit();


    }


    /**
     * Shows the scanner fragment.
     *
     * @param filter the UUID filter used to filter out available devices. The fragment will always show all bonded devices as there is no information about their
     *               services
     * @see #getFilterUUID()
     */
    private void showDeviceScanningDialog(final UUID filter) {
            dialog = ScannerFragment.getInstance(filter);
            dialog.show(getSupportFragmentManager(), "scan_fragment");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null) {
            dialog.onDestroy();
            dialog.dismiss();
            dialog=null;
        }

    }


    @Override
    protected void onCreateView(final Bundle savedInstanceState) {

        // Setup the sliding pane if it exists

    }


    @Override
    public void onServicesDiscovered(@NonNull final BluetoothDevice device, final boolean optionalServicesFound) {
        // do nothing
//        if (modes.equals("1")) {
//            if (null != uartControlFragment) uartControlFragment.onServiceStarted();
//        } else if (modes.equals("2")) {
//            if (null != uartControlScoreFragment)
//                uartControlScoreFragment.onServiceStarted();
//        }
//        dialog.onServiceStarted();
        //  ScannerFragment.onServiceStarted()

    }

    @Override
    public void onDeviceSelected(@NonNull final BluetoothDevice device, final String name) {
        // The super method starts the service
        super.onDeviceSelected(device, name);
        // Notify the log fragment about it


    }

    @Override
    protected int getDefaultDeviceName() {
        return R.string.uart_default_name;
    }

    @Override
    protected int getAboutTextId() {
        return R.string.uart_about_text;
    }

    @Override
    protected UUID getFilterUUID() {
        return null; // not used
    }

    @Override
    public void send(final String text) {
        if (serviceBinder != null)
            serviceBinder.send(text);
    }


    @Override
    public void onBackPressed() {
        if (slider != null && slider.isOpen()) {
            slider.closePane();
            return;
        }
        super.onBackPressed();
    }


    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {
        // do nothing
    }

    @Override
    public void onNewConfigurationClick() {
        // No item has been selected. We must close the spinner manually.
        //	configurationSpinner.close();

        // Open the dialog
        final DialogFragment fragment = UARTNewConfigurationDialogFragment.getInstance(null, false);
        fragment.show(getSupportFragmentManager(), null);

        // onNewConfiguration(null, false) will be called when user press OK
    }

    @Override
    public void onImportClick() {

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
