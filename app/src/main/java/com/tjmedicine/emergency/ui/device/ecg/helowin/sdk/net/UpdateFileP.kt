package com.helowin.sdk.net

import android.content.ContentValues
import android.text.TextUtils
import android.util.Log
import com.helowin.ecg.sdk.net.XHttpConnect
import com.helowin.sdk.XApp
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.MalformedURLException

class UpdateFileP(private val filePath: String?, val v:UpdateFileV) :Runnable{
    interface UpdateFileV{
        fun success(path: String?=null,id:String?=null)
    }
    fun start(){
        Thread(this).start()
    }
    override fun run() {
        if(TextUtils.isEmpty(filePath)){
            v.success()
            return
        }
        val xHttpConnect = XHttpConnect()
        xHttpConnect.functionId="XHJD001104001"
        xHttpConnect.setUrl(XApp.update_file_url)
        var  file = File(filePath)
        xHttpConnect.addFile("file", file)
       var path:String? = null
       var id:String? = null
        try {
            val hr = xHttpConnect.execute()
            val json = hr.get()
            val jsonObject = JSONObject(json)
            if (jsonObject != null) {
                val body = jsonObject.getJSONObject("body")
                if (body.has("obj")) {
                    val job = body.getJSONObject("obj")
                    if (job.has("filePath")) {

                        path = job.getString("filePath")
                        //                         path = path.substring(1,path.length()-1);
                    }
                    if (job.has("id")) {
                        id = job["id"].toString()
                    }
                    Log.d(ContentValues.TAG, "path = $path ,id = $id")
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Throwable) {
        } finally {
            v.success(path, id)
        }
    }
}