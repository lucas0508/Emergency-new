package com.helowin.sdk.net

import android.text.TextUtils
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.helowin.ecg.sdk.net.XHttpConnect
import com.helowin.sdk.XApp
import com.helowin.sdk.cache.AAUserId
import com.helowin.sdk.cache.findUid

class GetUidP(var id_: String, var v: GetUidV? = null) : Runnable ,Authorization.AuthorizationBack{
    interface GetUidV {
        fun handler(uid: String?)
    }
    var auth: String?=null
    fun start() {
        var uid = id_.findUid()
        if (uid != null) {
            v?.handler(uid)
        }else{
            Thread(this).start()
        }
    }
    override fun run() {
        if(TextUtils.isEmpty(auth)){
            Authorization().also { it.back = this }.run()
        }else {
            handler(auth)
        }
    }

    override fun handler(auth: String?) {
        this.auth = auth
        if(TextUtils.isEmpty(auth)){
            v?.handler(null)
            return
        }
        val xh = XHttpConnect()
        xh.setUrl(XApp.url)
        xh.addHeader("Authorization", auth)
        xh.functionId ="XHJD001103001"
        xh.addParams("openid",id_)
        xh.addParams("channel","lanzhou2020")
        val hr = xh.execute()

        val json = hr.get()
        System.out.println("openid = $id_ >>getUidP json = $json")
        val parser = JsonParser()
        if (TextUtils.isEmpty(json)){
            v?.handler(null)
            return
        }
        try {
            val element: JsonElement = parser.parse(json)
            val jsonObject = element.asJsonObject
            if (jsonObject != null) {
                val body = jsonObject.get("body").asJsonObject
                if(body.has("data")){
                    val data = body.get("data").asJsonObject
                    if(data.has("userId")){
                       var r = data.get("userId").asString;
                        if(r!=null){
                            AAUserId().let {
                                it.id_ = id_
                                it.uid = r
                                it
                            }.saveUpdate()
                           v?.handler(r)
                            return
                        }
                    }
                }
            }
        }catch(t:Throwable){
        }
        v?.handler(null)
    }
}