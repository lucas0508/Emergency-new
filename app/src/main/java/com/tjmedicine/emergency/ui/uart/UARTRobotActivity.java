package com.tjmedicine.emergency.ui.uart;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.Adapter;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.ViewHolder;
import com.tjmedicine.emergency.common.bean.UARTBean;
import com.tjmedicine.emergency.model.widget.BGAProgressBar;
import com.tjmedicine.emergency.ui.uart.data.presenter.IUARTRobotControlView;
import com.tjmedicine.emergency.ui.uart.data.presenter.UARTRobotControlPresenter;

import java.util.List;

import butterknife.BindView;


public class UARTRobotActivity extends BaseActivity implements IUARTRobotControlView {


    EasyRecyclerView mEasyRecyclerView;

    private Adapter<UARTBean.ListBean> mAdapter;

    private UARTRobotControlPresenter uartRobotControlPresenter = new UARTRobotControlPresenter(this);

        @BindView(R.id.tv_title)
        TextView tv_title;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_robot_uart;
    }

    @Override
    protected void initView() {
        tv_title.setText("历史数据");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mEasyRecyclerView = findViewById(R.id.recycler_view);

        initRecyclerView();
        uartRobotControlPresenter.queryUARTData(null,"");
    }


    private void initRecyclerView() {
        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(myLinearLayoutManager);
        mEasyRecyclerView.setAdapterWithProgress(mAdapter = new Adapter<UARTBean.ListBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

                return new ViewHolder<UARTBean.ListBean>(parent, R.layout.robot_historical_recycler_item) {

                    private TextView mTime, mScore;
                    private BGAProgressBar bga_pd_progress,
                            bga_pdcount_progress, bga_blow_progress;

                    @Override
                    public void initView() {
                        mTime = $(R.id.tv_time);
                        mScore = $(R.id.tv_score);
                        bga_pd_progress = $(R.id.bga_pd_progress);
                        bga_pdcount_progress = $(R.id.bga_pdcount_progress);
                        bga_blow_progress = $(R.id.bga_blow_progress);
                    }

                    @Override
                    public void setData(UARTBean.ListBean data) {
                        super.setData(data);
                        mTime.setText(data.getCreateAt());
                        mScore.setText(String.valueOf(data.getScore()));
                        bga_pd_progress.setProgress((int) data.getStrengthAverage());
                        bga_pdcount_progress.setProgress(data.getPressTime());
                        bga_blow_progress.setProgress(data.getBreatheTime());
                    }
                };
            }
        });

        mEasyRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                uartRobotControlPresenter.queryUARTData(mAdapter.refreshPage(),"");
            }
        });

        mAdapter.setMore(new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                uartRobotControlPresenter.queryUARTData(mAdapter.getNextPage(),"");
            }

            @Override
            public void onMoreClick() {
            }
        });

    }



    @Override
    public void queryUARTDataSuccess(List<UARTBean.ListBean> uartBeans) {
        Logger.d("数据返回-》"+uartBeans);
        mAdapter.addAll(uartBeans);
    }

    @Override
    public void queryUARTDataFail(String info) {
        mApp.shortToast(info);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
