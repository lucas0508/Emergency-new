package com.tjmedicine.emergency.ui.other;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;

/**
 * 问题反馈
 */
public class AccessFeedbackActivity extends BaseActivity {

    private static final String MAX_LENGTH = "235";
    private EditText mContent;
    private TextView mNumber;
    private Button mSubmit;

    @BindView(R.id.tv_title)
    TextView mTitle;
    private Map<String, Object> mFormData = new HashMap<>();
    @BindView(R.id.ib_close)
    ImageButton mClose;

    @Override
    protected int setLayoutResourceID() {

        return R.layout.activity_access_feedback;
    }

    @Override
    protected void initView() {
        mTitle.setText("问题反馈");


        mContent = findViewById(R.id.et_content);
        mSubmit = findViewById(R.id.b_submit);
        mNumber = findViewById(R.id.tv_number);

        mSubmit.setOnClickListener(v -> submitFormData());

        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = mContent.getText().toString();
                mNumber.setText("(" + content.length() + "/"
                        + MAX_LENGTH + ")");
            }
        });

        mClose.setOnClickListener(v -> finish());
    }


    private void submitFormData() {

        String content = mContent.getText().toString().trim();
        if (content.isEmpty() || content.length() <= 5) {
            mApp.shortToast("反馈详情不能为空且必须大于5个字符！");
            return;
        }
        mFormData.put("content", content);
        submit();
    }

    private void submit() {
        String url = GlobalConstants.USER_ADUSERFEEDBACK;
        HttpProvider.doJson(url, mFormData, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                ResponseEntity res = GsonUtils.parseJsonWithClass(responseText, new TypeToken<ResponseEntity>() {
                }.getType());
                if (res.getCode() >= 200 && res.getCode() < 300) {
                    mApp.shortToast("感谢您的反馈，您的反馈将是我们不断进步的动力！");
                    finish();
                } else {
                    mApp.shortToast(res.getMsg());
                }
            }
        });
    }
}

