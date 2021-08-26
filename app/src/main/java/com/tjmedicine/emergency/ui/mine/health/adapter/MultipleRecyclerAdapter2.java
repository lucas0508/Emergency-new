package com.tjmedicine.emergency.ui.mine.health.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.bean.HealthTagBeen;
import com.tjmedicine.emergency.ui.mine.health.CheckableLayout;
import com.tjmedicine.emergency.ui.mine.health.HealthAddFileActivity;

public class MultipleRecyclerAdapter2 extends RecyclerView.Adapter<MultipleRecyclerAdapter2.ViewHolder> {

    private Context mContext;
    private List<HealthTagBeen.ChildsBean> mListData = new ArrayList<>();


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public MultipleRecyclerAdapter2(Context mContext, List<HealthTagBeen.ChildsBean> mListData) {
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView typeTv;
        CheckableLayout rootLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTv = (TextView) itemView.findViewById(R.id.alive_type_tv);
            rootLayout = (CheckableLayout) itemView.findViewById(R.id.root_layout);
        }
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_multple, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set<Integer> positionSet = HealthAddFileActivity.positionSet2;
        //检查set里是否包含position，包含则显示选中的背景色，不包含则反之
        if (positionSet.contains(position)) {
            holder.rootLayout.setChecked(true);
            holder.typeTv.setTextColor(mContext.getResources().getColor(R.color.dark_orange));
            holder.rootLayout.setBackgroundResource(R.drawable.background_tab_menu);
        } else {
            holder.rootLayout.setChecked(false);
            holder.rootLayout.setBackgroundResource(R.drawable.background_tab_menu_false);
            holder.typeTv.setTextColor(mContext.getResources().getColor(R.color.beige));
        }

        Log.e("aaaaa", " addddd--->" + new Gson().toJson(positionSet));

        holder.typeTv.setText(mListData.get(position).getValue());
        Log.e("aaaaa", " adddddddddddddddd--->" + new Gson().toJson(mListData));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.OnItemClick(holder.itemView, pos);
                    holder.rootLayout.setChecked(true);
                }
            });
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
