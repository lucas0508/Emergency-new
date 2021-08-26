package com.tjmedicine.emergency.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.utils.SoundPlayUtils;


/**
 * Created by SXJ on 2017/2/28 0028.
 */

public class TaskSucDialog extends Dialog {

    private TextView taskRestart, tv_report_pd, tv_report_pd_regular, tv_pd_30_2,
            tv_pd_time, tv_test_result, tv_pd_rebound, tv_report_pd_regular_hg, tv_report_pd_regular_z;
    private ImageView taskClose;
    private AlertDialog mDialog;
    /*缩放动画*/
    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
    public Spring mScaleSpring;
    private Handler handler;

    public TaskSucDialog(Context context) {
        super(context);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.task_access_dialog, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.custom_alert_dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        mDialog = builder.create();
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.horizontalMargin = 0.6f;
        mDialog.getWindow().setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(false);

        taskClose = (ImageView) view.findViewById(R.id.task_close);
        taskRestart = (TextView) view.findViewById(R.id.task_restart);
        tv_report_pd = (TextView) view.findViewById(R.id.tv_report_pd);
        tv_report_pd_regular = (TextView) view.findViewById(R.id.tv_report_pd_regular);
        tv_pd_30_2 = (TextView) view.findViewById(R.id.tv_pd_30_2);
        tv_pd_time = (TextView) view.findViewById(R.id.tv_pd_time);
        tv_pd_rebound = view.findViewById(R.id.tv_pd_rebound);
        tv_report_pd_regular_hg = view.findViewById(R.id.tv_report_pd_regular_hg);
        tv_report_pd_regular_z = view.findViewById(R.id.tv_report_pd_regular_z);
        tv_test_result = (TextView) view.findViewById(R.id.tv_test_result);


        mScaleSpring = mSpringSystem.createSpring();
        handler = new Handler();
        setContentView(view);
        setCancelable(false);
        show();
    }


    public void initData(String report_pd, String report_pd_regular, String pd_30_2, String pd_time, String pd_rebound, String result) {
        //按压频率---：5/分
        //按压深度合格率---：0%
        //30:2---：不合格
        //两次按压时间差：不合格
        //pd_rebound 回弹
        if (Double.parseDouble(report_pd) > 99 && Double.parseDouble(report_pd) < 121) {
            tv_report_pd.setTextColor(Color.GREEN);
        } else {
            tv_report_pd.setTextColor(Color.RED);
        }
        String[] split = report_pd_regular.split("\\|");

        tv_report_pd_regular_hg.setText("(1).按压合格次数:" + split[0]);
        tv_report_pd_regular_z.setText("(2).按压总次数:" + split[1]);

        if (Double.parseDouble(split[2]) > 80) {
            tv_report_pd_regular.setTextColor(Color.GREEN);
        } else {
            tv_report_pd_regular.setTextColor(Color.RED);
        }

        if (Double.parseDouble(pd_rebound) > 80) {
            tv_pd_rebound.setTextColor(Color.GREEN);
        } else {
            tv_pd_rebound.setTextColor(Color.RED);
        }

        if (pd_30_2.equals("合格")) {
            tv_pd_30_2.setTextColor(Color.GREEN);
        } else {
            tv_pd_30_2.setTextColor(Color.RED);
        }
        if (pd_time.equals("合格")) {
            tv_pd_time.setTextColor(Color.GREEN);
        } else {
            tv_pd_time.setTextColor(Color.RED);
        }
        //按压频率为
        tv_report_pd.setText(report_pd + "次/分");
        //按压深度合格率为
        tv_report_pd_regular.setText(split[2] + "%");
        //人工呼吸的比例
        tv_pd_30_2.setText(pd_30_2);
        //胸廓完全回弹合格率为
        tv_pd_rebound.setText(pd_rebound + "%");
        //时间差
        tv_pd_time.setText(pd_time);

        tv_test_result.setText(result);
    }


    public void setTaskClose(View.OnClickListener clickListener) {
        taskClose.setOnClickListener(clickListener);
    }


//    public void setTaskScore(Double score) {
//        taskScore.setText(score + "");
//        if (score < 90) {
//            taskGrade.setText("Good !");
//            taskStar2.setImageResource(R.mipmap.task_star_dark);
//            taskStar3.setImageResource(R.mipmap.task_star_dark);
//            setSound(1);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    taskStar1.setImageResource(R.mipmap.task_star_light);
//                    AnimUtil.starAnim(taskStar1);
//                }
//            }, 500);
//        } else if (score >= 90 && score <= 95) {
//            taskGrade.setText("Great !");
//            taskStar3.setImageResource(R.mipmap.task_star_dark);
//            setSound(2);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    taskStar1.setImageResource(R.mipmap.task_star_light);
//                    AnimUtil.starAnim(taskStar1);
//                }
//            }, 500);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    taskStar2.setImageResource(R.mipmap.task_star_light);
//                    AnimUtil.starAnim(taskStar2);
//                }
//            }, 1000);
//        } else {
//            taskGrade.setText("Perfect !!");
//            setSound(3);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    taskStar1.setImageResource(R.mipmap.task_star_light);
//                    AnimUtil.starAnim(taskStar1);
//                }
//            }, 500);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    taskStar2.setImageResource(R.mipmap.task_star_light);
//                    AnimUtil.starAnim(taskStar2);
//                }
//            }, 1000);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    taskStar3.setImageResource(R.mipmap.task_star_light);
//                    AnimUtil.starAnim(taskStar3);
//                }
//            }, 1500);
//        }
//    }

    private void setSound(int num) {
        for (int i = 0; i < num; i++) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SoundPlayUtils.play(4);
                }
            }, 500 * (i + 1));
        }
    }

    public void setTaskRestart(View.OnClickListener clickListener) {
        taskRestart.setOnClickListener(clickListener);
    }


    private class ExampleSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
            taskClose.setScaleX(mappedValue);
            taskClose.setScaleY(mappedValue);
        }
    }

    @Override
    public void show() {
        super.show();
        mScaleSpring.addListener(mSpringListener);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mScaleSpring.removeListener(mSpringListener);
    }
}

