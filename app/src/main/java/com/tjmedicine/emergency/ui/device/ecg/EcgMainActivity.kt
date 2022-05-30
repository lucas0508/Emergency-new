package com.tjmedicine.emergency.ui.device.ecg

import a.DevAct
import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.helowin.ecg.sdk.util.EcgSdk
import com.helowin.sdk.EcgListAct
import com.helowin.sdk.cache.XCache

import com.tjmedicine.emergency.R
import com.tjmedicine.emergency.common.base.BaseActivity

class EcgMainActivity : BaseActivity() {

    lateinit var openid:String
    lateinit var uid:String

    var takeTime = System.currentTimeMillis()

    override fun setLayoutResourceID(): Int =R.layout.ecgactivity_main

    override fun initView() {
        openid = intent.getStringExtra("openid")
        uid = intent.getStringExtra("uid")
        EcgSdk.instance.userId = uid
        EcgSdk.instance.init(this)
        var ps = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH
        )
        ActivityCompat.requestPermissions(this, ps,
            100);
    }

    fun onClick(v: View){
        if(TextUtils.isEmpty(XCache.getMac())){
            startActivity(Intent(this, DevAct::class.java))
            return
        }
        EcgSdk.instance.initDev(XCache.getMac()!!,XCache.getSn()!!)
        when(v.id){
            R.id.h-> startActivity(Intent(this,EcgActivity::class.java))
            R.id.h24-> {
                if(XCache.getSn()!!.startsWith("T1")) {
                    var i = Intent(this, Ecg24Activity::class.java)
                    startActivity(i)
                }else{
                    Toast.makeText(this,"该设备不支持动态心电",Toast.LENGTH_LONG).show()
                }
            }
            R.id.h24_a->{// bind device
                startActivity(Intent(this,DevAct::class.java))
            }
            R.id.h24_b->{
                var i =Intent(this, EcgListAct::class.java)
                i.putExtra("ID",openid)
                startActivity(i)
            }
        }
    }
}




