package com.tjmedicine.emergency.common.base;

import android.view.ViewGroup;

/**
 * @author QiZai
 * @desc
 * @date 2018/7/24 9:25
 */

public abstract class ViewHolder<T> extends com.jude.easyrecyclerview.adapter.BaseViewHolder<T> {

    public ViewHolder(ViewGroup parent, int res) {
        super(parent, res);
        initView();
    }

	
    public abstract void initView();

}
