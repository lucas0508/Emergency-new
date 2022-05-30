package com.helowin.sdk.net

import android.content.ContentValues
import android.text.TextUtils
import android.util.Log
import com.helowin.ecg.sdk.bean.EcgBean
import com.helowin.ecg.sdk.net.XHttpConnect
import com.helowin.sdk.XApp
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException

class UpdateEcgP(val eb: EcgBean, val v:UpdateEcgV,val isDongTai:Boolean) :Runnable, Authorization.AuthorizationBack,
    UpdateFileP.UpdateFileV {

    interface UpdateEcgV{
        fun fail()
        fun success(rid:String)
    }
    fun start(){
        Thread(this).start()
    }
    var auth:String?=null

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
            v?.fail()
            return
        }
        UpdateFileP(eb.filePath,this).run()
    }

    override fun success(path: String?, id: String?) {
        if(path==null){
            v.fail()
        }else{
            var    rid:String? = null
            val xHttpConnect = XHttpConnect()
            xHttpConnect.setUrl(XApp.url)
            xHttpConnect.addHeader("Authorization", auth);
            xHttpConnect.apply {
                addParams("reportType","2")
                addParams("deviceType","3")
                addParams("deviceSn",eb.devId)
                addParams("takeTime",eb.takeTime)
                addParams("mos","2")
                addParams("heartRates", eb.rates)
                addParams("userId",eb.userNo)
                addParams("channel",XApp.channel)
                if(!isDongTai){
                    addParams("fileId", id!!)
                    addParams("filePath", path!!)
                    addParams("length",eb.length)
                }else {
                    addParams("fileEcgId", id!!)
                    addParams("fileEcgPath", path!!)
                    addParams("fileEcgLen",eb.length)
                }
            }
        if (isDongTai){
            xHttpConnect.functionId = "XHJD001107001"
        }else{
            xHttpConnect.functionId = "XHJD001104001"
        }
            try {
                val hr = xHttpConnect.execute()
                val json = hr.get()
                Log.d(ContentValues.TAG, "updateData json = $json")
                val jsonObject = JSONObject(json)
                if (jsonObject != null) {
                    val body = jsonObject.getJSONObject("body")
                    if (body.has("data")) {
                        val job = body.getJSONObject("data")
                        if (job.has("reportNo")) {
                            rid = job.getString("reportNo")
                        }
                    }
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Throwable) {
            } finally {
                if (rid != null) {
                    v.success(rid)
                } else {
                    v.fail()
                }
            }
        }
    }
}