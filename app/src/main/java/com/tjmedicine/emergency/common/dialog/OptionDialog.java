package com.tjmedicine.emergency.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.R;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/2 11:12
 */
public class OptionDialog extends Dialog {

    private AlertDialog mDialog;
    private View mView;
    private Context mContext;
    private ImageButton mClose;
    private TextView mTitle;
    private ListView mOption;
    private OptionAdapter mOptionAdapter;
    private Callback mCallback;
    private LinearLayout mDialogContent;

    public OptionDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        this.mView = getLayoutInflater().inflate(R.layout.dialog_option, null);
        initView();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(mView);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    //在dialog.show()之后调用
    private void setDialogWindowAttr(Dialog dlg) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER_VERTICAL;
//        lp.width = 400;//宽高可设置具体大小
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;//宽高可设置具体大小
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(lp);
    }

    private void initView() {
        mClose = mView.findViewById(R.id.ib_close);
        mTitle = mView.findViewById(R.id.tv_title);
        mOption = mView.findViewById(R.id.lv_option);
        mDialogContent = mView.findViewById(R.id.ll_dialog_content);
        mOptionAdapter = new OptionAdapter(mContext);
        mOption.setAdapter(mOptionAdapter);

        mOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismissDialog(position);
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(-1);
            }
        });
    }

    public void show(@NonNull String title, @NonNull String[] options, Callback callback) {
        mTitle.setText(title);
        mCallback = callback;
        mOptionAdapter.refresh(options);
        showDialog();
    }

    private void showDialog() {
        mDialog.show();
        // setDialogWindowAttr(mDialog);
        mView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha_in));
        mDialogContent.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.scale_in));
    }

    private void dismissDialog(final int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_out);
        mDialogContent.startAnimation(animation);
        mView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha_out));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (position != -1 && mCallback != null)
                    mCallback.clickItem(position);
                mDialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public interface Callback {
        void clickItem(int position);
    }

    private class OptionAdapter extends BaseAdapter {

        private Context mContext;
        private String[] mDataArray = {};

        OptionAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mDataArray.length;
        }

        @Override
        public String getItem(int position) {
            return mDataArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_option, null);
            }
            TextView text = view.findViewById(R.id.tv_text);
            ImageView image = view.findViewById(R.id.iv_image);
//            Log.e("TAG", "getView: " + getItem(position));
           // Glide.with(EmergencyApplication.getContext()).load(getItem(position)).into(image);
            if (position == 0) {
                text.setText("练习模式");
                image.setImageResource(R.mipmap.pop_practice);
            }
            else if (position == 1) {
                text.setText("测试模式");
                image.setImageResource(R.mipmap.pop_test);

            }
            else if (position == 2) {
                text.setText("考试模式");
                image.setImageResource(R.mipmap.pop_test);

            }
            return view;
        }

        public void refresh(String[] options) {
            if (options != null) {
                mDataArray = options;
                notifyDataSetChanged();
            }
        }
    }
}
