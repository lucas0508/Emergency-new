//package com.helowin.sdk.widget
//
//import android.content.Context
//import android.graphics.Color
//import android.text.TextUtils
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.TextView
//import com.helowin.sdk.cache.SelectEcgInfo
//import com.squareup.picasso.Picasso
//import com.tjmedicine.emergency.R
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.ArrayList
//
//class EcgAdapterHistory(val mC: Context):BaseAdapter(){
//    val data = ArrayList<SelectEcgInfoHistory.list>(12)
//    val inflater = LayoutInflater.from(mC)
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val cV:View;
//        val viewHolder:ViewHolder
//        if(convertView==null){
//
//            cV = inflater.inflate(R.layout.item_v237_ecg,parent,false)
//            val ecg_title = cV.findViewById<TextView>(R.id.ecg_title)
//            val ecg_take_time = cV.findViewById<TextView>(R.id.ecg_take_time)
//            val ecg_hr = cV.findViewById<TextView>(R.id.ecg_hr)
//            val ecg_lenth = cV.findViewById<TextView>(R.id.ecg_lenth)
//            val ecg_logo = cV.findViewById<XImageView>(R.id.ecg_logo)
//            viewHolder=ViewHolder(ecg_title,ecg_take_time,ecg_hr,ecg_lenth,ecg_logo)
//            cV.tag = viewHolder
//        }else{
//            viewHolder = convertView.tag as ViewHolder
//            cV= convertView
//        }
//        val  item = data[position]
//
//        var ecg_state: String = item.title
//        if (TextUtils.isEmpty(ecg_state)) {
//            item.title = "干扰过多，请重新测量"
//            ecg_state = item.title
//        }
//        if (ecg_state == "疑似异常") {
//            viewHolder.ecg_title.setTextColor(mC.getResources().getColor(android.R.color.holo_red_light))
//        } else if (ecg_state.contains("分析中")) {
//            viewHolder.ecg_title.setTextColor(Color.parseColor("#999999"))
//        } else {
//            viewHolder.ecg_title.setTextColor(Color.parseColor("#333333"))
//        }
//        viewHolder.ecg_title.setText(ecg_state)
//        val hr: Int = item.heartRate.toInt()
//        if (hr <= 0) {
//            viewHolder.ecg_hr.setText("-- bpm")
//        } else {
//            viewHolder.ecg_hr.setText(item.heartRate.toString() + " bpm")
//        }
//        viewHolder.ecg_take_time.setText(
//           getbir(item.takeTime)
//        )
////        viewHolder.ecg_logo.visibility = View.GONE
//        viewHolder.ecg_logo.setImageResource(0)
//
//
//        if (!TextUtils.isEmpty(item.fileImagePath))
//        Picasso.with(mC).load(item.fileImagePath).into(  viewHolder.ecg_logo)
//        if (ecg_state.contains("分析中")) {
//            viewHolder.ecg_lenth.setText("--")
//        } else {
//            viewHolder.ecg_lenth.setText(SelectEcgInfo.SelectEcg.getLength(item.length))
//        }
//        return cV
//    }
//    fun getbir(dr: String): String {
//        val d =
//            Date(dr.toLong())
//        // 格式化日期
//        val s = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        return s.format(d)
//    }
//    override fun getItem(position: Int): Any {
//        return data[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//    fun init(d:ArrayList<SelectEcgInfoHistory.list>){
//        data.clear()
//        data.addAll(d)
//        notifyDataSetChanged()
//    }
//    override fun getCount(): Int {
//        return data.size
//    }
//    class ViewHolder(val ecg_title:TextView,val ecg_take_time:TextView,val ecg_hr:TextView,val ecg_lenth:TextView,val ecg_logo:XImageView)
//
//}