package com.tjmedicine.emergency.ui.uart;

import android.graphics.Color;
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
        uartRobotControlPresenter.queryUARTData(null, "");
    }


    private void initRecyclerView() {
        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(myLinearLayoutManager);
        mEasyRecyclerView.setAdapterWithProgress(mAdapter = new Adapter<UARTBean.ListBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

                return new ViewHolder<UARTBean.ListBean>(parent, R.layout.robot_historical_recycler_item) {

                    private TextView mTime, mScore, tv_report_pd, tv_report_pd_regular, tv_pd_rebound;
                    private BGAProgressBar
                            bga_pdcount_progress, bga_blow_progress;

                    @Override
                    public void initView() {
                        mTime = $(R.id.tv_time);
                        mScore = $(R.id.tv_score);
                        bga_pdcount_progress = $(R.id.bga_pdcount_progress);
                        bga_blow_progress = $(R.id.bga_blow_progress);
                        tv_report_pd = $(R.id.tv_report_pd);
                        tv_report_pd_regular = $(R.id.tv_report_pd_regular);
                        tv_pd_rebound = $(R.id.tv_pd_rebound);
                    }

                    @Override
                    public void setData(UARTBean.ListBean data) {
                        super.setData(data);
                        mTime.setText(data.getCreateAt());
                        if (null != data.getPdResult()) {
                            if (data.getPdResult().equals("1")) {
                                mScore.setTextColor(Color.GREEN);
                            }else {
                                mScore.setTextColor(Color.RED);
                            }
                            mScore.setText(data.getPdResultStr());
                        }
                        if (null != data.getPdQualifiedCount())
                            bga_pdcount_progress.setProgress(data.getPdQualifiedCount());
                        if (null != data.getBlowCount())
                            bga_blow_progress.setProgress(data.getBlowCount());
                        if (null != data.getPdFrequency())
                            tv_report_pd.setText(data.getPdFrequency() + "次/分");
                        if (null != data.getPdDepth())
                            tv_report_pd_regular.setText(data.getPdDepth() + "%");
                        if (null != data.getPdRebound())
                            tv_pd_rebound.setText(data.getPdRebound() + "%");
                    }
                };
            }
        });

        mEasyRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                uartRobotControlPresenter.queryUARTData(mAdapter.refreshPage(), "");
            }
        });

        mAdapter.setMore(new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                uartRobotControlPresenter.queryUARTData(mAdapter.getNextPage(), "");
            }

            @Override
            public void onMoreClick() {
            }
        });

    }


    @Override
    public void queryUARTDataSuccess(List<UARTBean.ListBean> uartBeans) {
        Logger.d("数据返回-》" + uartBeans);
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
