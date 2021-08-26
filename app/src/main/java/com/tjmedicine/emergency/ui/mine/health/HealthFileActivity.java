package com.tjmedicine.emergency.ui.mine.health;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.Adapter;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.base.ViewHolder;
import com.tjmedicine.emergency.common.bean.HealthAllFileBeen;
import com.tjmedicine.emergency.common.bean.HealthFileBeen;
import com.tjmedicine.emergency.common.bean.HealthTagBeen;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.bean.TeachData;
import com.tjmedicine.emergency.ui.mine.contact.view.EmergencyContactActivity;
import com.tjmedicine.emergency.ui.mine.health.presenter.HealthPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HealthFileActivity extends BaseActivity implements IHealthView {

    private HealthPresenter healthPresenter = new HealthPresenter(this);


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycler_view)
    EasyRecyclerView mEasyRecyclerView;
    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.tv_count)
    TextView mCount;
    private Adapter<HealthFileBeen> mAdapter;
    private List<HealthFileBeen> healthFileBeens = new ArrayList<>();
    private int pos;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_healthgfile;
    }

    @Override
    protected void initView() {
        tv_title.setText("健康档案");
        initRecyclerView();
        initListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mApp.getLoadingDialog().show();
        healthPresenter.findHealthRecordsList();
    }

    private void initListeners() {
        btn_add.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                startActivity(HealthAddFileActivity.class);
            }
        });
    }


    private void initRecyclerView() {
        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(myLinearLayoutManager);

        mEasyRecyclerView.setAdapterWithProgress(mAdapter = new Adapter<HealthFileBeen>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

                return new ViewHolder<HealthFileBeen>(parent, R.layout.activity_health_recycler_item) {

                    private TextView tv_home_title,
                            tv_home_content, tv_age;

                    @Override
                    public void initView() {
                        tv_home_title = $(R.id.tv_title);
                        tv_home_content = $(R.id.tv_content);
                        tv_age = $(R.id.tv_age);
                    }

                    @Override
                    public void setData(HealthFileBeen data) {
                        super.setData(data);
                        Log.e("TAG", "setData: " + data.getAge());
                        tv_home_title.setText(data.getUsername());
                        tv_home_content.setText(data.getBirthday());
                        tv_age.setText(data.getAge());
                        Glide.with(getApplicationContext());
                    }

                };
            }

            @Override
            public int getViewType(int position) {
                return super.getViewType(position);
            }
        });

        mEasyRecyclerView.setEmptyView(R.layout.recycler_empty);

        mAdapter.setOnItemClickListener(position -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", mAdapter.getItem(position).getId());
            startActivity(HealthFileDetailActivity.class, bundle);
        });

        mAdapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {

                final AlertDialog dialog = new AlertDialog.Builder(HealthFileActivity.this)
                        .setTitle("提示")
                        .setMessage("是否删除该健康档案？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pos = position;
                                healthPresenter.delHealth(String.valueOf(mAdapter.getItem(position).getId()));
                            }
                        })
                        .setCancelable(false)
                        .show(); // this must be show() or the getButton() below will return null.
                return false;
            }
        });
    }

    @Override
    public void addHealthRecordsSuccess() {

    }

    @Override
    public void addHealthRecordsFail(String info) {

    }

    @Override
    public void delHealthRecordsSuccess() {
        mApp.longToast("删除成功");
        mAdapter.remove(pos);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void delHealthRecordsFail(String info) {
    }

    @Override
    public void findHealthRecordsListSuccess(List<HealthFileBeen> healthFileBeens) {
        mApp.getLoadingDialog().hide();
        this.healthFileBeens = healthFileBeens;

        int count = 3;
        int vi = count - healthFileBeens.size();
        mCount.setText("已添加" + healthFileBeens.size() + "位,还可以添加" + vi + "人");
        mAdapter.update(healthFileBeens);
        if (healthFileBeens.size() >= 3) {
            btn_add.setVisibility(View.GONE);
        }
    }

    @Override
    public void findHealthRecordsListFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

    @Override
    public void findHealthRecordsDetailSuccess(HealthAllFileBeen healthFileBeen) {

    }

    @Override
    public void findHealthRecordsDetailFail(String info) {

    }


    @Override
    public void findHealthRecordsTagSuccess(List<HealthTagBeen> healthTagBeens) {

    }

    @Override
    public void findHealthRecordsTagFail(String info) {

    }
}
