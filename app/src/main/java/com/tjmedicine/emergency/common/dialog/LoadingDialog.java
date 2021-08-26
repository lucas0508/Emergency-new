package com.tjmedicine.emergency.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;

import com.tjmedicine.emergency.R;


/**
 * @author QiZai
 * @desc
 * @date 2018/5/2 11:12
 */

public class LoadingDialog extends Dialog {
    private Context mContext;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private View mView;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        mBuilder = new AlertDialog.Builder(mContext);
        mView = getLayoutInflater().inflate(R.layout.dialog_loading, null);

        mBuilder.setView(mView);
        mDialog = mBuilder.setCancelable(false).create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void show() {
        mDialog.show();
    }

    public void hide() {
        mDialog.dismiss();
    }

    public interface DelayedCallback {
        void callback();
    }
}
