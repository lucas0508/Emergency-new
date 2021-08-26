package com.tjmedicine.emergency.common.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.model.widget.SelectPicDialog;


/**
 * @author QiZai
 * @desc
 * @date 2018/6/6 10:04
 */

public class DialogManage {

    private Context mContext;
    private OptionDialog mOptionDialog;
    private LoadingDialog mLoadingDialog;
    private SelectPicDialog mSelectPicDialog;
    private ConfirmAgreementDialog confirmAgreementDialog;

    public DialogManage(Context context) {
        mContext = context;
    }



    /**
     * 判断用户是否登录，未登录弹出提醒
     *
     * @return 是否登录
     */
    public Boolean isLoginToDialog() {
        boolean flag = false;
        if (isLogin())
            flag = isLogin();
        if (!flag) {
//            getLoginDialog().show();
        }
        return flag;
    }


    /**
     * 判断用户是否登录 - 无弹窗
     *
     * @return
     */
    public boolean isLogin() {
        return !TextUtils.isEmpty(UserInfo.getToken());
    }




    /**
     * 获取loading弹窗
     *
     * @return
     */
    public LoadingDialog getLoadingDialog() {
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(mContext);
        return mLoadingDialog;
    }

    /**
     * 获取选项弹窗
     *
     * @return
     */
    public OptionDialog getOptionDialog() {
        if (mOptionDialog == null)
            mOptionDialog = new OptionDialog(mContext);
        return mOptionDialog;
    }


    public SelectPicDialog getSelectPicDialog() {
        if (mSelectPicDialog == null)
            mSelectPicDialog = new SelectPicDialog(mContext);
        return mSelectPicDialog;
    }

    /**
     * 获取普通弹窗
     *
     * @return
     */
    public ConfirmAgreementDialog getConfirmAgreementDialog() {
        if (confirmAgreementDialog == null)
            confirmAgreementDialog = new ConfirmAgreementDialog(mContext);
        return confirmAgreementDialog;
    }



    /**
     * short - 吐司信息
     *
     * @param info
     */
    public void shortToast(String info) {
        if (info != null) Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * long - 吐司信息
     *
     * @param info
     */
    public void longToast(String info) {
        if (info != null) Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
    }
}
