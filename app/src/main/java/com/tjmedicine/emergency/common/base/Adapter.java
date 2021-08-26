package com.tjmedicine.emergency.common.base;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.tjmedicine.emergency.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/7/24 9:34
 */
 

public abstract class Adapter<T> extends RecyclerArrayAdapter<T> {

    private Map<String, Object> mPage = new HashMap<>();
    private int mCurrent = 0;
    private String mCurrentName = "pageNum";
    private String mPageSizeName = "pageSize";

    public void setPageName(String currentName, String pageSizeName) {
        if (currentName != null) mCurrentName = currentName;
        if (pageSizeName != null) mPageSizeName = pageSizeName;
    }

    public Map<String, Object> getNextPage() {
        mPage.put(mCurrentName, ++mCurrent);
        mPage.put(mPageSizeName, 10);
        return mPage;
    }

    public Map<String, Object> getFixationPage() {
        mPage.put(mCurrentName, ++mCurrent);
        mPage.put(mPageSizeName, 20);
        return mPage;
    }

    public Map<String, Object> refreshPage() {
        mCurrent = 0;
        super.clear();
        return getNextPage();
    }

    public void isShowEmpty(boolean isEmpty) {
        if (isEmpty) super.setNoMore(R.layout.recycler_nomore);
    }

    public Adapter(Context context) {
        super(context);
        isShowEmpty(true);
    }

    public Adapter(Context context, boolean isEmpty) {
        super(context);
        isShowEmpty(isEmpty);
    }

    public void update(Collection<? extends T> collection) {
        clear();
        addAll(collection);
    }

    public void update(T[] items) {
        clear();
        addAll(items);
    }

    public void updateData(Collection<? extends T> collection) {
        addAll(collection);
    }


    @Override
    public abstract BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType);


    public void setMore(OnMoreListener listener) {
        super.setMore(R.layout.recycler_progress, listener);
    }
}
