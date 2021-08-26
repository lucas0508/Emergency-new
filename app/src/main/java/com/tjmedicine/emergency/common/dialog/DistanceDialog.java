package com.tjmedicine.emergency.common.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.ui.main.HomePageFragment;

public class DistanceDialog extends BottomPopupView implements INaviInfoCallback {

    private float distance;
    private String title;
    private String address;
    private Context mContext;
    private String getAddress;
    private double latitude;
    private double longitude;
    //地理编码
    private GeocodeSearch geocodeSearch;

    public DistanceDialog(@NonNull Context context, float distance, String title, String address, double latitude, double longitude, String getAddress) {
        super(context);
        mContext = context;
        this.distance = distance;
        this.title = title;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.getAddress = getAddress;

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.homepage_click_item;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        TextView mName = findViewById(R.id.tv_name);
        TextView mGps = findViewById(R.id.tv_gps);
        TextView mDistance = findViewById(R.id.tv_distance);
        TextView mAddress = findViewById(R.id.tv_address);
//        mName.setText("急救AED设备（边防总队）");
//        mDistance.setText("距您 " + distance / 1000 + "km");
//        mAddress.setText("新城区乌兰恰特东街8号");

        //设置地理编码查询
        geocodeSearch = new GeocodeSearch(mContext);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {

                    mAddress.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
//                    mAddress.setText(address);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        //根据当前经纬度查询地址
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);

        mName.setText("急救AED设备(" + title + ")");
        mDistance.setText("距您约 " + distance + "km");
        mAddress.setText(address);

        mGps.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //startActivity(NaviActivity.class);
                //LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
                LatLng p2 = new LatLng(latitude, longitude);
                AmapNaviPage.getInstance().showRouteActivity(mContext, new AmapNaviParams(null, null, new Poi(getAddress, p2, ""), AmapNaviType.DRIVER), DistanceDialog.this);
                dismiss();
            }
        });
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
        Log.e("tag", "知乎评论 onShow");
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        Log.e("tag", "知乎评论 onDismiss");
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getScreenHeight(getContext()) * .7f);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviDirectionChanged(int i) {

    }

    @Override
    public void onDayAndNightModeChanged(int i) {

    }

    @Override
    public void onBroadcastModeChanged(int i) {

    }

    @Override
    public void onScaleAutoChanged(boolean b) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }
}
