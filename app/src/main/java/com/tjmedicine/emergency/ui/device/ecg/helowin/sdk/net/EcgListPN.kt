package com.helowin.sdk.net

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.helowin.ecg.sdk.net.XHttpConnect
import com.helowin.sdk.XApp
import com.helowin.sdk.cache.SelectEcgInfo
import com.orhanobut.logger.Logger
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo
import com.tjmedicine.emergency.common.global.GlobalConstants
import com.tjmedicine.emergency.ui.device.ecg.ceshiBean

class EcgListPN(var uid: String) : Runnable, Authorization.AuthorizationBack {
    var res = ArrayList<ceshiBean.ListBean>()

    interface EcgListV {
        fun success(r: ArrayList<ceshiBean.ListBean>)
        fun fail()
        fun end()
    }

    var v: EcgListV? = null
    var auth: String? = null
    var runnable = false
    fun start(flush: Boolean) {
        System.out.println("runnable = $runnable")
        if (!runnable) {
            runnable = true
            if (flush) res.clear()
            Thread(this).start()
        }
    }

    override fun run() {
        if (TextUtils.isEmpty(auth)) {
            Authorization().also { it.back = this }.run()
        } else {
            handler(auth)

        }
    }

    override fun handler(auth: String?) {
        if (TextUtils.isEmpty(auth)) {
            runnable = false
            return
        }
        this.auth = auth


        val xh = XHttpConnect()
        xh.addHeader("Authorization", UserInfo.getToken())
        xh.setUrl(GlobalConstants.APP_USER_GETREMOTEECG)
        xh.addParams("userId", uid)
        val pageNum: String = (res.size / 10).toString()
        xh.addParams("pageNum", pageNum)
        xh.addParams("pageSize", "10")
        val hr = xh.execute()

        val json = hr.get()
        System.out.println("json = $json")
        val parser = JsonParser()
        if (TextUtils.isEmpty(json)) {
            runnable = false
            v?.fail()
            return
        }
        val element: JsonElement = parser.parse(json)

        try {
            val jsonObject = element.asJsonObject
            if (jsonObject != null) {


                val body = jsonObject.get("data").asJsonObject

                //val data = body.get("data").asJsonObject

                var r = Gson().fromJson(body, ceshiBean::class.java) as ceshiBean
                Logger.d(r)

                if (r != null) {
                    r.list?.let {
                        if (it.size < 10) {
                            v?.end()
                        }
                        res.addAll(it)
                    }
                    runnable = false
                    v?.success(res)
                    return
                }
            }
        } catch (e: Exception) {
        }
        runnable = false
        v?.fail()

    }
}