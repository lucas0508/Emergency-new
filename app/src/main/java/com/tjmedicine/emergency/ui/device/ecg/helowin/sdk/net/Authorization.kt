package com.helowin.sdk.net

import com.helowin.ecg.sdk.net.XHttpConnect
import com.helowin.sdk.XApp
import org.json.JSONObject

class Authorization:Runnable{

    interface  AuthorizationBack{
        fun handler(auth:String?)
    }
    var  back:AuthorizationBack? = null
    override fun run() {
        var token:String? = null
        val xHttpConnect = XHttpConnect()
        xHttpConnect.setUrl(XApp.oauth_url)
        try {
            val hr = xHttpConnect.execute()
            val json = hr.get()
            val jsonObject = JSONObject(json)
            if (jsonObject.has("access_token")) {
                val v1 = jsonObject.getString("access_token")
                val v2 = jsonObject.getString("token_type")
                token = "$v2$v1"
                System.out.println("token = $token >>getUidP json = $json")
            }
        } catch (e: Throwable) {

        } finally {
            back?.handler(token)
        }
    }
    fun start(){
        Thread(this).start()
    }
}