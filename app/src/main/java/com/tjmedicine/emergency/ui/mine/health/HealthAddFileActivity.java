package com.tjmedicine.emergency.ui.mine.health;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.HealthAllFileBeen;
import com.tjmedicine.emergency.common.bean.HealthFileBeen;
import com.tjmedicine.emergency.common.bean.HealthTagBeen;
import com.tjmedicine.emergency.model.widget.SelectBloodTypeDialog;
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

public class HealthAddFileActivity extends BaseActivity implements IHealthView {

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

    private List<HealthTagBeen.ChildsBean> mListData;
    private MultipleRecyclerAdapter mAdapter;
    private MultipleRecyclerAdapter2 mAdapter2;
    public static Set<Integer> positionSet = new HashSet<>();
    public static Set<Integer> positionSet2 = new HashSet<>();
    private boolean selectMode = true; //选择模式 多选或者单选  true  多选
    private String checktypeId = ""; //记录选择种类 多种类以" ,"分开
    public Set<Integer> checkTYpeNameSet = new HashSet<>();
    private List<HealthTagBeen.ChildsBean> childs1;
    private List<HealthTagBeen.ChildsBean> childs2;
    private List<HealthTagBeen> healthTagBeens;
    private String bloodType="";

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_health_file;
    }

    @Override
    protected void initView() {
        mTitle.setText("新增健康档案");

        initRecyclerView();

        initData();
        healthPresenter.findHealthRecordsTag();
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
        mAdapter = new MultipleRecyclerAdapter(HealthAddFileActivity.this, childs1);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter2 = new MultipleRecyclerAdapter2(HealthAddFileActivity.this, childs2);
        mRecyclerAllergy.setAdapter(mAdapter2);
    }

    private void initListener() {

        btn_apply.setOnClickListener(new OnMultiClickListener() {

            @Override
            public void onMultiClick(View v) {
                submit();
            }
        });
        tv_bloodType.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                SelectBloodTypeDialog selectBloodTypeDialog = new SelectBloodTypeDialog(HealthAddFileActivity.this);
                selectBloodTypeDialog.show(new SelectBloodTypeDialog.SelectSexListener() {
                    @Override
                    public void setSelectSex(String str, String sex) {
                        bloodType = str;
                        tv_bloodType.setText(str);
                    }
                });
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
        if (TextUtils.isEmpty(bloodType)){
            mApp.shortToast("请选择血型!");
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

        stringObjectMap.put("bloodType", bloodType);
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
            checkTYpeNameSet.add(healthTagBeens.get(0).getId());
            checkTYpeNameSet.add(childs1.get(position).getId());
        }
        if (positionSet.size() == 0) {
            // 如果没有选中任何的item，则退出多选模式
            checkTYpeNameSet.remove(healthTagBeens.get(0).getId());
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
            checkTYpeNameSet.add(healthTagBeens.get(1).getId());
            checkTYpeNameSet.add(childs2.get(position).getId());
        }
        if (positionSet2.size() == 0) {
            // 如果没有选中任何的item，则退出多选模式
            checkTYpeNameSet.remove(healthTagBeens.get(1).getId());
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

    }

    @Override
    public void findHealthRecordsDetailFail(String info) {

    }


    @Override
    public void findHealthRecordsTagSuccess(List<HealthTagBeen> healthTagBeen) {
        healthTagBeens = healthTagBeen;
        tv_tag1.setText(healthTagBeens.get(0).getValue());
        tv_tag2.setText(healthTagBeens.get(1).getValue());
        childs1 = healthTagBeens.get(0).getChilds();
        childs2 = healthTagBeens.get(1).getChilds();
        mAdapter.update(childs1);
        mAdapter2.update(childs2);
    }

    @Override
    public void findHealthRecordsTagFail(String info) {

    }
}
