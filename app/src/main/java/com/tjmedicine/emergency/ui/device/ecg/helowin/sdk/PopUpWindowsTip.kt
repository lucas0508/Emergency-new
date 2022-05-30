package com.helowin.sdk

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.tjmedicine.emergency.R

class PopUpWindowsTip {
    companion object {
        private val TAG = "PopUpWindowsTip"
        @JvmStatic
    fun ShowPopUpWindowsTipMid(mContext: Context?, layoutIds: Int, TargetView: View,lp: RelativeLayout.LayoutParams?=null) {
        val mView = LayoutInflater.from(mContext).inflate(layoutIds, null)
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)


        val measuredWidth = mView.measuredWidth
        val popupWindow = PopupWindow(mView)
        popupWindow.width = WindowManager.LayoutParams.MATCH_PARENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.isFocusable = true//popupwindow设置焦点
            val wm = mContext
                    ?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val width = wm.defaultDisplay.width

        popupWindow.isOutsideTouchable = true//点击外面窗口消失
             var mColorDrawable:ColorDrawable=ColorDrawable(mContext?.resources.getColor(android.R.color.transparent))
            popupWindow.setBackgroundDrawable(mColorDrawable)
        // popupWindow.showAsDropDown(v,0,0);
        //获取点击View的坐标
        val location = IntArray(2)
        TargetView.getLocationOnScreen(location)
        //  popupWindow.showAsDropDown(imgSignTip);//在v的下面
        //显示在上方

            if (lp!=null)
            mView.findViewById<ImageView>(R.id.triangle).layoutParams = lp
            popupWindow.showAsDropDown(TargetView)
        }
    }




}