package com.tjmedicine.emergency.ui.mine.health.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.bean.HealthTagBeen;
import com.tjmedicine.emergency.ui.mine.health.CheckableLayout;
import com.tjmedicine.emergency.ui.mine.health.HealthAddFileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultipleRecyclerAdapter233333333333 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<HealthTagBeen.ChildsBean> mListData = new ArrayList<>();

    private final int TEXT_TYPE_HEADER = 1;
    private final int TEXT_TYPE = 2;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListData.get(position).getType() == TEXT_TYPE_HEADER) {
            return TEXT_TYPE_HEADER;
        } else if (mListData.get(position).getType() == TEXT_TYPE) {
            return TEXT_TYPE;
        }
        return super.getItemViewType(position);
    }

    public MultipleRecyclerAdapter233333333333(Context mContext, List<HealthTagBeen.ChildsBean> mListData) {
        //  mListData = new ArrayList<>();
        this.mContext = mContext;
        this.mListData = mListData;
    }

    public void update(List<HealthTagBeen.ChildsBean> list) {
        if (list != null && list.size() > 0) {
//            mListData.addAll(list);
            mListData = list;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView typeTv;
        CheckableLayout rootLayout;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            typeTv = (TextView) itemView.findViewById(R.id.alive_type_tv);
            rootLayout = (CheckableLayout) itemView.findViewById(R.id.root_layout);
        }
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView tv_header;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            tv_header = (TextView) itemView.findViewById(R.id.tv_header);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view;
        if (viewType == TEXT_TYPE_HEADER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_multple_header, viewGroup, false);
            return new ViewHolder1(view);
        } else if (viewType == TEXT_TYPE) {
             view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_multple, viewGroup, false);
            return new ViewHolder2(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Set<Integer> positionSet = HealthAddFileActivity.positionSet2;
        //检查set里是否包含position，包含则显示选中的背景色，不包含则反之

//        Log.e("数据源", " 父标题--->" + new Gson().toJson(mListData.get(position).getValue()));

        Log.e("数据源", " 父标题--aaaaaaa->" + new Gson().toJson(mListData.get(0).getValue()));

        Log.e("数据源", " 父标题--aaaaaaa->" + new Gson().toJson(mListData.get(1).getValue()));

        if (holder instanceof ViewHolder1) {
            ((ViewHolder1) holder).tv_header.setText(mListData.get(position).getValue());
            Log.e("数据源", " 父标题--->" + new Gson().toJson(mListData.get(position).getValue()));

        } else if (holder instanceof ViewHolder2) {

            Log.e("aaaaa", " add--->" + new Gson().toJson(positionSet));
            if (positionSet.contains(position)) {
                ((ViewHolder2) holder).rootLayout.setChecked(true);
                ((ViewHolder2) holder).typeTv.setTextColor(mContext.getResources().getColor(R.color.dark_orange));
                ((ViewHolder2) holder).rootLayout.setBackgroundResource(R.drawable.background_tab_menu);
            } else {
                ((ViewHolder2) holder).rootLayout.setChecked(false);
                ((ViewHolder2) holder).rootLayout.setBackgroundResource(R.drawable.background_tab_menu_false);
                ((ViewHolder2) holder).typeTv.setTextColor(mContext.getResources().getColor(R.color.beige));
            }

            Log.e("aaaaa", " addddd--->" + new Gson().toJson(positionSet));

            ((ViewHolder2) holder).typeTv.setText(mListData.get(position).getValue());
            Log.e("aaaaa", " adddddddddddddddd--->" + new Gson().toJson(mListData));

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.OnItemClick(holder.itemView, pos);
                        ((ViewHolder2) holder).rootLayout.setChecked(true);
                    }
                });
            }
        }
    }



    @Override
    public int getItemCount() {
        return mListData != null ? mListData.size() : 0;
    }


    public interface OnItemClickListener {

        //单点监听
        void OnItemClick(View view, int position);

        //长点监听
        void OnItemLongClick(View view, int position);
    }

}
