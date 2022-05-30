package com.tjmedicine.emergency.ui.device.ecg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplicationmode.ecg.EcgDisplayer;
import com.example.myapplicationmode.ecg.EcgStatusSub;

import com.helowin.ecg.sdk.bean.EcgBean;
import com.helowin.ecg.sdk.ble.ConManager;
import com.helowin.ecg.sdk.ble.Data24Manager;
import com.helowin.ecg.sdk.ble.DataTask;
import com.helowin.ecg.sdk.ecg.IView;
import com.helowin.ecg.sdk.util.EcgSdk;
import com.helowin.ecg.sdk.widget.AnimImageView;

import com.helowin.sdk.EcgDetailAct;
import com.helowin.sdk.EcgDetailFailAct;
import com.helowin.sdk.cache.SelectEcgInfo;
import com.helowin.sdk.net.EcgListP;
import com.helowin.sdk.net.UpdateEcgP;
import com.tjmedicine.emergency.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import b.IHis;
import b.SaveHisManager;
import b.Utils;

public class Ecg24Activity extends FragmentActivity implements IView, IHis, UpdateEcgP.UpdateEcgV, EcgListP.EcgListV {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ecg_24);
        init();
    }

    //    EcgListP ecgListP;
    private static final String TAG = Ecg24Activity.class.getSimpleName();
    EcgStatusSub ecgStatusSub = new EcgStatusSub();
    EcgDisplayer ecgDisplayer = new EcgDisplayer();
    HisView hisView = new HisView();
    ConManager conManager;
    Data24Manager dataManager;

    TextView delete;
    public  void f(){
        layout_ecg_b_his.setVisibility(View.GONE);
        layout_clm_b_sleep.setVisibility(View.GONE);
        layout_sys_stop.setVisibility(View.GONE);
        layout_doit_data.setVisibility(View.GONE);
        layout_ecg_b_status_sub.setVisibility(View.GONE);
    }



    SaveHisManager saveHisManager;
    View layout_ecg_b_status_sub;
    View layout_clm_b_sleep;
    View layout_doit_data;
    View layout_ecg_b_his;
    View layout_sys_stop;
    TextView timeLength;
    View stop;
    TextView reTry;
    /**
     * 保持屏幕常亮
     */
    protected void openScreenOn() {
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {
        }
    }
    /**
     * 取消屏幕常亮
     */
    protected void closeScreenOn() {
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDes = true;
         SaveHisManager.closeT();
        DataTask.Companion.close();
        closeScreenOn();
        if(!isOk){
        }
    }

    public void back(View view){
        finish();


    }
    protected void init() {
        setTitle("采集动态心电");
        EcgSdk.Companion.getInstance().init(this);
        if(!Utils.getMac().equals(EcgSdk.Companion.getInstance().getMac())){
            Utils.setMode(0);
        }
        openScreenOn();
        layout_doit_data = findViewById(R.id.layout_doit_data);
        layout_clm_b_sleep =findViewById(R.id.layout_clm_b_sleep);

        {
            delete = layout_clm_b_sleep.findViewById(R.id.delete);
            timeLength = layout_clm_b_sleep.findViewById(R.id.timeLength);
            stop = layout_clm_b_sleep.findViewById(R.id.stop);
            stop.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (dataManager != null) {
                        if(seq>=180) {
                            dataManager.stop(true);
                            layout_sys_stop.setVisibility(View.VISIBLE);
                        }else {
                            new AlertDialog.Builder(v.getContext()).setTitle("提示")
                                    .setMessage("动态心电3分钟内不能停止").setPositiveButton("是", null).setCancelable(true).create().show();
                        }
                    }
                }
            });
        }
        layout_sys_stop = findViewById(R.id.layout_sys_stop);
        layout_ecg_b_status_sub = findViewById(R.id.layout_ecg_b_status_sub);
        layout_ecg_b_his = findViewById(R.id.layout_ecg_b_his);
        ecgStatusSub.init(layout_ecg_b_status_sub);
        ecgDisplayer.init(layout_clm_b_sleep);
        hisView.init(layout_ecg_b_his);
        start();
        reTry = findViewById(R.id.reTry);
        reTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reTry.setVisibility(View.GONE);
                finish(ecgBean);
            }
        });
    }
    public void start() {
        f();
        layout_ecg_b_status_sub.setVisibility(View.VISIBLE);
        if(conManager==null){
            conManager = new ConManager();
            conManager.init(this,0);
        }
        conManager.start(this);
    }
    public void clm(View view){
        if(ecgStatusSub.isTryable()){
            start();
        }
    }
    @Override
    public void connectOk() {
        this.ecgStatusSub.connectOk();
    }
    EcgBean ecgBean;
    boolean isDes = false;
    @Override
    public void show(String banfeibi, String time, int index, int max) {
        hisView.show(banfeibi,time,index,max);
    }

    @Override
    public void showLowEng(boolean flag) {
        hisView.showLowEng(flag);
    }

    @Override
    public void update() {

    }

    @Override
    public void updateFail() {

    }

    @Override
    public void updateSuccess() {

    }

    @Override
    public void showUpdateFile(int index, int max) {

    }
    UpdateEcgP ecgP;
    @Override
    public void finish(EcgBean ecgBean) {
        if(ecgBean==null){
            Toast.makeText(this,"测量失败请重新测量",Toast.LENGTH_LONG).show();
            super.finish();
            isOk = true;
            return;
        }
        this.ecgBean = ecgBean;

        Log.e(TAG,ecgBean.toString());
        if(layout_doit_data!=null) {
            TextView textView = layout_doit_data.findViewById(R.id.text10);
            textView.setText("数据上传中...");
        }
        if(ecgP==null){
            layout_doit_data.setVisibility(View.VISIBLE);
            TextView textView = layout_doit_data.findViewById(R.id.text10);

            textView.setText("");
            ecgP = new UpdateEcgP(ecgBean,this,false);
            ecgP.start();
        }
                try {
                    f();
            layout_doit_data.setVisibility(View.VISIBLE);
            if(animImageView==null) {
                animImageView = layout_doit_data.findViewById(R.id.aiv10);
                animImageView.startAnim();
                animImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      finish(ecgBean);
                    }
                });
            }
            TextView textView = layout_doit_data.findViewById(R.id.text10);
            textView.setText("请稍等");
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        //上传流程
    }

    @Override
    public void HisTimeOut() {
        start();
    }

    AnimImageView animImageView;
    @Override
    public void save(int length) {
        if(dataManager!=null){
            dataManager.close();
            dataManager = null;
        }
        if(saveHisManager==null) {
            saveHisManager = SaveHisManager.create();
            saveHisManager.init(this,5);
            f();
            layout_ecg_b_his.setVisibility(View.VISIBLE);
        }
    }
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second)+"秒";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分"
                        + unitFormat(second)+"秒";
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    boolean isFirst = true;
    int sum = -1;
    int seq = 0;
    @Override
    public void seq(int seq) {
        this.seq = seq;
        if(seq == 0){
            if (isFirst) {
                isFirst = false;
            } else {
                if (sum == -1) {
                    sum = 100;
                } else if (sum > 0) {
                    sum--;
                }
                this.timeLength.setText("清除缓存,请稍等" + ((sum > 0) ? ("(" + (100-sum) + "%)") : ""));
                return;
            }
        }
         this.timeLength.setText("监测时长：" + secToTime(seq));
    }

    @Override
    public void timeOut(int time) {
        ecgStatusSub.timeOut(time);
        if (time==30){
            connectTimeOut();
        }
    }

    @Override
    public void connectUi() {
        this.ecgStatusSub.connectUi();
    }

    @Override
    public void connectTimeOut() {
        layout_ecg_b_status_sub.setVisibility(View.VISIBLE);
        this.ecgStatusSub.connectTimeOut();
    }

    @Override
    public void setBuffer(float[] buffer) {
        ecgDisplayer.setBuffer(buffer);
    }

    @Override
    public void setNoEcg(int eng, int leadoff, int hr, int temp) {
        ecgDisplayer.setNoEcg(eng,leadoff,hr,temp);
    }

    @Override
    public void next(String equVersion) {
        if(conManager!=null) {
            conManager.close();
            conManager = null;
        }
        int mode = Utils.getMode();
        if(mode<=5){
            f();
            layout_clm_b_sleep.setVisibility(View.VISIBLE);
            dataManager = new Data24Manager();
            dataManager.setRunning(mode==5);
            dataManager.init(this, 5);

        }else {
            f();
            layout_ecg_b_his.setVisibility(View.VISIBLE);
            saveHisManager = SaveHisManager.create();
            saveHisManager.init(this,5);
        }
    }

    @Override
    public void setVersion(String equVersion) {
        ecgDisplayer.setVersion(equVersion);
    }

    @Override
    public void bleCheckFail() {
        ecgStatusSub.bleCheckFail();
    }
    String rid;
    private EcgListP ecgListP;
    @Override
    public void success(String rid) {

        this.rid = rid;
        isOk = true;
        Log.e(TAG,rid+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        setResult(RESULT_OK);
        if(layout_doit_data!=null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout_doit_data.setVisibility(View.VISIBLE);
                    TextView textView = layout_doit_data.findViewById(R.id.text10);

                    textView.setText("分析中...");
                }
            });

        }
        ecgListP = new EcgListP(ecgBean.getUserNo());
        ecgListP.setV(this);
        ecgListP.start(true);
        //上传成功获取详情功能
    }
    boolean isOk;
    public void showToast(String string){
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void finish() {
        if(isOk){
//            ecgListP.setElv(null);
            super.finish();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否退出");
        builder.setPositiveButton("是",new DialogInterface.OnClickListener(){


            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                if(ecgListP!=null) {
//                    ecgListP.setElv(null);
//                }
                Ecg24Activity.super.finish();

            }
        });
        builder.setNegativeButton("否",new DialogInterface.OnClickListener(){


            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    int count = 0;

    @Override
    public void fail() {
        ecgP = null;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isOk) {
                    reTry.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void success(@NotNull ArrayList<SelectEcgInfo.SelectEcg> r) {
        if(!r.isEmpty()){
            for(SelectEcgInfo.SelectEcg se:r){
                if(se.getUuid().equals(rid)){
                    if(!se.getTitle().contains("分析中")) {
                        Intent intent = new Intent();
                        if (!TextUtils.isEmpty(se.isNormal()) && se.isNormal().equals("0")) {
                            intent.setClass(this, EcgDetailAct.class);
                        } else {
                            intent.setClass(this, EcgDetailFailAct.class);
                        }
                        intent.putExtra("TAG", se);
                        startActivity(intent);
                        finish();
                        return;
                    }
                    break;
                }
            }
            ecgListP.start(true);
        }
    }

    @Override
    public void end() {

    }
}
