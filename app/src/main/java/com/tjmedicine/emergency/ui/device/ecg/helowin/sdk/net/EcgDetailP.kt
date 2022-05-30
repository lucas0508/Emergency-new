package com.helowin.sdk.net

import android.text.TextUtils
import android.util.Log
import com.helowin.sdk.cache.XEcgDetialBean
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.helowin.ecg.sdk.net.XHttpConnect
import com.helowin.sdk.XApp
import com.orhanobut.logger.Logger

class EcgDetailP(val v: EcgDetailV,val id:String)
    :Runnable,Authorization.AuthorizationBack{
    var auth:String?=null

    interface  EcgDetailV{
        fun success(r: XEcgDetialBean)
        fun fail()
    }
    fun start(){
        Thread(this).start()
    }
    override fun run() {
        if(TextUtils.isEmpty(auth)){
            Authorization().also { it.back = this }.run()
        }else{
            handler(auth)
        }
    }

    override fun handler(auth: String?) {
        val xh = XHttpConnect()
        xh.addHeader("Authorization", auth);
        xh.setUrl(XApp.url)
        xh.functionId ="XHJD001105003"
        xh.addParams("reportNo",id)
        val hr = xh.execute()
        val json = hr.get()
        Logger.d(json)
        Log.e("TTTTTT",json)
        val parser = JsonParser()
        if (TextUtils.isEmpty(json)){
            v?.fail()
            return
        }
        val element: JsonElement = parser.parse(json)


        val jsonObject = element.asJsonObject//JSONObject(json)
        if (jsonObject != null) {
            val body = jsonObject.get("body").asJsonObject
            if(body.has("data")){
                val data = body.get("data").asJsonObject
               var r = Gson().fromJson(data, XEcgDetialBean::class.java) as XEcgDetialBean
                if(r!=null){
                   v?.success(r)
                    return
                }

            }
        }
        v?.fail()
    }
}