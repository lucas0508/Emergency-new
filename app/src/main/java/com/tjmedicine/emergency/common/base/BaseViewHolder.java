package com.tjmedicine.emergency.common.base;

import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/16 11:20
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 获取 ItemView
     *
     * @return ItemView
     */
    public View getItemView() {
        return mItemView;
    }

    /**
     * 获取 itemView 中的子view
     *
     * @param viewId id
     * @param <T> 类别
     * @return view对象
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

}
