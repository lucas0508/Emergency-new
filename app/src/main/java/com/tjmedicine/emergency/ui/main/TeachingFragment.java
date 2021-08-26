package com.tjmedicine.emergency.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.Adapter;
import com.tjmedicine.emergency.common.base.BaseFragment;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.base.ViewHolder;
import com.tjmedicine.emergency.common.bean.BannerBean;
import com.tjmedicine.emergency.common.bean.TeachingBean;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.model.widget.RecycleViewDivider;
import com.tjmedicine.emergency.ui.bean.TeachData;
import com.tjmedicine.emergency.ui.other.WebActivity;
import com.tjmedicine.emergency.ui.teach.presenter.BannerPresenter;
import com.tjmedicine.emergency.ui.teach.presenter.TeachingPresenter;
import com.tjmedicine.emergency.ui.teach.view.IBannerView;
import com.tjmedicine.emergency.ui.teach.view.ITeachingView;
import com.tjmedicine.emergency.ui.teach.view.TeachingDetailActivity;
import com.tjmedicine.emergency.ui.teach.view.test;
import com.tjmedicine.emergency.ui.uart.UARTActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

public class TeachingFragment extends BaseFragment implements IBannerView, ITeachingView {
//    Intent intent = new Intent(getActivity(), UARTActivity.class);
//    getActivity().startActivity(intent);

    private TeachingPresenter teachingPresenter = new TeachingPresenter(this);

    private BannerPresenter bannerPresenter = new BannerPresenter(this);
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.ib_close)
    ImageButton mClose;
    @BindView(R.id.recycler_view)
    EasyRecyclerView mEasyRecyclerView;
    @BindView(R.id.banner)
    BGABanner bgaBanner;
    List<String> bannerList = new ArrayList<>();
    List<String> bannerListUrl = new ArrayList<>();
    List<String> bannerListType = new ArrayList<>();
    private Adapter<TeachingBean.ListBean> mAdapter;

    @Override
    protected void initView() {
        mTitle.setText("急救学堂");
        mClose.setVisibility(View.GONE);
        initBanner();
        initRecyclerView();
        bannerPresenter.queryBannerData();
        teachingPresenter.findTeachingList(mAdapter.refreshPage());
    }

    private void initBanner() {
         }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_teaching;
    }


    private void initRecyclerView() {

        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(getActivity());
        mEasyRecyclerView.setLayoutManager(myLinearLayoutManager);

        mEasyRecyclerView.setAdapterWithProgress(mAdapter = new Adapter<TeachingBean.ListBean>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

                return new ViewHolder<TeachingBean.ListBean>(parent, R.layout.fragment_teach_recycler_item) {

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
                    public void setData(TeachingBean.ListBean data) {
                        super.setData(data);
                        tv_home_title.setText(data.getTitle());
                        tv_home_content.setText(data.getIntroduce());
                        Glide.with(requireActivity())
                                .load(data.getBaseUrl())
                                .into(riv_image);
                    }
                };
            }
        });
        mEasyRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                teachingPresenter.findTeachingList(mAdapter.refreshPage());
            }
        });
//        mAdapter.setMore(new RecyclerArrayAdapter.OnMoreListener() {
//            @Override
//            public void onMoreShow() {
//
//                // TODO: 2021-03-30 修改下拉刷新   mAdapter.getNextPage()
//
//                teachingPresenter.findTeachingList(mAdapter.refreshPage());
//            }
//
//            @Override
//            public void onMoreClick() {
//            }
//        });


        mAdapter.setOnItemClickListener(position -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("teachingBean", mAdapter.getItem(position));
            startActivity(TeachingDetailActivity.class, bundle);
//          startActivity(test.class, bundle);
        });
    }

    @Override
    public void queryBannerSuccess(List<BannerBean> bannerBeanList) {






        bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(requireActivity())
                        .load(model)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        for (BannerBean url : bannerBeanList) {
            String imgUrl = url.getImgUrl();
            bannerList.add(imgUrl);
            bannerListUrl.add(url.getHttpUrl());
            bannerListType.add(url.getType());
        }

//        bannerList.set(0,"https://bkimg.cdn.bcebos.com/pic/5fdf8db1cb134954fccc766a564e9258d0094aea?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2U4MA==,g_7,xp_5,yp_5/format,f_auto");
//        bannerList.set(1,"https://img0.baidu.com/it/u=2958138263,3784611146&fm=26&fmt=auto&gp=0.jpg");
        //bannerList.set(2,"https://img0.baidu.com/it/u=3782916108,2692951759&fm=26&fmt=auto&gp=0.jpg");
        bgaBanner.setData(bannerList, null);
        bgaBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                //Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
//                String url = bannerListUrl.get(position);
//                Bundle intent = new Bundle();
//                intent.putString(Constants.WEB_KEY_URL, url);
//                intent.putInt(Constants.WEB_KEY_FLAG, 1);
//                intent.putString(Constants.WEB_KEY_TYPE, bannerListType.get(position));
//                intent.putInt("activity_id", bannerBeanList.get(position).getActivityId());
//                intent.putString("play_url", bannerBeanList.get(position).getPlayUrl());
//                startActivity(WebActivity.class, intent);
            }
        });
    }

    @Override
    public void queryBannerFail(String info) {

    }

    @Override
    public void findTeachingSuccess(List<TeachingBean.ListBean> listBeans) {
//        listBeans.get(0).setBaseUrl("https://i2cut.vzan.cc/image/liveImage/jpeg/2021/1/20/0431228e08becc725447dc86e6f9513561af8b.jpeg");
//        listBeans.get(1).setBaseUrl("https://mmbiz.qpic.cn/mmbiz_jpg/DiaezH07koIHicbAmJdL6AMsgIpDwQUJYEicckUe6vibOJGLUTPIKnbh0NmW7qxxj8SZkwpNib9E9peA2BKHSNoL4DQ/640?wx_fmt=jpeg");
//        listBeans.get(2).setBaseUrl("https://135editor.cdn.bcebos.com/uploadword/1761401/202011/5fb79bc6-ecac-458a-9bc0-1124ac10006b.jpg");
//        listBeans.get(3).setBaseUrl("https://mmbiz.qpic.cn/mmbiz_jpg/DiaezH07koIGlmc97vONjvaTHzibfSNIkQHzvRIFRrzocjzyib6CmtxibQ0sbONP5afMYvTnRtE6oFXgPvITuZH5RQ/640?wx_fmt=jpeg");
        mAdapter.addAll(listBeans);
    }

    @Override
    public void findTeachingFail(String info) {

    }

    @Override
    public void findTeachingDetailSuccess() {

    }

    @Override
    public void findTeachingDetailFail(String info) {

    }
}
