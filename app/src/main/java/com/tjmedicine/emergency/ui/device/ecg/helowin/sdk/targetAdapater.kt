package com.helowin.sdk

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.helowin.sdk.cache.TargetBean
import com.tjmedicine.emergency.R

import kotlinx.android.synthetic.main.hkwd_text_watch_target_list_item.view.*

class TargetAdapater(var items : List<TargetBean>) : RecyclerView.Adapter<TargetAdapater.ViewHolder>() {


    lateinit var mContext:Context

    override fun getItemCount(): Int =items.size

    fun setDate( items : List<TargetBean>){
        this.items=items

        notifyDataSetChanged()

    }


    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView){


        fun setDrawableLeft(attention: TextView, drawableId: Int,mContext:Context) {
            if(drawableId==0){
                attention.setCompoundDrawables(null, null, null, null)
            }else {
                val drawable = mContext.resources.getDrawable(drawableId)
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                attention.setCompoundDrawables(null, null, drawable, null)
            }
        }



        fun bindDate(mWatchTargetListBean: TargetBean, mContext:Context){

              with(mWatchTargetListBean){
                  mView.item_watch_target_list_measure_name.text=this.name
                  mView.item_watch_target_list_measure_edit_value.setTextColor(Color.parseColor("#333333"))
                  setDrawableLeft( mView.item_watch_target_list_measure_edit_value,0,mContext)
                  if (this.state==1){
                      mView.item_watch_target_list_measure_edit_value.setTextColor(Color.parseColor("#e95f5f"))
                      setDrawableLeft( mView.item_watch_target_list_measure_edit_value,
                              R.mipmap.icon_dataup,mContext)
                  }else if (this.state==2){
                      mView.item_watch_target_list_measure_edit_value.setTextColor(Color.parseColor("#e95f5f"))
                      setDrawableLeft( mView.item_watch_target_list_measure_edit_value, R.mipmap.icon_datadown,mContext)
                  }else if (this.state==3){
                      mView.item_watch_target_list_measure_edit_value.setTextColor(Color.parseColor("#e95f5f"))
                  }
                  if(this.value==null){
                      mView.item_watch_target_list_measure_edit_value.text = this.ecg
                  }else {
                      mView.item_watch_target_list_measure_edit_value.text = this.value
                  }

                  mView.item_watch_target_list_measure_target.text=this.range
              }


            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.mContext= parent?.context!!
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.hkwd_text_watch_target_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindDate(items[position],mContext)
        if (position % 2 == 0) {
            holder?.mView?.setBackgroundColor(Color.parseColor("#f7f7f7"))
        } else {
            holder?.mView?.setBackgroundResource(android.R.color.white)
        }
    }


}