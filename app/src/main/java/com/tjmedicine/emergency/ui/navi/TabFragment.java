package com.tjmedicine.emergency.ui.navi;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.ParallelRoadListener;
import com.amap.api.navi.enums.AMapNaviParallelRoadStatus;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapRestrictionInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabFragment extends BaseFragment implements AMapNaviListener, AMapNaviViewListener, ParallelRoadListener, View.OnClickListener, INaviInfoCallback {


    private static final String TAG = "TabFragment";
    public View mView;
    @BindView(R.id.gps_program1)
    LinearLayout gps_program1;
    @BindView(R.id.gps_program2)
    LinearLayout gps_program2;
    @BindView(R.id.gps_program3)
    LinearLayout gps_program3;
    /**
     * 地图对象
     */
    @BindView(R.id.navi_view)
    MapView mRouteMapView;
    private Marker mStartMarker;
    private Marker mEndMarker;
    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;
    private AMap mAmap;
    /**
     * 路线计算成功标志位
     */
    private boolean calculateSuccess = false;
    private boolean chooseRouteSuccess = false;

    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();

    protected NaviLatLng mEndLatlng = new NaviLatLng(40.084894, 116.603039);
    protected NaviLatLng mStartLatlng = new NaviLatLng(39.825934, 116.342972);
    /**
     * 路线的权值，重合路线情况下，权值高的路线会覆盖权值低的路线
     **/
    private int zindex = 1;
    /**
     * 当前用户选中的路线，在下个页面进行导航
     */
    private int routeIndex;

    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mRouteMapView != null) {
            mRouteMapView.onSaveInstanceState(outState);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mView = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, mView);
        mRouteMapView.onCreate(savedInstanceState);
        mAmap = mRouteMapView.getMap();
        // 初始化Marker添加到地图
        mStartMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))));
        mEndMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))));

        mAMapNavi = AMapNavi.getInstance(EmergencyApplication.getContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addParallelRoadListener(this);
        mAMapNavi.setUseInnerVoice(true, true);

        //设置模拟导航的行车速度
        mAMapNavi.setEmulatorNaviSpeed(75);
        sList.add(mStartLatlng);
        eList.add(mEndLatlng);
        // 经纬度算路
        mAMapNavi.calculateDriveRoute(sList, eList, null, PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES_DEFAULT);

        LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
        AmapNaviPage.getInstance().showRouteActivity(requireActivity(), new AmapNaviParams(null, null, new Poi("故宫博物院", p2, ""), AmapNaviType.DRIVER), TabFragment.this);

        return mView;

    }
    @Override
    protected void initView() {
        gps_program1.setOnClickListener(this);
        gps_program2.setOnClickListener(this);
        gps_program3.setOnClickListener(this);
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_tab;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gps_program1:
                changeRoute();
                break;
            case R.id.gps_program2:
                changeRoute();
                break;
            case R.id.gps_program3:
                changeRoute();
                break;
        }
    }

