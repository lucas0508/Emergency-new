package com.tjmedicine.emergency.ui.uart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tjmedicine.emergency.ui.uart.profile.UARTInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.tjmedicine.emergency.ui.uart.UARTService.EXTRA_DATA;



public class UpdateFW {


    private String TAG ="UpdateFW.CLASS";

    private String hwInVersionFile = null;
    private String versionInVersionFile = null;
    private String fwInVersionFile = null;
    private int lenInVersionFile = 0;
    private String md5InVersionFile = null;
    private byte[] arrayAIDBin = null;
    private int lenSeg = 90;
    Context context;
    String strFWVersion;
    UARTInterface uartInterface;

    public interface DataCallback{
        void setDataProgress(int progress);
    }

    public DataCallback dataCallback;//接口


    public UpdateFW(Context context,String strFWVersion,UARTInterface uartInterface,DataCallback dataCallback) {
        this.context=context;
        this.strFWVersion=strFWVersion;
        this.uartInterface=uartInterface;
        this.dataCallback= dataCallback;

        BTBroadcastReceiver receiver = new BTBroadcastReceiver();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("no.nordicsemi.android.nrftoolbox.uart.BROADCAST_UART_RX");
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter1);

    }

    public  boolean isUpdateFW() {


        String strAIDVersionFile = getAIDVersionFileString();
        if (strAIDVersionFile == null) {
            Log.e(TAG, " --->" + "Get AIDVersion File Failed!");
            return false;
        } else {
            Log.e(TAG, " --->" + "AIDVersion=" + strAIDVersionFile);
        }

        if (strFWVersion == null) {
            Log.e(TAG, " --->" + "Get FW Version Failed!");
            return false;
        } else {
            Log.e(TAG, " --->" + "FW Version=" + strFWVersion);
        }
        if (strFWVersion.indexOf(versionInVersionFile) > 0) {
            Log.e(TAG, " --->" + "No New Version Update!");
            return true;
        }

        if (getAIDBinFile() == false) {
            Log.e(TAG, " --->" + "Download Bin Faild!");
            return false;
        }
        return sendUpdateInfo();
    }


    private boolean sendUpdateInfo() {

        String strRsp = null;

        strRsp = sendChecksumCommand("NewVer=" + versionInVersionFile);
        if (strRsp == null)
            return false;
        if (!strRsp.equals("<OK>"))
            return false;

        strRsp = sendChecksumCommand("NewLen=" + lenInVersionFile);
        if (strRsp == null)
            return false;
        if (!strRsp.equals("<OK>"))
            return false;

        strRsp = sendChecksumCommand("MD5A=" + md5InVersionFile.substring(0, 8));
        if (strRsp == null)
            return false;
        if (!strRsp.equals("<OK>"))
            return false;

        strRsp = sendChecksumCommand("MD5B=" + md5InVersionFile.substring(8, 16));
        if (strRsp == null)
            return false;
        if (!strRsp.equals("<OK>"))
            return false;

        strRsp = sendChecksumCommand("MD5C=" + md5InVersionFile.substring(16, 24));
        if (strRsp == null)
            return false;
        if (!strRsp.equals("<OK>"))
            return false;

        strRsp = sendChecksumCommand("MD5D=" + md5InVersionFile.substring(24, 32));
        if (strRsp == null)
            return false;
        if (!strRsp.equals("<OK>"))
            return false;

        strRsp = sendChecksumCommand("SegLen=" + lenSeg);
        if (strRsp == null)
            return false;
        if (!strRsp.equals("<OK>"))
            return false;

        strRsp = sendCommand("<UpdateStart>");
        if (strRsp == null)
            return false;
        if (!strRsp.equals("<OK>"))
            return false;

        if (sendBinFile() == false)
            return false;

        strRsp = sendCommand("<UpdateFinish>");
        if (strRsp == null)
            return false;
        if (strRsp.equals("<OK>"))
            return true;
        else
            return false;
    }

    private String getAIDVersionFileString() {
        String strVersionFile = null;

        String urlStr = "http://mu2020.xyz/aid/version.txt";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            InputStream input = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            strVersionFile = sb.toString();
            Log.e(TAG, " --->" + "VersionFile:strVersionFile");
            JSONObject jsonObject = new JSONObject(strVersionFile);
            hwInVersionFile = jsonObject.getString("hw");
            versionInVersionFile = jsonObject.getString("version");
            fwInVersionFile = jsonObject.getString("fw");
            lenInVersionFile = jsonObject.getInt("len");
            md5InVersionFile = jsonObject.getString("md5");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strVersionFile;
    }

    private String sendCommand(String cmd) {
        String strRsp = null;
        resetStringBTBroadcastReceiver();
        uartInterface.send(cmd);
        strRsp = getStringBTBroadcastReceiver(10000);
        if (strRsp == null)
            Log.e(TAG, " --->" + "Command " + cmd + " No Rsp!");
        return strRsp;
    }

    private boolean getAIDBinFile() {
        /**
         *  {
         * "hw": "AIDv1.0",
         * "version": "v0.1.1",
         * "fw": "AIDv0.1.0.bin",
         * "len": "18728",
         * "md5": "AECB8AFDD5236A6DCE2A6299CA47ADF0"
         * }
         */
        arrayAIDBin = new byte[0];
        String urlStr = "http://mu2020.xyz/aid/" + fwInVersionFile;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            InputStream input = conn.getInputStream();
            byte[] buff = new byte[1024];
            while (true) {
                int len = input.read(buff);
                if (len < 0)
                    break;
                byte[] tmp = new byte[arrayAIDBin.length + len];
                System.arraycopy(arrayAIDBin, 0, tmp, 0, arrayAIDBin.length);
                System.arraycopy(buff, 0, tmp, arrayAIDBin.length, len);
                arrayAIDBin = tmp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (arrayAIDBin.length == lenInVersionFile)
            return true;
        else {
            Log.e(TAG, " --->" + "Bin File Length Error!, Length=" + arrayAIDBin.length);
            return false;
        }
    }

    private String sendChecksumCommand(String cmd) {
        String strRsp = null;
        String checksumCmd = "<" + cmd + "|" + getChecksum(cmd) + ">";
        resetStringBTBroadcastReceiver();
        uartInterface.send(checksumCmd);
        strRsp = getStringBTBroadcastReceiver(10000);
        if (strRsp == null)
            Log.e(TAG, " --->" + "Command " + cmd + " No Rsp!");
        return strRsp;
    }

    private boolean sendBinFile() {
        String strSend = "";
        String strRsp = null;

        for (int i = 0; i < lenInVersionFile; i++) {
            int c = arrayAIDBin[i] & 0xFF;
            if (c < 16)
                strSend = strSend + "0" + Integer.toHexString(c);
            else
                strSend = strSend + Integer.toHexString(c);

            if (strSend.length() == 18) {
                strSend = "<" + strSend + ">";
                if ((i + 1) % lenSeg != 0) {
                    sendData(strSend);
                } else {
                    strRsp = sendCommand(strSend);
                    if (strRsp == null)
                        return false;
                    if (!strRsp.equals("<OK>"))
                        return false;
                }

                //进度
                Log.e(TAG, " --->" + (100 * i / lenInVersionFile) + "% Sent!");
                dataCallback.setDataProgress((100 * i / lenInVersionFile));
                strSend = "";
            }
        }
        if (strSend.length() > 0) {
            strSend = "<" + strSend + ">";
            strRsp = sendCommand(strSend);
            if (strRsp == null)
                return false;
            if (!strRsp.equals("<OK>"))
                return false;
        }

        return true;
    }

    private void sendData(String cmd) {
        uartInterface.send(cmd);
    }

    private void resetStringBTBroadcastReceiver() {
        stringBTBroadcastReceiver = null;
    }

    private String stringBTBroadcastReceiver = null;

    public class BTBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String data = intent.getStringExtra(EXTRA_DATA);
            stringBTBroadcastReceiver = data;
            System.out.println("BTBroadcastReceiver,action:" + action + ",data:" + data);
            Log.e(TAG, " --->" + "BTBroadcastReceiver,action:" + action + ",data:" + data);
        }
    }

    private String getStringBTBroadcastReceiver(int timeout) {
        long t1 = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - t1 > timeout)
                return null;
            if (stringBTBroadcastReceiver != null)
                return stringBTBroadcastReceiver;
        }
    }

    private String getChecksum(String str) {
        int nChecksum = 0;
        for (int i = 0; i < str.length(); i++) {
            nChecksum += str.charAt(i);
        }
        nChecksum = nChecksum & 0xFF;
        String strChecksum = Integer.toHexString(nChecksum);
        if (nChecksum < 16)
            strChecksum = "0" + Integer.toHexString(nChecksum);
        return strChecksum;
    }
}
