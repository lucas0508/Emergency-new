package com.helowin.sdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.google.gson.Gson
import com.handmark.pulltorefresh.library.PullToRefreshBase
import com.helowin.sdk.cache.SelectEcgInfo
import com.helowin.sdk.net.EcgListP
import com.helowin.sdk.net.EcgListPN
import com.helowin.sdk.net.GetUidP
import com.helowin.sdk.widget.EcgAdapter
import com.helowin.sdk.widget.EcgAdapterN
import com.orhanobut.logger.Logger
import com.tjmedicine.emergency.R
import com.tjmedicine.emergency.common.base.BaseActivity
import com.tjmedicine.emergency.ui.device.ecg.ceshiBean

class EcgListAct :BaseActivity(),PullToRefreshBase.OnRefreshListener2<android.widget.ListView>,
    AdapterView.OnItemClickListener, GetUidP.GetUidV, EcgListPN.EcgListV {
    fun back(view: View?) {
        finish()
    }
    private lateinit var list:com.handmark.pulltorefresh.library.PullToRefreshListView
    lateinit var ecgAdapter: EcgAdapterN
    lateinit var uidP: GetUidP
    var ecgListP:EcgListPN?=null
    var id:String?=null


    override fun setLayoutResourceID(): Int=R.layout.act_ecg_list

    override fun initView() {
        id= intent.getStringExtra("ID")
        uidP= GetUidP(id!!)
        uidP.v = this
        uidP.start()
        list = findViewById(R.id.list)
        ecgAdapter = EcgAdapterN(this)
        list.setAdapter(ecgAdapter)
        list.setOnItemClickListener(this)
        list.setOnRefreshListener(this)
        list.mode = PullToRefreshBase.Mode.BOTH
    }


    override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>) {
        list.setMode(PullToRefreshBase.Mode.BOTH)
        ecgListP?.start(true)
    }

    override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
        ecgListP?.start(false)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var se=  ecgAdapter.data[id.toInt()]
        var intent = Intent()
        if (!TextUtils.isEmpty(se.state) && se.state.equals("0")) {
            intent.setClass(this, EcgDetailActN::class.java)
        } else {
            intent.setClass(this, EcgDetailFailActN::class.java)
        }
        intent.putExtra("TAG",se)
        Logger.d(""+Gson().toJson(se))
        startActivity(intent)
    }

    override fun handler(uid: String?) {
        if(uid!=null){
            ecgListP = EcgListPN(uid)
            ecgListP?.v = this
            ecgListP?.start(true)
        }
    }


    override fun success(r: ArrayList<ceshiBean.ListBean>) {
        runOnUiThread {
            ecgAdapter.data.clear()
            ecgAdapter.data.addAll(r)
            ecgAdapter.notifyDataSetChanged()
            list.onRefreshComplete()
        }
    }

    override fun fail() {
    }

    override fun end() {
        runOnUiThread {
            list.onRefreshComplete()
        }
    }
}

private fun Intent.putExtra(s: String, se: ceshiBean.ListBean) {

}
