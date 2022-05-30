package com.helowin.sdk

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import com.helowin.sdk.cache.XEcgDetialBean
import com.helowin.sdk.cache.SelectEcgInfo
import com.helowin.sdk.net.EcgDetailP
import com.squareup.picasso.Picasso
import com.tjmedicine.emergency.R
import com.tjmedicine.emergency.common.base.BaseActivity
import com.tjmedicine.emergency.ui.device.ecg.ceshiBean
import kotlinx.android.synthetic.main.act_ecg_record_fail_result.*
import java.text.SimpleDateFormat
import java.util.*
class EcgDetailFailActN : BaseActivity(), EcgDetailP.EcgDetailV {
    fun back(view: View?) {
        finish()
    }
    lateinit var edp: EcgDetailP

    var time:Long = 0

    fun jump() {
        if (!TextUtils.isEmpty(ecgUrl)) {
//            EcgWebAct.start(this, "查看心电图", ecgUrl!!)
        }else{
//            XApp.showToast("心电图链接不存在")
        }
    }
    fun getbir(dr: String?): String {
        val d =
            Date(dr?.toLong()?:System.currentTimeMillis())
        // 格式化日期
        val s = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return s.format(d)
    }
    fun initFail(mEcgReportInfo: XEcgDetialBean){
        time = mEcgReportInfo.takeTime?.toLong()?:0
        include_new_analyze_result_fail_status.text = mEcgReportInfo.title
        include_new_analyze_result_fail_time.text = getbir(mEcgReportInfo.takeTime)
        include_new_analyze_result_fail_length.text = "时长:" + SelectEcgInfo.SelectEcg.getLength(mEcgReportInfo.length)
//        XApp.setImage(mEcgReportInfo.fileImagePath, include_new_analyze_result_fail_picture, 0)
        include_new_analyze_result_fail_content.text = mEcgReportInfo.ecgResultTz
        if(TextUtils.isEmpty(ecgUrl)) {
            ecgUrl = mEcgReportInfo.ecgUrl
        }
        if(!TextUtils.isEmpty(mEcgReportInfo.fileImagePath)) {
            Picasso.with(this).load(mEcgReportInfo.fileImagePath).into(include_new_analyze_result_fail_picture)
        }
    }
    var ecgUrl: String? = null
    lateinit var mSelectInfo: ceshiBean.ListBean
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setLayoutResourceID(): Int =R.layout.act_ecg_record_fail_result

    override fun initView() {
        setTitle("心电详情")
        var se = intent.getSerializableExtra("TAG") as ceshiBean.ListBean
        ecgUrl=se?.ecgUrl?:""
        edp = EcgDetailP(this,se?.uuid?:532785.toString())
        edp.start()
        include_impress_heart_question_fail_new.setOnClickListener{
            include_impress_heart_fail.measure(0,0)
            var lp= RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(include_impress_heart_fail.width, 0, 0, 0);

            PopUpWindowsTip.ShowPopUpWindowsTipMid(this, R.layout.pop_show_tip_view, include_impress_heart_question_fail_new,lp)
        }
        include_new_analyze_result_fail_picture.setOnClickListener { jump() }
        mSelectInfo = intent.getSerializableExtra("TAG") as ceshiBean.ListBean

    }

    override fun success(r: XEcgDetialBean) {
        runOnUiThread {
            initFail(r)
        }
    }

  override  fun fail() {
    }


}