package com.helowin.sdk.net

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.helowin.ecg.sdk.net.XHttpConnect
import com.helowin.sdk.XApp
import com.helowin.sdk.cache.SelectEcgInfo

class EcgListP(var uid: String) : Runnable ,Authorization.AuthorizationBack{
    var res =ArrayList<SelectEcgInfo.SelectEcg>()
    interface EcgListV{
        fun success(r: ArrayList<SelectEcgInfo.SelectEcg>)
        fun fail()
        fun end()
    }
    var v:EcgListV?=null
    var auth: String?=null
    var runnable = false
    fun start(flush: Boolean){
        System.out.println("runnable = $runnable")
        if(!runnable) {
            runnable = true
            if(flush)res.clear()
            Thread(this).start()
        }
    }
    override fun run() {
        if(TextUtils.isEmpty(auth)){
            Authorization().also { it.back = this }.run()
        }else{
            handler(auth)

        }
    }

    override fun handler(auth: String?) {
        if(TextUtils.isEmpty(auth)){
            runnable = false
            return
        }
        this.auth = auth


        val xh = XHttpConnect()
        xh.addHeader("Authorization", auth)
        xh.functionId = "XHJD001105002"
        xh.setUrl(XApp.url)
        xh.addParams("userId",uid)
        val pageNum: String = (res.size / 10).toString()
        xh.addParams("pageNum",pageNum)
        xh.addParams("pageSize","10")
        val hr = xh.execute()

        val json = hr.get()
        System.out.println("json = $json")
        val parser = JsonParser()
        if (TextUtils.isEmpty(json)){
            runnable = false
            v?.fail()
            return
        }
        val element: JsonElement = parser.parse(json)

        try{
            val jsonObject = element.asJsonObject
            if (jsonObject != null) {
                val body = jsonObject.get("body").asJsonObject
                if(body.has("data")){
                    val data = body.get("data").asJsonObject


                  var  r = Gson().fromJson(data, SelectEcgInfo::class.java) as SelectEcgInfo
                    if(r!=null){
                        r.datas?.let {
                            if(it.size<10){
                                v?.end()
                            }
                            res.addAll(it)
                        }
                        runnable = false
                        v?.success(res)
                        return
                    }
                }
            } }catch (e:Exception){
        }
        runnable = false
      v?.fail()

    }
}