package com.helowin.sdk


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.helowin.sdk.cache.TargetBean
import com.helowin.sdk.cache.XEcgDetialBean
import com.helowin.sdk.cache.SelectEcgInfo
import com.helowin.sdk.net.EcgDetailP
import com.squareup.picasso.Picasso
import com.tjmedicine.emergency.R
import com.tjmedicine.emergency.common.base.BaseActivity
import com.tjmedicine.emergency.ui.device.ecg.helowin.sdk.HtmlTagHandler
import kotlinx.android.synthetic.main.include_new_analyze_result_success_x.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EcgDetailAct : BaseActivity(), EcgDetailP.EcgDetailV {
    fun back(view: View?) {
        finish()
    }

    override fun setLayoutResourceID(): Int = R.layout.act_v237_ecg_detail

    private fun toInt(v: String?): Int {
        return v?.toInt() ?: 0
    }
    override fun initView() {
        setTitle("心电详情")
        var se = intent.getSerializableExtra("TAG") as SelectEcgInfo.SelectEcg
        ecgUrl = se?.ecgUrl ?: "";

        edp = EcgDetailP(
            this,
            se?.uuid ?: 532785.toString()
        )//EcgDetailP(this,EcgConfigs.newInstance().url,intent.getStringExtra("ID"))
        edp.start()
        initData()
        ListDate = ArrayList()
        mTargetAdapater = TargetAdapater(ListDate!!)
//        var t = act_rl_view_list_x as RecycleListView
        act_rl_view_list_x.adapter = mTargetAdapater
        act_rl_view_list_x.layoutManager = LinearLayoutManager(this)
        include_impress_bianyixingzhishu_x.setOnClickListener {
//            EcgWebAct.start(this, "心率变异性指数", "http://api.995120.cn/static/hm503/news/single/ecgUserAPP/ecgVariation.html")
            webView("http://api.995120.cn/static/hm503/news/single/ecgUserAPP/ecgVariation.html")
            include_impress_heart_x.setOnClickListener {
                include_impress_heart_text.measure(0, 0)
                var lp = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(include_impress_heart_text.width, 0, 0, 0);
                PopUpWindowsTip.ShowPopUpWindowsTipMid(
                    this,
                    R.layout.pop_show_tip_view,
                    include_impress_heart_x,
                    lp
                )
            }
            xindianceliangzhibiao.setOnClickListener {

                xindianceliangzhibiao_x.measure(0, 0)
                var lp = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(xindianceliangzhibiao_x.width, 0, 0, 0);
                PopUpWindowsTip.ShowPopUpWindowsTipMid(
                    this,
                    R.layout.pop_show_tip_view_zhibiao,
                    xindianceliangzhibiao,
                    lp
                )
            }

        }

    }
    private fun initHRV(ecg: XEcgDetialBean) {
        act_result_id0.setTitle("心率变异性指数", toInt(ecg.hrv_value))
        act_result_id0.setValue(ecg.hrv)
        act_result_id1.setTitle("心脏疾病风险评估", toInt(ecg.hdrisk_value))
        act_result_id1.setValue(ecg.hdrisk)
        act_result_id2.setTitle("精神压力指数", toInt(ecg.mental_value))
        act_result_id2.setValue(ecg.mentalPressure)
        act_result_id3.setTitle("疲劳指数", toInt(ecg.fatigue_value))
        act_result_id3.setValue(ecg.fatigue)
    }


    fun init(editText: TextView, type: Int) {
        if (type < 102) {
            init(type.toString(), -1, editText)
        } else if (type > 180) {
            init(type.toString(), 1, editText)
        } else {
            init(type.toString(), 0, editText)
        }
    }

    fun init(str: String?, type: Int, editText: TextView) {
        editText.text = str
        if (type == 1) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_dataup, 0)
        } else if (type == -1) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_datadown, 0)
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    fun initSuccess(mEcgReportInfo: XEcgDetialBean) {
        allHRV.visibility = View.GONE
        arrow.visibility = View.GONE
        after_pay_view0.visibility = View.VISIBLE

        clzhibiaocount.visibility = View.GONE
        var mTargetBean = mEcgReportInfo.extList
        if (mTargetBean != null && !mTargetBean.isEmpty()) {
            var str = String.format("%d项异常", mTargetBean.size)
            clzhibiaocount.text = str
        } else {
            mTargetBean = ArrayList<TargetBean>()
            clzhibiaocount.text = "正常"
        }
        var temp = mEcgReportInfo.normalList
        if (temp != null) {
            mTargetBean?.addAll(temp)
        }
        if (!TextUtils.isEmpty(mEcgReportInfo.fileImagePath)) {
            Picasso.with(this).load(mEcgReportInfo.fileImagePath)
                .into(include_new_analyze_result_success_ecg_pic_x)
        }
        mTargetAdapater?.setDate(mTargetBean!!)
        include_new_analyze_result_success_ecg_pic_x.setOnClickListener { jump(true) }
        include_new_analyze_result_success_ecg_length_x.text =
            "时长:" + SelectEcgInfo.SelectEcg.getLength(mEcgReportInfo.length)
//        XApp.setImage(mEcgReportInfo.fileImagePath, include_new_analyze_result_success_ecg_pic_x, 0)
        mDrawText(
            this,
            include_new_analyze_result_success_ecg_heart_x,
            mEcgReportInfo.heartRate.toString(),
            " bpm",
            "平均心率",
            null
        )
        include_new_analyze_result_success_ecg_heart_ecg_result_x.text = mEcgReportInfo.ecgResultTz
        include_new_analyze_result_success_title_x.text = mEcgReportInfo.title
        include_new_analyze_result_success_heart_rate_x.text =
            mEcgReportInfo.heartRate.toString() + " bpm"
        include_new_analyze_result_success_test_time_x.text = getbir(mEcgReportInfo.takeTime)
        mDrawText(
            this,
            include_new_analyze_result_success_ecg_heart_max_x,
            mEcgReportInfo?.max.toString(),
            " bpm",
            "最高心率",
            null
        )
        mDrawText(
            this,
            include_new_analyze_result_success_ecg_heart_min_x,
            mEcgReportInfo?.min.toString(),
            " bpm",
            "最低心率",
            null
        )
        mDrawText(
            this,
            include_new_analyze_result_success_ecg_heart_normal_x,
            mEcgReportInfo?.normalRate.toString(),
            " %",
            "正常心率",
            "(60~100bpm)"
        )
        mDrawText(
            this,
            include_new_analyze_result_success_ecg_heart_quick_x,
            mEcgReportInfo?.heartbeatRate.toString(),
            " %",
            "心率偏快",
            "(>100bpm)"
        )
        mDrawText(
            this,
            include_new_analyze_result_success_ecg_heart_slow_x,
            mEcgReportInfo?.slowRate.toString(),
            " %",
            "心率偏慢",
            "(&lt;60bpm)"
        )
    }

    fun getbir(dr: String?): String {
        val d =
            Date(dr?.toLong() ?: System.currentTimeMillis())
        // 格式化日期
        val s = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return s.format(d)
    }

    fun initPlayAfter(mEcgReportInfo: XEcgDetialBean) {
        initHRV(mEcgReportInfo)
        /* hrv_value.text = mEcgReportInfo.hrv_value
         hdrisk_value.text = mEcgReportInfo.hdrisk_value
         mental_value.text = mEcgReportInfo.mental_value
         fatigue_value.text = mEcgReportInfo.fatigue_value*/
        init(allHRV, mEcgReportInfo.hrv_value?.toInt() ?: 0)
//        allHRV.setOnClickListener({
//            var intent = Intent(this,HRVAct::class.java)
//            intent.putExtra("TAG",mEcgReportInfo)
//            startActivity(intent)
//        })
//        arrow.setOnClickListener({
//            var intent = Intent(this,HRVAct::class.java)
//            intent.putExtra("TAG",mEcgReportInfo)
//            startActivity(intent)
//        })
    }



    fun jump(isWebView: Boolean = false) {

//            intent3 = if (isWebView){
//                Intent(this, PdfAct1::class.java)
//            }else {
//                Intent(this, PdfAct::class.java)
//            }
//            intent3.putExtra("path", ecgUrl)
//            intent3.putExtra("time", time)
//            startActivity(intent3)


        if (!TextUtils.isEmpty(ecgUrl)) {
            webView(ecgUrl!!)
//                EcgWebAct.start(this, "查看心电图",ecgUrl!!)

        } else {
//                XApp.showToast("心电图链接不存在")
        }
    }

    var mTagets: ArrayList<TargetBean>? = ArrayList()
    var mTargetAdapater: TargetAdapater? = null
    var ListDate: ArrayList<TargetBean>? = null
    val DATA = "data"
    private fun initData() {
    }


    var ecgUrl: String? = null



    fun webView(url: String) {

        var uri = Uri.parse(url);
        var intent = Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    lateinit var edp: EcgDetailP


    override fun success(r: XEcgDetialBean) {

        if (TextUtils.isEmpty(ecgUrl)) {
            ecgUrl = r.ecgUrl
        }
        runOnUiThread {
            table_layout_hrv.visibility = View.VISIBLE
            initPlayAfter(r)
            if (r.title.equals("疑似异常")) {
                include_new_analyze_result_success_title_x.setTextColor(Color.parseColor("#e95f5f"))
            } else {
                include_new_analyze_result_success_title_x.setTextColor(Color.parseColor("#333333"))
            }
            initSuccess(r)
        }
    }

    override fun fail() {

    }

    companion object {
        private fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        fun mDrawText(
            context: Context,
            mTitle: TextView,
            data: String,
            Content: String,
            NextLine: String,
            info: String?
        ) {


            val s14sp = "<myfont color='#333333'size='" + sp2px(context, 14f) + "'>"
            val s18sp = "<strong><myfont color='#333333'size='" + sp2px(context, 18f) + "'>"
            val s12sp = "<br /><myfont color='#888888'size=''" + sp2px(context, 12f) + "'>"
            var target: String = s18sp + data + "</myfont></strong>" +
                    s14sp + Content + "</myfont><br />" + s14sp + NextLine + "</myfont>"

            if (info != null) {
                target += s12sp + info + "</myfont>"
            }

            mTitle.text = Html.fromHtml("<p>" + target + "</p>", null, HtmlTagHandler("myfont"))

        }

        fun htmlFastAnalyzePrice(context: Context, num: String, mText: TextView) {

            val s16sp = "<strong><myfont color='#f9a049'size='" + sp2px(context, 16f) + "'>"
            val s12sp = "<myfont color='#f9a049'size='" + sp2px(context, 12f) + "'>"

            var target: String = s16sp + num + "元</myfont></strong>" + s12sp + "/次</myfont>"
            mText.text = Html.fromHtml("<p>" + target + "</p>", null, HtmlTagHandler("myfont"))
        }


        fun htmlFont(num: String, mTextView: TextView) {


            val content = "<p>剩余<strong>" + num + "</strong>次</p>"
            mTextView.text = Html.fromHtml(content)

        }


        fun mDrawBackText(mTitle: TextView, data: String, Content: String) {


            val s14sp = "<p><myfont color='#ffffff'size='16px'><size>"
            val s12sp = "<myfont color='#ffffff'size='1px'><size>"

            var target: String = s14sp + data + "</size></myfont>" +
                    s12sp + Content + "</myfont></p>"


            mTitle.text = Html.fromHtml(target)

        }


        fun mDrawColorPart(mTitle: TextView, colorContent: String, Content: String) {

            var text =
                "<font color='#888888'>$Content</font><font color='#4ac995'><u>$colorContent</u></font>"

            mTitle.text = Html.fromHtml(text)

        }
    }

}