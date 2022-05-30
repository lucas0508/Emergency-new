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
import com.example.myapplicationmode.ecg.FastEcg;
import com.helowin.ecg.sdk.bean.EcgBean;
import com.helowin.ecg.sdk.ble.ConManager;
import com.helowin.ecg.sdk.ble.DataManager;
import com.helowin.ecg.sdk.ble.DataTask;
import com.helowin.ecg.sdk.ecg.IView;
import com.helowin.ecg.sdk.util.EcgSdk;
import com.helowin.ecg.sdk.widget.AnimImageView;

import com.helowin.sdk.EcgDetailAct;
import com.helowin.sdk.EcgDetailFailAct;
import com.helowin.sdk.cache.SelectEcgInfo;
import com.helowin.sdk.net.EcgListP;
import com.helowin.sdk.net.UpdateEcgP;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EcgActivity extends BaseActivity implements IView, EcgListP.EcgListV, UpdateEcgP.UpdateEcgV {

    @Override
    protected int setLayoutResourceID() {
        return R.layout.act_ecg;
    }

    @Override
    protected void initView() {
        init();
    }

    //    EcgListP ecgListP;
    private static final String TAG = EcgActivity.class.getSimpleName();
    EcgStatusSub ecgStatusSub = new EcgStatusSub();
    EcgDisplayer ecgDisplayer = new EcgDisplayer();
    FastEcg fastEcg = new FastEcg();
    UpdateEcgP ecgP;
    ConManager conManager;
    DataManager dataManager;
    View layout_ecg_b_status_sub;
    View layout_ecg_b_fast_displayer;
    View layout_doit_data;
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
    boolean isDes = false;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataTask.Companion.close();
        closeScreenOn();
        if(!isOk){

        }
        isDes = true;
    }

    public void back(View view){
        finish();


    }
    protected void init() {
        setTitle("采集片段心电");
        EcgSdk.Companion.getInstance().init(this);
        openScreenOn();
        layout_doit_data = findViewById(R.id.layout_doit_data);
        layout_ecg_b_fast_displayer =findViewById(R.id.layout_ecg_b_fast_displayer);
        layout_ecg_b_status_sub = findViewById(R.id.layout_ecg_b_status_sub);
        ecgStatusSub.init(layout_ecg_b_status_sub);
        ecgDisplayer.init(layout_ecg_b_fast_displayer);
        fastEcg.init(layout_ecg_b_fast_displayer);
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
    @Override
    public void finish(EcgBean ecgBean) {
        if(ecgBean==null){
            Toast.makeText(this,"测量失败请重新测量",Toast.LENGTH_LONG).show();
            finish();
            isOk = true;
            return;
        }
        this.ecgBean = ecgBean;
        Log.e(TAG,ecgBean.toString());
        //上传数据
        if(ecgP==null){
            layout_doit_data.setVisibility(View.VISIBLE);
            TextView textView = layout_doit_data.findViewById(R.id.text10);

            textView.setText("");
            ecgP = new UpdateEcgP(ecgBean,this,false);
            ecgP.start();
        }
                if(layout_doit_data!=null) {
            TextView textView = layout_doit_data.findViewById(R.id.text10);
            textView.setText("数据上传中...");
        }
        //上传流程
    }
    AnimImageView animImageView;
    @Override
    public void save(int length) {
        try {
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
            textView.setText(secToTime(length));
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }
    boolean isShow = false;
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
    @Override
    public void seq(int seq) {
        Log.d("TAG",seq+" >>> seq ");
        if(seq>=3) {
            seq -= 3;
            layout_ecg_b_status_sub.setVisibility(View.GONE);
            fastEcg.seq(seq);
        }else{
            ecgStatusSub.showS(seq);
        }
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
        if(!isShow) {
            isShow = true;
        }
        ecgDisplayer.setBuffer(buffer);
    }

    @Override
    public void setNoEcg(int eng, int leadoff, int hr, int temp) {
        ecgDisplayer.setNoEcg(eng,leadoff,hr,temp);
    }

    @Override
    public void next(String equVersion) {
        conManager.close();
        dataManager = new DataManager();
        dataManager.init(this, 0);
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
    EcgListP ecgListP;
    @Override
    public void success(String rid) {

        this.rid = rid;
        isOk = true;
        Log.e(TAG,rid+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        HttpProvider.doGet(GlobalConstants.APP_USER_GETREPORTNO+this.rid,null);
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
    public void showToast(String string){
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }
    boolean isOk;


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
                EcgActivity.super.finish();

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
