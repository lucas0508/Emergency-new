package com.tjmedicine.emergency.ui.navi;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.model.widget.ScaleTransitionPagerTitleView;
import com.tjmedicine.emergency.ui.navi.adapter.TabFragmentAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NaviActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager mVpHome;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> mTitleArray = new ArrayList<>();
    private List<String> status = new ArrayList<>();


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_navi;
    }

    @Override
    protected void initView() {
        mTitle.setText("请选择出行方式");
//                // 驾车路径规划
//                 new DemoDetails(R.string.navi_route_driver_title, R.string.navi_route_driver_desc, DriverListActivity.class),
//                // 步行路径规划
//                new DemoDetails(R.string.navi_route_walk_title, R.string.navi_route_walk_desc, WalkRouteCalculateActivity.class),
//                // 骑行路径规划
//                new DemoDetails(R.string.navi_route_ride_title, R.string.navi_route_ride_desc, RideRouteCalculateActivity.class),
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.WHITE);
        mTitleArray.add("驾车路线");
        mTitleArray.add("骑行路线");
        mTitleArray.add("步行路线");
        status.add("0");
        status.add("1");
        status.add("2");
        for (int i = 0; i < mTitleArray.size(); i++) {
            fragments.add(addFragmentnewInstance(status.get(i), i));
        }
        TabFragmentAdapter mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments, mTitleArray);
        mVpHome.setAdapter(mAdapter);
        mVpHome.setCurrentItem(0);

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments == null ? 0 : fragments.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mTitleArray.get(index));
                simplePagerTitleView.setTextSize(22);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.parseColor("#F21743"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVpHome.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            /**
             * 设置下划线
             * @param context
             * @return
             */
            @Override
            public IPagerIndicator getIndicator(Context context) {
               /* BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"),
                Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"));*/
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 6));
                indicator.setRoundRadius(UIUtil.dip2px(context, 2));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#F21743"));
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mVpHome);
    }

    public TabFragment addFragmentnewInstance(String status, int position) {
        TabFragment homeTabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mQueryType", status);
        homeTabFragment.setArguments(bundle);
        return homeTabFragment;
    }


//    /**
//     * 自定义Tab的View
//     *
//     * @param currentPosition
//     * @return
//     */
//    private View getTabView(int currentPosition) {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab, null);
//        TextView textView = view.findViewById(R.id.tab_item_textview);
//        textView.setText(status.get(currentPosition));
//        return view;
//    }
}
