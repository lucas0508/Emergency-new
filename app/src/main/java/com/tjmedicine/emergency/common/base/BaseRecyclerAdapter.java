package com.tjmedicine.emergency.common.base;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/16 11:25
 */

public abstract class BaseRecyclerAdapter<T> extends BaseRecyclerAdapterAbstract {

    private List<T> mDataList;

    public BaseRecyclerAdapter(Context context, List<T> dataList) {
        super(context);
        mDataList = dataList == null ? new ArrayList<T>() : dataList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 重置list数据
     *
     * @param dataList
     */
    public void refresh(List<T> dataList) {
        if (dataList != null) {
            setLoading(dataList.size() < getPageSize());
            mDataList = dataList;
            notifyDataSetChanged();
        }
    }

    /**
     * 追加list数据
     *
     * @param dataList
     */
    public void addList(List<T> dataList) {
        if (dataList != null) {
            setLoading(dataList.size() < getPageSize());
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    /**
     * 指定位置添加数据
     *
     * @param position
     * @param data
     */
    public void addData(int position, T data) {
        if (data != null) {
            mDataList.add(position, data);
            notifyItemInserted(position);
        }
    }

    /**
     * 追加一条数据
     *
     * @param data
     */
    public void addData(T data) {
        if (data != null) {
            mDataList.add(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 指定位置添加数据
     *
     * @param position
     */
    public T getData(int position) {
        return mDataList.get(position);
    }

    /**
     * 指定位置添加数据
     */
    public int getDataSize() {
        return mDataList.size();
    }

    /**
     * 删除
     *
     * @param elem
     */
    public void remove(T elem) {
        if (elem != null) {
            this.mDataList.remove(elem);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 删除
     *
     * @param index
     */
    public void remove(int index) {
        this.mDataList.remove(index);
        this.notifyDataSetChanged();
    }

    /**
     * 点击事件回掉接口
     *
     * @param <T>
     */
    public interface OnItemClickListener<T> {
        void onItemClick(View view, T data);
    }
}
