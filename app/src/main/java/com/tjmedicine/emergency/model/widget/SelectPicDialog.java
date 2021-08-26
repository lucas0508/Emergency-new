package com.tjmedicine.emergency.model.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tjmedicine.emergency.R;


/**
 * @author : Xrf
 * @date : 2019/3/2414:47
 * desc   :选择相册弹出框
 */
public class SelectPicDialog extends Dialog {
    private Callback mCallback;

    private View view;
    private Context context;

    //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    public SelectPicDialog(Context context) {
        super(context, R.style.SelectPicDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectpic);//这行一定要写在前面
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        //拍照
        findViewById(R.id.tvTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(0);
            }
        });


        //选择照片
        findViewById(R.id.tvSelectPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(1);
            }
        });


        findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(-1);
            }
        });
    }

    private void dismissDialog(final int position) {
        if (position != -1 && mCallback != null)
            mCallback.clickItem(position);
        dismiss();
    }

    public interface Callback {
        void clickItem(int position);
    }

    public void show(Callback callback) {
        mCallback = callback;
        show();
    }
}

