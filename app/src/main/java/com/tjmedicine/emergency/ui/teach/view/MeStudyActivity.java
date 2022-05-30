package com.tjmedicine.emergency.ui.teach.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.Adapter;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.ViewHolder;
import com.tjmedicine.emergency.common.bean.StudyBean;
import com.tjmedicine.emergency.ui.teach.presenter.MeStudyPresenter;

import java.util.List;

import butterknife.BindView;

public class MeStudyActivity extends BaseActivity implements IMeStudyView {

    private MeStudyPresenter meStudyPresenter = new MeStudyPresenter(this);


    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.recycler_view)
    EasyRecyclerView mEasyRecyclerView;
    @BindView(R.id.tv_progress)
    TextView tv_progress;

    private Adapter<StudyBean.ListBean> mAdapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_mestudy;
    }

    @Override
    protected void initView() {
        mTitle.setText("我要学习");


        initRecyclerView();
//        meStudyPresenter.queryMeStudyList(mAdapter.refreshPage());

    }

    @Override
    protected void onResume() {
        super.onResume();
        meStudyPresenter.queryMeStudyList(mAdapter.refreshPage());
    }

    private void initRecyclerView() {

        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(myLinearLayoutManager);

        mEasyRecyclerView.setAdapterWithProgress(mAdapter = new Adapter<StudyBean.ListBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

                return new ViewHolder<StudyBean.ListBean>(parent, R.layout.activity_mestudy_item) {

                    private TextView tv_title;


                    @Override
                    public void initView() {
                        tv_title = $(R.id.tv_title);

                    }

                    @Override
                    public void setData(StudyBean.ListBean data) {
                        super.setData(data);
                        tv_title.setText(data.getTitle());
                        if (data.getCompleteStudy()==1){
                            tv_title.setEnabled(false);
                        }

                    }
                };
            }
        });
        mEasyRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                meStudyPresenter.queryMeStudyList(mAdapter.refreshPage());
            }
        });
        mAdapter.setMore(new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                meStudyPresenter.queryMeStudyList(mAdapter.getNextPage());
            }

            @Override
            public void onMoreClick() {
            }
        });


        mAdapter.setOnItemClickListener(position -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("studyBean", mAdapter.getItem(position));
            startActivity(MeStudyDetailActivity.class, bundle);
        });
    }

    @Override
    public void queryMeStudySuccess(List<StudyBean.ListBean> studyBeans) {
        Logger.d(studyBeans);

        if (null!=studyBeans && studyBeans.size() > 0) {
            tv_progress.setText("学习进度" + studyBeans.get(0).getStudyCourseProgress() + "%");
            mProgressBar.setProgress(studyBeans.get(0).getStudyCourseProgress());
        }
        mAdapter.addAll(studyBeans);
    }

    @Override
    public void queryBannerFail(String info) {
        Logger.d(info);
    }
}
