package com.tjmedicine.emergency.common.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.facebook.stetho.common.LogUtil;
import com.tjmedicine.emergency.common.dialog.DialogManage;
import com.tjmedicine.emergency.common.global.Constants;

import java.util.List;
import java.util.Objects;
import butterknife.ButterKnife;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/10 10:51
 */

public abstract class BaseFragment extends Fragment {

    /**
     * Activity,Fragment 公共方法处理类
     */
    public DialogManage mApp;
    public View mContentView;
    /**
     * View有没有加载过
     */
    protected boolean isViewInitiated;
    /**
     * 页面是否可见
     */
    protected boolean isVisibleToUser;
    /**
     * 是不是加载过
     */
    protected boolean isDataInitiated;

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    private BaseActivity.PermissionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(), container, false);
        mApp = ((BaseActivity) requireActivity()).mApp;
        ButterKnife.bind(this, mContentView);
//        initView(savedInstanceState);
        initView();
        return mContentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewInitiated = true;
        loadData();
    }


    /**
     * 初始化view
     */
    protected abstract void initView();


//    /**
//     * 初始化view
//     */
//    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        loadData();
    }
    /**
     * [页面跳转]
     * * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getActivity(), clz));
    }

    /**
     * [携带数据的页面跳转]
     * * @param clz * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    /**
     * 懒加载
     */
    private void loadData() {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated)) {
            isDataInitiated = true;
            lazyLoad();
        }
    }

    /**
     * 6. 懒加载，Fragment可见的时候调用这个方法，而且只调用一次
     */
    protected void lazyLoad() {

    }
    /**
     * 授权运行
     *
     * @param permissionList 需要权限
     * @param listener       权限监听
     */
    public void requestRunPermission(List<String> permissionList, BaseActivity.PermissionListener listener) {
        mListener = listener;
        LogUtil.d("权限：", permissionList.toArray());
        LogUtil.d("权限数量：", permissionList.size());
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissionList.toArray(new String[permissionList.size()]), Constants.All_PERMISSIONS_CODE);
        } else {
            //表示全都授权了
            mListener.onGranted();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //RefWatcher refWatcher = MyApplication.getRefWatcher(MyApplication.getContext());
        //refWatcher.watch(this);
    }
}
