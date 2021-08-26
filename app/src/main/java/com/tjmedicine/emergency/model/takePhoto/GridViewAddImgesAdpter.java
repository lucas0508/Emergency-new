package com.tjmedicine.emergency.model.takePhoto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;

import java.util.Arrays;
import java.util.List;

public class GridViewAddImgesAdpter extends BaseAdapter {
    List<String> mBusinessPictureList;
    private Context context;
    private LayoutInflater inflater;
    /**
     * 可以动态设置最多上传几张，之后就不显示+号了，用户也无法上传了
     * 默认9张
     */
    private int maxImages = 6;

    public GridViewAddImgesAdpter(List<String> mBusinessPictureList, Context context) {
        this.mBusinessPictureList = mBusinessPictureList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 获取最大上传张数
     *
     * @return
     */
    public int getMaxImages() {
        return maxImages;
    }

    /**
     * 设置最大上传张数
     *
     * @param maxImages
     */
    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    boolean isShow = false;

    /**
     * 让GridView中的数据数目加1最后一个显示+号
     * 当到达最大张数时不再显示+号
     *
     * @return 返回GridView中的数量
     */
    @Override
    public int getCount() {

        int count = mBusinessPictureList == null ? 1 : mBusinessPictureList.size() + 1;

        Log.e("", "getView: count--" + count);
        Log.e("", "getView: images--" + mBusinessPictureList.size());

        if (count > maxImages || isShow) {
            return mBusinessPictureList.size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<String> mBusinessPictureList) {
        this.mBusinessPictureList = mBusinessPictureList;
        this.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<String> mBusinessPictureList, boolean isShow) {
        this.mBusinessPictureList = mBusinessPictureList;
        this.isShow = isShow;
        this.notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_addimage, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**代表+号之前的需要正常显示图片**/


        if (isShow) {
            Logger.e("Arrays.mBusinessPictureList" + Arrays.asList(mBusinessPictureList));
            viewHolder.btdel.setVisibility(View.GONE);
            viewHolder.btdel.setOnClickListener(null);
            viewHolder.ivimage.setOnClickListener(null);

            if (mBusinessPictureList != null && position < mBusinessPictureList.size()) {
                Glide.with(context).load(mBusinessPictureList.get(position))
                        .apply(new RequestOptions().placeholder(R.mipmap.upload_defaut).centerCrop())
                        .into(viewHolder.ivimage);
            } else {
                viewHolder.btdel.setVisibility(View.GONE);
            }

        } else {
            if (mBusinessPictureList != null && position < mBusinessPictureList.size()) {
                // final File file = new File(mBusinessPictureList.get(position));

                Glide.with(context).load(mBusinessPictureList.get(position))
                        .apply(new RequestOptions().placeholder(R.mipmap.upload_defaut).centerCrop())
                        .into(viewHolder.ivimage);
                viewHolder.btdel.setVisibility(View.VISIBLE);
                viewHolder.btdel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    if (file.exists()) {
//                        file.delete();
//                    }
                        mBusinessPictureList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            } else {
                /**代表+号的需要+号图片显示图片**/
                Glide.with(context)
                        .load(R.mipmap.upload_bg)
                        .into(viewHolder.ivimage);
                viewHolder.ivimage.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.btdel.setVisibility(View.GONE);

            }
        }

        return convertView;

    }

    public class ViewHolder {
        public final ImageView ivimage;
        public final Button btdel;
        public final View root;

        public ViewHolder(View root) {
            ivimage = (ImageView) root.findViewById(R.id.iv_image);
            btdel = (Button) root.findViewById(R.id.bt_del);
            this.root = root;
        }
    }
}
