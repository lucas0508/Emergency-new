package com.tjmedicine.emergency.ui.teach.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;


public class test extends BaseActivity {


    private TextView textView;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.atest;
    }

    @Override
    protected void initView() {
        String tableHtml = "<p><p><p></p><p></p><p " +
                "style=\\\"margin: 5px 6px 16px; padding: 0px; max-width: 100%;" +
                " clear: both; min-height: 1em; text-align: justify; " +
                "text-indent: 0em; border-width: 0px; border-style: initial; border-color: initial; line-height: 2em; overflow-wrap: break-word !important;\\\"><span style=\\\"color: rgb(34, 34, 34); font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; letter-spacing: 1px;\\\">众所周知，</span><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; color: rgb(34, 34, 34); font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; letter-spacing: 1px; overflow-wrap: break-word !important;\\\">冠心病患者家里或者口袋里都会备有一种救命药，那就是硝酸甘油。因为冠心病患者在心绞痛急性发作时，如果不能快速缓解，那很可能会危及生命。而硝酸甘油就是他们的急救药。</span><img src=\\\"http://39.104.170.185:8080/profile/upload/2021/01/15/b7bd9365-7c9e-4e77-917f-3e862de3caee.jpg\\\" style=\\\"width: 448px;\\\"><span style=\\\"letter-spacing: 1px; color: rgb(65, 59, 56); font-size: 19px; font-weight: bold; font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif;\\\">下面这6类人群禁用硝酸甘油</span><br></p><section style=\\\"margin: 0px; padding: 0px; max-width: 100%; color: rgb(51, 51, 51); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 17px; letter-spacing: 0.544px; overflow-wrap: break-word !important;\\\"><section data-width=\\\"30%\\\" style=\\\"margin: 6px 0px 0px; padding: 0px; max-width: 100%; width: 192.891px; height: 2px; background: rgb(128, 100, 162); overflow: hidden; overflow-wrap: break-word !important;\\\"><br style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"></section></section><section data-autoskip=\\\"1\\\" style=\\\"margin: 1.5em 0px 0px; padding: 0px; max-width: 100%; font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; text-align: justify; line-height: 1.75em; letter-spacing: 1.5px; font-size: 14px; color: rgb(65, 58, 56); overflow-wrap: break-word !important;\\\"><section data-role=\\\"paragraph\\\" style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"><p style=\\\"margin-bottom: 0px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; overflow-wrap: break-word !important;\\\"><br style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"></p></section><section data-tools=\\\"135编辑器\\\" data-id=\\\"101324\\\" style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"><section style=\\\"margin: 10px auto; padding: 0px; max-width: 100%; text-align: left; overflow-wrap: break-word !important;\\\"><section style=\\\"margin: 0px; padding: 0px 0px 0px 1em; max-width: 100%; border-left: 1px solid rgb(204, 193, 217); overflow-wrap: break-word !important;\\\"><section style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"><section style=\\\"margin: 0px; padding: 0px; max-width: 100%; display: inline-block; overflow-wrap: break-word !important;\\\"><section data-brushtype=\\\"text\\\" style=\\\"margin: 0px; padding: 4px 1em; max-width: 100%; color: rgb(255, 255, 255); font-weight: bold; background: rgb(178, 162, 199); overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; overflow-wrap: break-word !important;\\\">一、部分心肌梗死</span></section></section></section></section></section></section></section><img src=\\\"http://39.104.170.185:8080/profile/upload/2021/01/15/53d4ae48-3b4a-4acc-9a27-2de1c05bc34a.jpg\\\" style=\\\"width: 448px;\\\"><span style=\\\"font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; background-color: rgb(253, 250, 246); color: rgb(34, 34, 34); letter-spacing: 1px; text-align: justify; text-indent: 0em;\\\">网上流传，急性心肌梗死要第一时间含服硝酸甘油，这是不负责任的。</span><p></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\">首先急性右室、下壁心肌梗死常常伴随低血压，这时候硝酸甘油是绝对不能使用的；还有部分急性广泛前壁心肌梗死合并心源性休克，血压也非常低了，当然也不能使用硝酸甘油。</span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\">这时候需要第一时间抗血小板治疗，开通血管。</span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\"><span style=\\\"color: rgb(255, 255, 255); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-weight: 700; letter-spacing: 1.5px; background-color: rgb(178, 162, 199);\\\">二、低血压</span><br></span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; overflow-wrap: break-word !important;\\\"><img src=\\\"http://39.104.170.185:8080/profile/upload/2021/01/15/f95ea442-1aff-4029-bb4a-61a051f4dff1.jpg\\\" style=\\\"width: 448px;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\"><br></span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; text-indent: 0em; overflow-wrap: break-word !important;\\\">一般来说，</span><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; text-indent: 0em; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-weight: 700; overflow-wrap: break-word !important;\\\">收缩压低于90，舒张压低于60就为低血压</span>，</span><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; text-indent: 0em; overflow-wrap: break-word !important;\\\">如果血压进一步降低，</span><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; text-indent: 0em; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-weight: 700; overflow-wrap: break-word !important;\\\">收缩压低于80，舒张压低于50，甚至更低，那一般是严重低血压</span>，</span><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; text-indent: 0em; overflow-wrap: break-word !important;\\\">这些严重低血压的患者本来就容易出现头晕、晕厥等危险。</span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; text-indent: 0em; overflow-wrap: break-word !important;\\\">如果这些严重低血压的患者再服用硝酸甘油，扩张血管，使血压进一步下降，那很容易就会由于缺血缺氧而昏厥，从而危及生命。</span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; text-indent: 0em; overflow-wrap: break-word !important;\\\">所以有严重低血压的患者一定要禁止使用硝酸甘油，要不然会导致心肌梗死而更危及人的生命。</span></p><div><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; text-indent: 0em; overflow-wrap: break-word !important;\\\"><span style=\\\"color: rgb(255, 255, 255); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-weight: bold; letter-spacing: 1.5px; text-align: justify; background-color: rgb(178, 162, 199);\\\">三、青光眼</span><br></span></div><p></p><p style=\\\"margin-bottom: 0px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; color: rgb(51, 51, 51); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 17px; letter-spacing: 0.544px; text-align: justify; overflow-wrap: break-word !important;\\\"><img src=\\\"http://39.104.170.185:8080/profile/upload/2021/01/15/18a2ac03-cca2-4fe4-8a0c-9c96a6299a4b.jpg\\\" style=\\\"width: 448px;\\\"><span style=\\\"font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; color: rgb(34, 34, 34); letter-spacing: 1px; text-indent: 0em; background-color: rgb(253, 250, 246);\\\">这是了解硝酸甘油的第一课，我们每个使用硝酸甘油的医生脑子都有一个列表，其中清清楚楚写着“青光眼”禁用。</span><br></p><p></p><section hm_fix=\\\"332:424\\\" style=\\\"margin: 1em 0px 0px; padding: 1em; max-width: 100%; color: rgb(78, 62, 58); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 14px; letter-spacing: 1.5px; text-align: justify; background: rgb(253, 250, 246); overflow-wrap: break-word !important;\\\"><section data-autoskip=\\\"1\\\" style=\\\"margin: 0px; padding: 0px; max-width: 100%; line-height: 1.75em; overflow-wrap: break-word !important;\\\"><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; cursor: text; border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; text-indent: 0em; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; outline: none 0px !important; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\">对于青光眼患者，尤其是那些未行手术治疗的患者来说，如果使用硝酸甘油会导致病情加重，出现头痛、恶心、眼部胀痛等副作用。我们遇到青光眼朋友发作心绞痛只能使用其他药物代替；但如果只有硝酸甘油有效，这时候需要和家属交代清楚，有所舍弃，先保心脏。</span></p></section></section><p style=\\\"margin-bottom: 0px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; color: rgb(78, 62, 58); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 14px; letter-spacing: 1.5px; text-align: justify; background-color: rgb(253, 250, 246); overflow-wrap: break-word !important;\\\"><br style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"></p><section style=\\\"margin: 0px; padding: 0px; max-width: 100%; color: rgb(78, 62, 58); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 14px; letter-spacing: 1.5px; text-align: justify; background-color: rgb(253, 250, 246); display: inline-block; overflow-wrap: break-word !important;\\\"><section data-brushtype=\\\"text\\\" style=\\\"margin: 0px; padding: 4px 1em; max-width: 100%; color: rgb(255, 255, 255); font-weight: bold; background: rgb(178, 162, 199); overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; overflow-wrap: break-word !important;\\\">四、颅内压高</span></section></section><img src=\\\"http://39.104.170.185:8080/profile/upload/2021/01/15/0255ceab-d885-46fe-b17f-414dac2d587f.jpg\\\" style=\\\"width: 448px;\\\"><span style=\\\"font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; color: rgb(34, 34, 34); letter-spacing: 1px; text-indent: 0em; background-color: rgb(253, 250, 246); text-align: justify;\\\">对于很多存在颅压升高的患者来说，不能使用硝酸甘油，因为硝酸甘油扩张头部血管，造成病情的加重。</span><p style=\\\"margin-bottom: 0px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; color: rgb(78, 62, 58); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 14px; letter-spacing: 1.5px; text-align: justify; background-color: rgb(253, 250, 246); overflow-wrap: break-word !important;\\\"><br style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"></p><section style=\\\"margin: 0px; padding: 0px; max-width: 100%; color: rgb(78, 62, 58); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 14px; letter-spacing: 1.5px; text-align: justify; background-color: rgb(253, 250, 246); display: inline-block; overflow-wrap: break-word !important;\\\"><section data-brushtype=\\\"text\\\" style=\\\"margin: 0px; padding: 4px 1em; max-width: 100%; color: rgb(255, 255, 255); font-weight: bold; background: rgb(178, 162, 199); overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; overflow-wrap: break-word !important;\\\">五、肥厚梗阻性心肌病</span></section></section><img src=\\\"http://39.104.170.185:8080/profile/upload/2021/01/15/11fba0d5-ab8b-4ea7-8ad7-0f4f66ee0c47.jpg\\\" style=\\\"width: 448px;\\\"><span style=\\\"font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 17px; color: rgb(34, 34, 34); letter-spacing: 1px; text-indent: 0em; background-color: rgb(253, 250, 246); text-align: justify;\\\">梗阻的肥厚性心肌病患者，一定不能使用硝酸甘油，肥厚型梗阻性心肌病，以左室血液充盈而受阻，舒张期顺应性下降为基本的病态。使用硝酸甘油后降低心脏后负荷，使左室充盈进一步下降，左室泵血减少；降低前负荷，使左室泵血时流出道压力阶差增大，负压效应增强，梗阻加重，也使左室泵血减少，病情恶化。</span><p></p></p><p style=\\\"margin-bottom: 0px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; color: rgb(78, 62, 58); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 14px; letter-spacing: 1.5px; text-align: justify; background-color: rgb(253, 250, 246); overflow-wrap: break-word !important;\\\"><br style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"></p><section style=\\\"margin: 0px; padding: 0px; max-width: 100%; color: rgb(78, 62, 58); font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: 14px; letter-spacing: 1.5px; text-align: justify; background-color: rgb(253, 250, 246); display: inline-block; overflow-wrap: break-word !important;\\\"><section data-brushtype=\\\"text\\\" style=\\\"margin: 0px; padding: 4px 1em; max-width: 100%; color: rgb(255, 255, 255); font-weight: bold; background: rgb(178, 162, 199); overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; overflow-wrap: break-word !important;\\\">六、硝酸甘油过敏</span></section></section></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); cursor: text; border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; outline: none 0px !important; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\">每个人对药物的敏感性不同的，尤其是有些人会对一些药品过敏，这时候是不能使用该药品的。</span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); cursor: text; border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; outline: none 0px !important; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\">药物过敏轻症者可能只是导致皮肤瘙痒、红肿等，但重者有可能导致四肢抽搐、休克等，所以不可大意。</span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); cursor: text; border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; outline: none 0px !important; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\"></span></p><p style=\\\"margin: 16px 6px; padding: 0px; max-width: 100%; clear: both; min-height: 1em; text-align: justify; text-indent: 0em; background-color: rgb(253, 250, 246); cursor: text; border-width: 0px; border-style: initial; border-color: initial; color: rgb(34, 34, 34); font-size: 16px; line-height: 2em; letter-spacing: 1px; font-family: &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, &quot;Helvetica Neue&quot;, Arial, sans-serif; outline: none 0px !important; overflow-wrap: break-word !important;\\\"><strong style=\\\"margin: 0px; padding: 0px; max-width: 100%; overflow-wrap: break-word !important;\\\"><span style=\\\"margin: 0px; padding: 0px; max-width: 100%; font-size: 17px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; overflow-wrap: break-word !important;\\\">是药三分毒，虽然硝酸甘油是心绞痛急性发作的救命药，但以上这6种人群是禁止使用的，所以一定要注意。用药要按医嘱，要根据个人的情况来进行合理用药，而不能自己擅自使用，要不然可能不是救命而是害命。</span></strong></p>";

        textView = findViewById(R.id.textView);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String sample = "<h2>Hello wold</h2>"
                + "<font size=\"5\" color=\"#FF0000\">Font size</font>"
                + "<img src=\"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3517187993,901832070&fm=15&gp=0.jpg\"/>";

        RichText.initCacheDir(this);
        RichText.debugMode = true;
        RichText.fromHtml(sample)
                .urlClick(new OnUrlClickListener() {
                    @Override
                    public boolean urlClicked(String url) {
                        if (url.startsWith("code://")) {
                            Toast.makeText(test.this, url.replaceFirst("code://", ""), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .into(textView);

//        HtmlText.from(sample)
//                .setImageLoader(new HtmlImageLoader() {
//                    @Override
//                    public void loadImage(String url, final Callback callback) {
//                        // Glide sample, you can also use other image loader
//
//                        Glide.with(test.this)
//                                .load(url)
//                                .into(new SimpleTarget<Drawable>() {
//                                    @Override
//                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                        callback.onLoadComplete(drawableToBitmap(resource));
//                                    }
//
//                                    @Override
//                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                                        callback.onLoadFailed();
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public Drawable getDefaultDrawable() {
//                        return ContextCompat.getDrawable(test.this, R.drawable.ic_launcher);
//                    }
//
//                    @Override
//                    public Drawable getErrorDrawable() {
//                        return ContextCompat.getDrawable(test.this, R.drawable.ic_launcher);
//                    }
//
//                    @Override
//                    public int getMaxWidth() {
//                        return getTextWidth();
//                    }
//
//                    @Override
//                    public boolean fitWidth() {
//                        return false;
//                    }
//                })
//                .setOnTagClickListener(new OnTagClickListener() {
//                    @Override
//                    public void onImageClick(Context context, List<String> imageUrlList, int position) {
//                        // image click
//                    }
//
//                    @Override
//                    public void onLinkClick(Context context, String url) {
//                        // link click
//                    }
//                })
//                .into(textView);
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private int getTextWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.widthPixels - textView.getPaddingLeft() - textView.getPaddingRight();
    }
}
