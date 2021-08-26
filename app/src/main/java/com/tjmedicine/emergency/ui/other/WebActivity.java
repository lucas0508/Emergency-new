package com.tjmedicine.emergency.ui.other;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.model.widget.CustomWebView;
import com.tjmedicine.emergency.ui.bean.SignUpBean;
import com.tjmedicine.emergency.ui.mine.apply.presenter.MineApplyPresenter;
import com.tjmedicine.emergency.ui.mine.apply.view.IMineApplyView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;

import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_URL;

/**
 * 特别注意：JS代码调用一定要在 onPageFinished（） 回调之后才能调用，否则不会调用。
 * onPageFinished()属于WebViewClient类的方法，主要在页面加载结束时调用
 */
public class WebActivity extends BaseActivity implements View.OnClickListener, IMineApplyView {

    private MineApplyPresenter mineApplyPresenter = new MineApplyPresenter(this);


    private static Bitmap bitmap;
    private static CopyOnWriteArrayList<Bitmap> bitmapList = new CopyOnWriteArrayList<>();
    @BindView(R.id.wv_view)
    CustomWebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.rl_base_layout)
    RelativeLayout mBaseLayout;
    @BindView(R.id.ib_close)
    ImageButton mClose;
    @BindView(R.id.btn_apply)
    Button btn_apply;


    private String mUrl;
    private int mFlag = 0;
    private int activity_id;
    /*区别跳转来的是 报名链接 or 直播链接 */
    private String mType = "";
    private String play_url;


    @Override
    protected void initView() {

        mTitle.setTextColor(Color.BLACK);
        mClose.setImageResource(R.mipmap.icon_back);

        btn_apply.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mApp.getLoadingDialog().show();
                mineApplyPresenter.signUp(String.valueOf(activity_id));
            }
        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_web;
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {

        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        // 设置支持JavaScript脚本
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true); // 允许webView执行JavaScript
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowFileAccess(true);//设置启用或禁止访问文件数据
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(false);// 支持缩放
        webSettings.setBuiltInZoomControls(false);// 设置出现缩放工具
        webSettings.setUseWideViewPort(false);// 扩大比例的缩放
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        /**
         * 自己处理点击事件,返回true
         */
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);   //在当前的webview中跳转到新的url
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();  // 接受信任所有网站的证书
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (mFlag == 1) {
                    mTitle.setText(title);
                } else if (mFlag == 2) {
                    mTitle.setText(title);
                } else {
                    mTitle.setText("详情");
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //显示进度条
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    //加载完毕隐藏进度条
                    mProgressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    @Override
    public void onClick(View v) {
        //android 调用 js 方法 获取返回值
        // 通过Handler发送消息
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                // 注意调用的JS方法名要对应上
                // 调用javascript的callJS()方法
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript("javascript:backToMain()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                } else {
                    mWebView.loadUrl("javascript:backToMain()");
                }
                String jsonParams = "123456";
                //String method = "jsMethod()";//不拼接参数，直接调用js的jsMethod函数
                String method = "jsMethod(" + jsonParams + ")";//拼接参数，就可以把数据传递给js
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript(method, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.i("qcl0228", "js返回的数据" + value);
                            Logger.d("JS返回的数据" + value);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mUrl = getIntent().getStringExtra(WEB_KEY_URL);
        Logger.d(mUrl);
        mFlag = getIntent().getIntExtra(Constants.WEB_KEY_FLAG, 0);
        mType = getIntent().getStringExtra(Constants.WEB_KEY_TYPE);
        activity_id = getIntent().getIntExtra("activity_id", 0);
        play_url = getIntent().getStringExtra("play_url");
        ;
        if (mUrl != null) {
            initWebView();
            mWebView.loadUrl(mUrl);
        } else {
            mApp.shortToast("网址加载失败！");
            finish();
        }

        //1.线下活动，2.直播，3.其他
        if (!TextUtils.isEmpty(mType) && mType.equals("1")) {
            btn_apply.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(mType) && mType.equals("2")) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();//返回上个页面
                    return;
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void findSignUpSuccess(List<SignUpBean> contactBean) {

    }

    @Override
    public void findSignUpFail(String info) {

    }

    @Override
    public void signUpSuccess() {
        mApp.getLoadingDialog().hide();
        mApp.shortToast("报名成功！");
        btn_apply.setEnabled(false);
    }

    @Override
    public void signUpFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }
}
