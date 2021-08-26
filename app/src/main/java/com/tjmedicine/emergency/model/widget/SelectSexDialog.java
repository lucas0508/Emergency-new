package com.tjmedicine.emergency.model.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.tjmedicine.emergency.R;

import java.util.Arrays;

/**
 * @author : Xrf
 * @date : 2019/3/2414:47
 * desc   :选择性别弹出框
 */
public class SelectSexDialog extends Dialog {
    private static final String[] PLANETS = new String[]{"男", "女", "保密"};
    private Context context;
    private SelectSexWheelView selSex;

    public SelectSexDialog(Context context) {
        super(context, R.style.SelectPicDialog);
        this.context = context;

    }

    public interface SelectSexListener {
        void setSelectSex(String str, String sex);
    }

    SelectSexListener selectSexListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectsex);//这行一定要写在前面

        selSex = findViewById(R.id.selSex);
        TextView txt_confirm = findViewById(R.id.txt_confirm);
        TextView txt_cancel = findViewById(R.id.txt_cancel);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        selSex.setOffset(1);
        selSex.setItems(Arrays.asList(PLANETS));
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seletedItem = selSex.getSeletedItem();
                String sex;
                if (seletedItem.equals("男")){
                    sex="1";
                }else if (seletedItem.equals("女")){
                    sex="2";
                }else {
                    sex="0";
                }
                selectSexListener.setSelectSex(seletedItem,sex);
                dismiss();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(SelectSexListener selectSexListener) {
        this.selectSexListener=selectSexListener;
        show();
    }
}

