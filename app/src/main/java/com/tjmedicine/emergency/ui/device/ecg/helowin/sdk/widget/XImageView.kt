package com.helowin.sdk.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView


@SuppressLint("AppCompatCustomView")
class XImageView:
    ImageView {
    var ratio = 440f / 750
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (ratio != 0f) {
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            setMeasuredDimension(widthSize, (widthSize * ratio).toInt())
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        if (contentDescription != null) {
            val des = contentDescription.toString()
            val d = des.split("-".toRegex()).toTypedArray()
            val w = d[0].toInt()
            val h = d[1].toInt()
            ratio = 1.0f * h / w
        }
    }
}