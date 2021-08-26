package com.tjmedicine.emergency.ui.mine.health;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.HealthAllFileBeen;
import com.tjmedicine.emergency.common.bean.HealthFileBeen;
import com.tjmedicine.emergency.common.bean.HealthTagBeen;
import com.tjmedicine.emergency.ui.mine.health.adapter.MultipleRecyclerAdapter;
import com.tjmedicine.emergency.ui.mine.health.adapter.MultipleRecyclerAdapter2;
import com.tjmedicine.emergency.ui.mine.health.presenter.HealthPresenter;
import com.tjmedicine.emergency.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

public class HealthFileDetailActivity extends BaseActivity implements IHealthView {

    private HealthPresenter healthPresenter = new HealthPresenter(this);

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.recycler_allergy)
    RecyclerView mRecyclerAllergy;
    @BindView(R.id.tv_tag1)
    TextView tv_tag1;
    @BindView(R.id.tv_tag2)
    TextView tv_tag2;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.tv_bloodType)
    TextView tv_bloodType;
    @BindView(R.id.et_height)
    EditText et_height;
    @BindView(R.id.et_weight)
    EditText et_weight;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_idNum)
    EditText et_idNum;
    @BindView(R.id.btn_apply)
    Button btn_apply;
    @BindView(R.id.ll_type1)
    LinearLayout ll_type1;
    @BindView(R.id.ll_type2)
    LinearLayout ll_type2;
    private List<HealthTagBeen.ChildsBean> mListData;
    private MultipleRecyclerAdapter mAdapter;
    private MultipleRecyclerAdapter2 mAdapter2;
    public static Set<Integer> positionSet = new HashSet<>();
    public static Set<Integer> positionSet2 = new HashSet<>();
    private boolean selectMode = true; //选择模式 多选或者单选  true  多选
    private String checktypeId = ""; //记录选择种类 多种类以" ,"分开
    public Set<Integer> checkTYpeNameSet = new HashSet<>();
    private List<HealthTagBeen.ChildsBean> childs1 = new ArrayList<>();
    private List<HealthTagBeen.ChildsBean> childs2 = new ArrayList<>();
    private int id;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_health_file;
    }

    @Override
    protected void initView() {
        mTitle.setText("详情");
        id = getIntent().getIntExtra("id", 0);
        healthPresenter.findHealthRecordsDetail(id);
        healthPresenter.findHealthRecordsTag();
        initRecyclerView();
        initData();
        initListener();
    }

    private void initRecyclerView() {
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        FlexboxLayoutManager manager1 = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerAllergy.setLayoutManager(manager1);
    }

    private void initData() {


        mListData = new ArrayList<>();
        mAdapter = new MultipleRecyclerAdapter(HealthFileDetailActivity.this, childs1);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter2 = new MultipleRecyclerAdapter2(HealthFileDetailActivity.this, childs2);
        mRecyclerAllergy.setAdapter(mAdapter2);

        btn_apply.setVisibility(View.GONE);

    }

    private void initListener() {

        btn_apply.setOnClickListener(new OnMultiClickListener() {

            @Override
            public void onMultiClick(View v) {
                //   submit();
            }
        });
        mAdapter.setOnItemClickListener(new MultipleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                addOrRemove(position);
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });

        mAdapter2.setOnItemClickListener(new MultipleRecyclerAdapter2.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                addOrRemove2(position);
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });

    }

    private void submit() {
        Log.e("info 选中数据", checkTYpeNameSet.toString());

        if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
            mApp.shortToast("请输入姓名!");
            return;
        }
        if (!ValidateUtils.validatePhone(et_phone.getText().toString().trim())) {
            mApp.shortToast("请输入正确的手机号!");
            return;
        }
        if (TextUtils.isEmpty(et_idNum.getText().toString().trim())) {
            mApp.shortToast("请输入正确身份证号!");
            return;
        }
        Map<String, Object> stringObjectMap = new HashMap<>();

        stringObjectMap.put("username", et_name.getText().toString().trim());
        stringObjectMap.put("bloodType", "保密");
        stringObjectMap.put("height", et_height.getText().toString().trim());
        stringObjectMap.put("weight", et_weight.getText().toString().trim());
        stringObjectMap.put("phone", et_phone.getText().toString().trim());
        stringObjectMap.put("idNum", et_idNum.getText().toString().trim());
        stringObjectMap.put("list", checkTYpeNameSet);

        mApp.getLoadingDialog().show();
        healthPresenter.addHealthRecords(stringObjectMap);

    }


    private void addOrRemove(int position) {
        if (positionSet.contains(position)) {
            // 如果包含，则撤销选择
            positionSet.remove(position);
            checkTYpeNameSet.remove(childs1.get(position).getId());
        } else {
            // 如果不包含，则添加
            positionSet.add(position);
            checkTYpeNameSet.add(childs1.get(position).getId());
        }
        if (positionSet.size() == 0) {
            // 如果没有选中任何的item，则退出多选模式
            mAdapter.notifyDataSetChanged();
            selectMode = false;
        } else {
            // 更新列表界面，否则无法显示已选的item
            mAdapter.notifyDataSetChanged();
        }
    }

    private void addOrRemove2(int position) {
        if (positionSet2.contains(position)) {
            // 如果包含，则撤销选择
            positionSet2.remove(position);
            checkTYpeNameSet.remove(childs2.get(position).getId());
        } else {
            // 如果不包含，则添加
            positionSet2.add(position);
            checkTYpeNameSet.add(childs2.get(position).getId());
        }
        if (positionSet2.size() == 0) {
            // 如果没有选中任何的item，则退出多选模式
            mAdapter2.notifyDataSetChanged();
            selectMode = false;
        } else {
            // 更新列表界面，否则无法显示已选的item
            mAdapter2.notifyDataSetChanged();
        }

        Log.e("info", positionSet2.toString());

        //  Toast.makeText(HealthAddFileActivity.this, checkTYpeNameSet.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        positionSet.clear();
        positionSet2.clear();
    }

    @Override
    public void addHealthRecordsSuccess() {
        mApp.getLoadingDialog().hide();
        mApp.shortToast("添加成功！");
        finish();
    }

    @Override
    public void addHealthRecordsFail(String info) {
        mApp.getLoadingDialog().hide();
        mApp.shortToast(info);
    }

    @Override
    public void delHealthRecordsSuccess() {

    }

    @Override
    public void delHealthRecordsFail(String info) {

    }

    @Override
    public void findHealthRecordsListSuccess(List<HealthFileBeen> contactBean) {

    }

    @Override
    public void findHealthRecordsListFail(String info) {

    }

    @Override
    public void findHealthRecordsDetailSuccess(HealthAllFileBeen healthFileBeen) {
        et_name.setText(healthFileBeen.getUsername());
        tv_bloodType.setText(healthFileBeen.getBloodType());
        et_height.setText(String.valueOf(healthFileBeen.getHeight()));
        et_weight.setText(String.valueOf(healthFileBeen.getWeight()));
        et_phone.setText(healthFileBeen.getPhone());
        et_idNum.setText(healthFileBeen.getIdNum());

        List<HealthAllFileBeen.TagsBean> tags = healthFileBeen.getTags();
        if (null != tags && tags.size() > 0) {
            if (tags.size() > 1) {
                List<HealthAllFileBeen.TagsBean.ChildsBean> child11 = tags.get(0).getChilds();

                Logger.d("数据："+new Gson().toJson(child11));
                tv_tag1.setText(tags.get(0).getValue());
                for (int i = 0; i < child11.size(); i++) {
                    HealthTagBeen.ChildsBean childsBean1 = new HealthTagBeen.ChildsBean();
                    childsBean1.setId(child11.get(i).getId());
                    childsBean1.setValue(child11.get(i).getValue());
                    childs1.add(childsBean1);
                }
                mAdapter.update(childs1);
            } else {
                ll_type1.setVisibility(View.GONE);
            }
            if (tags.size() == 2) {
                tv_tag2.setText(tags.get(1).getValue());
                List<HealthAllFileBeen.TagsBean.ChildsBean> childs22 = tags.get(1).getChilds();
                for (int i = 0; i < childs22.size(); i++) {
                    HealthTagBeen.ChildsBean childsBean2 = new HealthTagBeen.ChildsBean();
                    childsBean2.setId(childs22.get(i).getId());
                    childsBean2.setValue(childs22.get(i).getValue());
                    childs2.add(childsBean2);
                }
                mAdapter2.update(childs2);
            } else {
                ll_type2.setVisibility(View.GONE);
            }
        } else {
            ll_type1.setVisibility(View.GONE);
            ll_type2.setVisibility(View.GONE);
        }

    }

    @Override
    public void findHealthRecordsDetailFail(String info) {

    }


    @Override
    public void findHealthRecordsTagSuccess(List<HealthTagBeen> healthTagBeens) {
//        tv_tag1.setText(healthTagBeens.get(0).getValue());
//        tv_tag2.setText(healthTagBeens.get(1).getValue());
//        childs1 = healthTagBeens.get(0).getChilds();
//        childs2 = healthTagBeens.get(1).getChilds();
//        mAdapter.update(childs1);
//        mAdapter2.update(childs2);

    }

    @Override
    public void findHealthRecordsTagFail(String info) {

    }
}
