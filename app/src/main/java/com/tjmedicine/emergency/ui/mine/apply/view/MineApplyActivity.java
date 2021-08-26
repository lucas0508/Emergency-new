package com.tjmedicine.emergency.ui.mine.apply.view;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.Adapter;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.ViewHolder;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.ui.bean.SignUpBean;
import com.tjmedicine.emergency.ui.bean.TeachData;
import com.tjmedicine.emergency.ui.mine.apply.presenter.MineApplyPresenter;
import com.tjmedicine.emergency.ui.other.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MineApplyActivity extends BaseActivity implements IMineApplyView {

    private MineApplyPresenter mineApplyPresenter = new MineApplyPresenter(this);

    @BindView(R.id.recycler_view)
    EasyRecyclerView mEasyRecyclerView;
    @BindView(R.id.tv_title)
    TextView mTitle;
    private Adapter<SignUpBean> mAdapter;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_mine_apply;
    }

    @Override
    protected void initView() {
        mTitle.setText("我的报名");
        initRecyclerView();
        mApp.getLoadingDialog().show();
        mineApplyPresenter.findSignUp();
    }

    private void initRecyclerView() {
        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(myLinearLayoutManager);

        mEasyRecyclerView.setAdapterWithProgress(mAdapter = new Adapter<SignUpBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

                return new ViewHolder<SignUpBean>(parent, R.layout.fragment_teach_recycler_item) {

                    private TextView tv_home_title,
                            tv_home_content;
                    private ImageFilterView riv_image;

                    @Override
                    public void initView() {
                        tv_home_title = $(R.id.tv_title);
                        tv_home_content = $(R.id.tv_content);
                        riv_image = $(R.id.show_img);
                    }

                    @Override
                    public void setData(SignUpBean data) {
                        super.setData(data);
                        tv_home_title.setText(data.getTitle());
                        tv_home_content.setText(data.getContent());
                        Glide.with(EmergencyApplication.getContext())
                                .load(data.getImgUrl())
                                .into(riv_image);
                    }
                };
            }
        });
        mEasyRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mineApplyPresenter.findSignUp();
            }
        });

        mEasyRecyclerView.setEmptyView(R.layout.recycler_sign_up_empty);
//        mAdapter.setMore(new RecyclerArrayAdapter.OnMoreListener() {
//            @Override
//            public void onMoreShow() {
//                //   recruitmentPresenter.loadRecruitmentData(mAdapter.getNextPage(), mCityCode, key,"");
//            }
//
//
//            @Override
//            public void onMoreClick() {
//            }
//        });


        mAdapter.setOnItemClickListener(position -> {
            Bundle intent = new Bundle();
            intent.putString(Constants.WEB_KEY_URL, mAdapter.getItem(position).getHttpUrl());
            intent.putInt(Constants.WEB_KEY_FLAG, 1);
            startActivity(WebActivity.class, intent);
        });

    }

    @Override
    public void findSignUpSuccess(List<SignUpBean> signUpBeans) {
        mApp.getLoadingDialog().hide();
        mAdapter.update(signUpBeans);
    }

    @Override
    public void findSignUpFail(String info) {
        mApp.getLoadingDialog().hide();
    }

    @Override
    public void signUpSuccess() {

    }

    @Override
    public void signUpFail(String info) {

    }
}