//    /**
//     * 启动高德App进行路线规划导航 http://lbs.amap.com/api/amap-mobile/guide/android/route
//     *
//     * @param context
//     * @param sourceApplication 必填 第三方调用应用名称。如 "appName"
//     * @param sid
//     * @param sla
//     * @param slon
//     * @param sname
//     * @param did
//     * @param dlat
//     * @param dlon
//     * @param dName
//     * @param dev               起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
//     * @param t                 t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
//     *                          （骑行仅在V788以上版本支持）
//     */
//    public static void toGaoDeRoute(Context context, String sourceApplication
//            , String sid, String sla, String slon, String sname
//            , String did, String dlat, String dlon, String dName
//            , String dev, String t) {
//        StringBuffer stringBuffer = new StringBuffer("androidamap://route/plan?sourceApplication=").append(sourceApplication);
//        if (!TextUtils.isEmpty(sid)) {
//            stringBuffer.append("&sid=").append(sid);
//        }
//        if (!TextUtils.isEmpty(sla)) {
//            stringBuffer.append("&sla=").append(sla);
//        }
//        if (!TextUtils.isEmpty(sla)) {
//            stringBuffer.append("&sla=").append(sla);
//        }
//        if (!TextUtils.isEmpty(slon)) {
//            stringBuffer.append("&slon=").append(slon);
//        }
//        if (!TextUtils.isEmpty(sname)) {
//            stringBuffer.append("&sname=").append(sname);
//        }
//        if (!TextUtils.isEmpty(did)) {
//            stringBuffer.append("&did=").append(did);
//        }
//        stringBuffer.append("&dlat=").append(dlat);
//        stringBuffer.append("&dlon=").append(dlon);
//        stringBuffer.append("&dName=").append(dName);
//        stringBuffer.append("&dev=").append(dev);
//        stringBuffer.append("&t=").append(t);
//
//
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//
//        //将功能Scheme以URI的方式传入data
//        Uri uri = Uri.parse(stringBuffer.toString());
//        intent.setData(uri);
//        context.startActivity(intent);
//    }



    @Override
    public void onInitNaviFailure() {
        Toast.makeText(requireActivity(), "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess() {
        //初始化成功
    }

    @Override
    public void onStartNavi(int type) {
        //开始导航回调
    }

    @Override
    public void onTrafficStatusUpdate() {
        //
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        //当前位置回调
    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onGetNavigationText(int type, String text) {
        //播报类型和播报文字回调
    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {
        //结束模拟导航
    }

    @Override
    public void onArriveDestination() {
        //到达目的地
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {

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
    public void onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        //到达途径点
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        //GPS开关状态回调
    }

    @Override
    public void onNaviSetting() {
        //底部导航设置点击回调
    }

    @Override
    public void onNaviMapMode(int naviMode) {
        //导航态车头模式，0:车头朝上状态；1:正北朝上模式。
    }

    @Override
    public void onNaviCancel() {
        //finish();
    }


    @Override
    public void onNaviTurnClick() {
        //转弯view的点击回调
    }

    @Override
    public void onNextRoadClick() {
        //下一个道路View点击回调
    }


    @Override
    public void onScanViewButtonClick() {
        //全览按钮点击回调
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        //导航过程中的信息更新，请看NaviInfo的具体说明
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //已过时
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示放大图回调
    }

    @Override
    public void hideCross() {
        //隐藏放大图回调
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        //显示车道信息

    }

    @Override
    public void hideLaneInfo() {
        //隐藏车道信息
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //多路径算路成功回调
        HashMap<Integer, AMapNaviPath> naviPaths = AMapNavi.getInstance(getActivity()).getNaviPaths();

        Log.e(TAG, "onCalculateRouteSuccess: " + naviPaths);


    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("wlx", "导航页面加载成功");
        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }


    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

        int[] routeIds = aMapCalcRouteResult.getRouteid();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < routeIds.length; i++) {
            AMapNaviPath path = paths.get(routeIds[i]);
            if (path != null) {
                drawRoutes(routeIds[i], path);
            }
        }
    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        mAmap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAmap, path, requireActivity());
        routeOverLay.setTrafficLine(false);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }

    public void changeRoute() {
        if (!calculateSuccess) {
            Toast.makeText(getActivity(), "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 计算出来的路径只有一条
         */
        if (routeOverlays.size() == 1) {
            chooseRouteSuccess = true;
            //必须告诉AMapNavi 你最后选择的哪条路
            mAMapNavi.selectRouteId(routeOverlays.keyAt(0));
            Toast.makeText(getActivity(), "导航距离:" + (mAMapNavi.getNaviPath()).getAllLength() + "m" + "\n" + "导航时间:" + (mAMapNavi.getNaviPath()).getAllTime() + "s", Toast.LENGTH_SHORT).show();
            return;
        }

        if (routeIndex >= routeOverlays.size()) {
            routeIndex = 0;
        }
        int routeID = routeOverlays.keyAt(routeIndex);
        //突出选择的那条路
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.4f);
        }
        RouteOverLay routeOverlay = routeOverlays.get(routeID);
        if (routeOverlay != null) {
            routeOverlay.setTransparency(1);
            /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
            routeOverlay.setZindex(zindex++);
        }
        //必须告诉AMapNavi 你最后选择的哪条路
        mAMapNavi.selectRouteId(routeID);
        Toast.makeText(requireActivity(), "路线标签:" + mAMapNavi.getNaviPath().getLabels(), Toast.LENGTH_SHORT).show();
        routeIndex++;
        chooseRouteSuccess = true;

        /**选完路径后判断路线是否是限行路线**/
        AMapRestrictionInfo info = mAMapNavi.getNaviPath().getRestrictionInfo();
        if (!TextUtils.isEmpty(info.getRestrictionTitle())) {
            Toast.makeText(requireActivity(), info.getRestrictionTitle(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult result) {
        //路线计算失败
        Log.e("dm", "--------------------------------------------");
        Log.i("dm", "路线计算失败：错误码=" + result.getErrorCode() + ",Error Message= " + result.getErrorDescription());
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
        Toast.makeText(requireActivity(), "errorInfo：" + result.getErrorDetail() + ", Message：" + result.getErrorDescription(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void notifyParallelRoad(AMapNaviParallelRoadStatus aMapNaviParallelRoadStatus) {
        if (aMapNaviParallelRoadStatus.getmElevatedRoadStatusFlag() == 1) {
            Toast.makeText(getActivity(), "当前在高架上", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在高架上");
        } else if (aMapNaviParallelRoadStatus.getmElevatedRoadStatusFlag() == 2) {
            Toast.makeText(requireActivity(), "当前在高架下", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在高架下");
        }

        if (aMapNaviParallelRoadStatus.getmParallelRoadStatusFlag() == 1) {
            Toast.makeText(requireActivity(), "当前在主路", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在主路");
        } else if (aMapNaviParallelRoadStatus.getmParallelRoadStatusFlag() == 2) {
            Toast.makeText(requireActivity(), "当前在辅路", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在辅路");
        }
    }


    @Override
    public void notifyParallelRoad(int i) {
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        //更新交通设施信息
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        //更新巡航模式的统计信息
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        //更新巡航模式的拥堵信息
    }

    @Override
    public void onPlayRing(int i) {

    }


    @Override
    public void onLockMap(boolean isLock) {
        //锁地图状态发生变化时回调
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

    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {
    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }
    @Override
    public void onResume() {
        super.onResume();
        mRouteMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRouteMapView.onPause();

//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRouteMapView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        sList.clear();
        eList.clear();
        routeOverlays.clear();
        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        mAMapNavi.removeAMapNaviListener(this);
        mAMapNavi.destroy();
    }
}
