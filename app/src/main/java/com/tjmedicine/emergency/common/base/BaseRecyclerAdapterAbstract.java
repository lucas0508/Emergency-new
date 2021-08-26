package com.tjmedicine.emergency.common.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/16 11:25
 */

abstract class BaseRecyclerAdapterAbstract extends RecyclerView.Adapter<BaseViewHolder> {

    Context mContext;

    /**
     * 用于传参
     */
    private Map<String, Object> mPage;

    /**
     * 当前页数
     */
    private int mCurrentPage = 1;

    /**
     * 每页条数
     */
    private int mPageSize = 10;

    /**
     * 当前页名称
     */
    private String mCurrentPageName = "currentPage";
    /**
     * 每页条数名称
     */
    private String mPageSizeName = "pageSize";

    public String getCurrentPageName() {
        return mCurrentPageName;
    }

    public void setCurrentPageName(String currentPageName) {
        mCurrentPageName = currentPageName;
    }

    public String getPageSizeName() {
        return mPageSizeName;
    }

    public void setPageSizeName(String pageSizeName) {
        mPageSizeName = pageSizeName;
    }

    /**
     * 滚动监听
     */
    private RecyclerView.OnScrollListener mOnScrollListener;

    /**
     * 是否加载中
     */
    private boolean isLoading = false;

    public BaseRecyclerAdapterAbstract(Context context) {
        this.mContext = context;
        if (mPage == null) mPage = new HashMap<>();
    }

    /**
     * 设置当前页
     *
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    /**
     * 获取每页条数
     *
     * @return
     */
    public int getPageSize() {
        return mPageSize;
    }

    /**
     * 获取分页对象
     *
     * @return
     */
    public Map<String, Object> getPage() {
        mPage.put(mCurrentPageName, mCurrentPage);
        mPage.put(mPageSizeName, mPageSize);
        return mPage;
    }

    /**
     * 获取下一页
     *
     * @return
     */
    public Map<String, Object> getNextPage() {
        mCurrentPage++;
        return getPage();
    }

    /**
     * 设置记载状态
     *
     * @param loading
     */
    protected void setLoading(boolean loading) {
        isLoading = loading;
    }

    /**
     * 滚动事件
     *
     * @param loadData
     * @return
     */
    public RecyclerView.OnScrollListener getOnScrollListener(final LoadData loadData) {
        if (loadData == null) return null;
        if (mOnScrollListener == null) mOnScrollListener = new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isLoading && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == getItemCount()) {
                    setLoading(true);
                    loadData.callback();
                }
            }
        };
        return mOnScrollListener;
    }

    /**
     * 获取 根 view
     *
     * @param layout
     * @param parent
     * @param attachToRoot
     * @return
     */
    public View getView(int layout, ViewGroup parent, boolean attachToRoot) {
        return LayoutInflater.from(mContext).inflate(layout, parent, attachToRoot);
    }

    /**
     * 滚动至底部回调
     */
    public interface LoadData {
        void callback();
    }

}
