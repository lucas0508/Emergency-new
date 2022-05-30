package a

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.helowin.sdk.cache.DevBean
import com.helowin.sdk.cache.XCache
import com.tjmedicine.emergency.R
import com.tjmedicine.emergency.common.base.BaseActivity
import com.tjmedicine.emergency.ui.device.ecg.helowin.sdk.scan.ScanDev
import com.tjmedicine.emergency.ui.device.ecg.helowin.sdk.scan.ScanResultCallback
import kotlinx.android.synthetic.main.act_dev.*


class DevAct :BaseActivity(), ScanResultCallback {
    fun back(view: View?) {
        finish()
    }
    var ebs: ScanDev = ScanDev.getInstance(this)

    fun flush(){
        serial_number.setText(XCache.getSn())
    }
    fun checkBle(activity: Activity?): Boolean {
        if (!b.Utils.setLocationService(activity)) {
            return false
        }
        return b.Utils.setBle(activity)
    }


    override fun setLayoutResourceID(): Int = R.layout.act_dev

    override fun initView() {
        flush()
        sssb.setOnClickListener {
            if(checkBle(this)) {
                pbLL.visibility = View.VISIBLE
                list.clear()
                ebs.startScan()
            }
        }
    }

    var list:ArrayList<DevBean> = ArrayList()
    override fun find(t: BluetoothDevice?): Boolean {
        if(t!=null){
          list.add(DevBean().apply { this.address = t.address
          this.name = t.name})
        }else{
            pbLL.visibility = View.GONE
            if(list.isEmpty()){
                Toast.makeText(this,"没有找到设备",Toast.LENGTH_LONG).show()
            }else{
                    var a = Array(list.size){list.get(it).name}


                val alertDialog3: AlertDialog = AlertDialog.Builder(this)
                    .setTitle("选择设备")
                    .setItems(a,
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                            XCache.save(list[i])
                            flush()
                        })
                    .create()
                alertDialog3.show()
            }
        }
        return false
    }
}