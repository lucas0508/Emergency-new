package com.tjmedicine.emergency.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.BaseFragment;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.MapDataBeen;
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.common.dialog.DialogManage;
import com.tjmedicine.emergency.common.dialog.DistanceDialog;
import com.tjmedicine.emergency.common.dialog.TaskSucDialog;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.common.server.LocationService;
import com.tjmedicine.emergency.ui.login.view.activity.LoginActivity;
import com.tjmedicine.emergency.ui.map.Cluster;
import com.tjmedicine.emergency.ui.map.ClusterClickListener;
import com.tjmedicine.emergency.ui.map.ClusterItem;
import com.tjmedicine.emergency.ui.map.ClusterOverlay;
import com.tjmedicine.emergency.ui.map.ClusterRender;
import com.tjmedicine.emergency.ui.map.RegionItem;
import com.tjmedicine.emergency.ui.map.presenter.IMapDataView;
import com.tjmedicine.emergency.ui.map.presenter.MapDataPresenter;
import com.tjmedicine.emergency.ui.mseeage.systemInformation.SystemInformationActivity;
import com.tjmedicine.emergency.ui.navi.NaviActivity;
import com.tjmedicine.emergency.ui.navi.TabFragment;
import com.tjmedicine.emergency.ui.other.WebActivity;
import com.tjmedicine.emergency.ui.uart.SettingActivity;
import com.tjmedicine.emergency.ui.uart.UARTActivity;
import com.tjmedicine.emergency.ui.uart.data.presenter.IUARTControlView;
import com.tjmedicine.emergency.ui.uart.data.presenter.PDScoreData;
import com.tjmedicine.emergency.ui.uart.data.presenter.UARTControlPresenter;
import com.tjmedicine.emergency.utils.AnimUtil;
import com.tjmedicine.emergency.utils.GifLoadOneTimeGif;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.lxj.xpopup.util.XPopupUtils.dp2px;
import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_FLAG;
import static com.tjmedicine.emergency.common.global.Constants.WEB_KEY_URL;

public class HomePageFragment extends BaseFragment implements AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, LocationSource, AMapLocationListener, ClusterRender, ClusterClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener, IMapDataView, INaviInfoCallback {
    //    private UARTControlPresenter uartControlPresenter = new UARTControlPresenter(this);
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private MapDataPresenter mapDataPresenter = new MapDataPresenter(this);

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.iv_dummy)
    ImageView mDummy;
    @BindView(R.id.iv_doctors)
    ImageView mDoctors;
    @BindView(R.id.iv_volunteers)
    ImageView mVolunteers;
    @BindView(R.id.iv_aids)
    ImageView mAids;
    @BindView(R.id.iv_common_right)
    ImageView iv_common_right;
    @BindView(R.id.iv_location)
    ImageView mLocation;
    @BindView(R.id.ic_launcher)
    ImageView ic_launcher;
    @BindView(R.id.iv_equipment)
    ImageView mEquipment;
    @BindView(R.id.iv_car)
    ImageView mCar;
    private AMap aMap;
    public View mView;


    private Circle mcircle;
    private boolean isLoad = true;
    private LatLng latLngDistrict;
    private LatLng currLatlng;
    private AMapLocation myMapLocation;
    private int clusterRadius = 100;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private ClusterOverlay mClusterOverlay;
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    private CustomMapStyleOptions mapStyleOptions;
    private View infoWindow;
    public DialogManage mApp;

    //当前点击的marker
    private Marker clickMaker;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mView);
        mApp = ((BaseActivity) requireActivity()).mApp;
        //这个功能是去掉地图的logo和放大缩小图标
        mapView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ((ViewGroup) mapView.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
        mapView.onCreate(savedInstanceState);
        mTitle.setText("121急救");
        init();
        initListener();

        return mView;

    }

    private void initListener() {
        ic_launcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("使用最大校准之前，首先把模拟人放平，按压5秒之后松开")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                mApp.shortToast("校准中······");
//                                if (uartInterface != null) {
//                                    uartInterface.send("<MaxPressCal>");
//                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sDialog.setTitleText("校准完成!")
                                                .setConfirmText("确定")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                }, 7000);

                            }
                        }).show();
            }
        });
        mDummy.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (mApp.isLogin()) {
                    mApp.getOptionDialog().show("请选择模式", new String[]{"1", "2"}, position -> {
                        /**
                         * type  1: 练习
                         *       2：测试
                         *       3：考试
                         */
                        Bundle bundle = new Bundle();
                        if (position == 0) {
                            bundle.putString("mode", "1");
                        } else if (position == 1) {
                            bundle.putString("mode", "2");
                        }
//                    else if (position == 2) {
//                        //添加实名认证
//                        bundle.putString("mode", "3");
//                    }
                        startActivity(UARTActivity.class, bundle);
                    });
                } else {
                    startActivity(LoginActivity.class);
                }

                /*if (mApp.isLogin()) {
                    mApp.getOptionDialog().show("请选择模式", new String[]{"1", "2","3"}, position -> {
                        *//**
                 * type  1: 练习
                 *       2：测试
                 *       3：考试
                 *//*
                        Bundle bundle = new Bundle();
                        if (position == 0) {
                            bundle.putString("mode", "1");
                        } else if (position == 1) {
                            bundle.putString("mode", "2");
                        } else if (position == 2) {
                            //添加实名认证
                            bundle.putString("mode", "3");
                        }
                        startActivity(UARTActivity.class, bundle);
                    });
                }
                else {
                    startActivity(LoginActivity.class);
                }*/


            }
        });
        mLocation.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mlocationClient.startLocation();
            }
        });
        mDoctors.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                AnimUtil.starAnim2(mDoctors);
//                userOverlay(Constants.MAPROLE_DOCTOR);
                mapDataPresenter.queryMapData(Constants.MAPROLE_DOCTOR);
            }
        });
        mVolunteers.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
//                userOverlay(Constants.MAPROLE_VOLUNTEER);
                AnimUtil.starAnim2(mVolunteers);
                mapDataPresenter.queryMapData(Constants.MAPROLE_VOLUNTEER);
            }
        });
        mAids.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
//                userOverlay(Constants.MAPROLE_AED);
                AnimUtil.starAnim2(mAids);
                mapDataPresenter.queryMapData(Constants.MAPROLE_AED);
            }
        });
        mCar.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                String url = "http://transport.mengyuanyiliao.com/h5/#/";
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra(WEB_KEY_URL, url);
                intent.putExtra(WEB_KEY_FLAG, 1);
                startActivity(intent);
            }
        });
        mEquipment.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                AnimUtil.starAnim2(mEquipment);
                mApp.shortToast("攻城狮正在开发中...");
                // startActivity(SystemInformationActivity.class);
            }
        });
        iv_common_right.setOnClickListener(new OnMultiClickListener() {

            @Override
            public void onMultiClick(View v) {
                startActivity(SystemInformationActivity.class);
            }
        });

    }

    @Override
    protected void initView() {
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_home;
    }


    /**
     * 地图初始化
     */
    private void init() {
        if (aMap != null) {
            aMap.clear();
        }
        aMap = mapView.getMap();
        //显示地图简单样式
        //setMapCustomStyleFile(requireActivity());
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMapLoadedListener(this);
        aMap.setOnCameraChangeListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
        aMap.setCustomMapStyle(mapStyleOptions);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        //mapDataPresenter.updateAddress(myMapLocation.getLatitude(),myMapLocation.getLongitude());
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.icon_positioning_me));//R.drawable.gps_point)
        // 将自定义的 myLocationStyle 对象添加到地图上
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle);
        //设置自定义信息窗口
        aMap.setInfoWindowAdapter(this);
        //设置地图点击事件监听
        aMap.setOnMapClickListener(this);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    private void setMapCustomStyleFile(Context context) {
        String styleName = "style.data";
        mapStyleOptions = new CustomMapStyleOptions();
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(styleName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            if (mapStyleOptions != null) {
                // 设置自定义样式
                mapStyleOptions.setEnable(true);
                mapStyleOptions.setStyleData(b);
//                mapStyleOptions.setStyleExtraPath("/mnt/sdcard/amap/style_extra.data");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onMapLoaded() {
//        doSearchQuery();.

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Log.e("比例尺缩放级别", "onCameraChangeFinish:--》 " + cameraPosition.zoom);
        //addMarkerInScreenCenter();
        if (mcircle == null) {
            mcircle = aMap.addCircle(new CircleOptions().center(cameraPosition.target).radius(150).strokeColor(1));
        }
        if (isLoad || !mcircle.contains(cameraPosition.target)) {
            mcircle.setCenter(cameraPosition.target);
            LatLng latLng = cameraPosition.target;
            latLngDistrict = latLng;
            currLatlng = latLng;
            if (latLng != null) {
                doSearchQuery();
            }
            isLoad = false;
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
//        if (mlocationClient == null) {
        //初始化定位
        mlocationClient = new AMapLocationClient(requireActivity());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位回调监听
        mlocationClient.setLocationListener(this);

        mLocationOption.setOnceLocation(true);

        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation();//启动定位
//        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                myMapLocation = aMapLocation;
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                Log.e("AmapErr", aMapLocation.getLatitude() + "");
                Log.e("AmapErr", aMapLocation.getLongitude() + ":经度");
                UserInfo.setLang(String.valueOf(aMapLocation.getLongitude()));
                UserInfo.setLat(String.valueOf(aMapLocation.getLatitude()));
                // aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myMapLocation.getLatitude(), myMapLocation.getLongitude()), 17), 500, null);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myMapLocation.getLatitude(), myMapLocation.getLongitude()), 17));

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


    /**
     * @param
     */
    private void doSearchQuery() {
        //LatLng latLng
        //mNearPresenter.queryNearWorkerList(skillView.getText().toString(), latLng.longitude, latLng.latitude, null, searchNature);
        //userOverlay(Constants.MAPROLE_DOCTOR);
        mapDataPresenter.queryMapData(Constants.MAPROLE_DOCTOR);
    }

//    /**
//     * 位置撒点
//     */
//    public void userOverlay(int mapRole) {
//        if (mClusterOverlay != null) {
//            mClusterOverlay.onDestroy();
//        }
//        //添加测试数据
//        new Thread() {
//            public void run() {
//
//                List<ClusterItem> items = new ArrayList<ClusterItem>();
//
//                //随机10000个点
//                for (int i = 0; i < 1000; i++) {
//                    //myMapLocation.getLatitude()
//                    //myMapLocation.getLongitude()
//                    double lat = Math.random() + myMapLocation.getLatitude();
//                    double lon = Math.random() + myMapLocation.getLongitude();
//                    LatLng latLng = new LatLng(lat, lon, false);
//                    RegionItem regionItem = new RegionItem(latLng,
//                            "test" + i);
//                    items.add(regionItem);
//
//                }
//                mClusterOverlay = new ClusterOverlay(aMap, items,
//                        dp2px(getActivity().getApplicationContext(), clusterRadius),
//                        getActivity().getApplicationContext(), mapRole);
//                mClusterOverlay.setClusterRenderer(HomePageFragment.this);
//                mClusterOverlay.setOnClusterClickListener(HomePageFragment.this);
//
//            }
//        }.start();
//
//    }

    /**
     * @param marker       点击的聚合点
     * @param clusterItems
     */
    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        Cluster cluster = (Cluster) marker.getObject();
        clickMaker = marker;
        clusterItems.get(0).getPosition();
        Logger.d("执行了" + clusterItems.get(0).getPosition());

        DPoint dmPoint = new DPoint();
        dmPoint.setLatitude(myMapLocation.getLatitude());
        dmPoint.setLongitude(myMapLocation.getLongitude());
        DPoint dhPoint = new DPoint();
        dhPoint.setLatitude(marker.getPosition().latitude);
        dhPoint.setLongitude(marker.getPosition().longitude);
        CoordinateConverter.calculateLineDistance(dmPoint, dhPoint);
        // dmPoint      我的点
        // dhPoint      目标点
        float distance = CoordinateConverter.calculateLineDistance(dmPoint, dhPoint);
        BigDecimal bd = new BigDecimal(distance / 1000);
        //保留小数点后n位，并四舍五入
        bd = (bd).setScale(1, BigDecimal.ROUND_HALF_UP);
        if (cluster.getClusterCount() == 1 && clusterItems.get(0).getMapRole() == Constants.MAPROLE_AED) {
//            mApp.getUserInfoDialog().show(clusterItems.get(0).getUserId(), null);
//            marker.showInfoWindow();
            String title = clusterItems.get(0).getTitleName();
            String address = clusterItems.get(0).getAddress();
            DistanceDialog distanceDialog = new DistanceDialog(requireActivity(), bd.floatValue(), title, address,
                    marker.getPosition().latitude, marker.getPosition().longitude, myMapLocation.getAddress());
            new XPopup.Builder(requireActivity()).asCustom(distanceDialog).show();
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (ClusterItem clusterItem : clusterItems) {
            builder.include(clusterItem.getPosition());
        }
        LatLngBounds latLngBounds = builder.build();
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
    }

    @Override
    public Drawable getDrawAble(int clusterNum, int mMapRole) {
        int radius = dp2px(getActivity().getApplicationContext(), 80);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                if (Constants.MAPROLE_DOCTOR == mMapRole) {
                    bitmapDrawable = EmergencyApplication.getContext().getResources().getDrawable(
                            R.mipmap.icon_positioning_doctor);
                } else if (Constants.MAPROLE_VOLUNTEER == mMapRole) {
                    bitmapDrawable = getActivity().getApplication().getResources().getDrawable(
                            R.mipmap.icon_positioning_volunteers);
                } else if (Constants.MAPROLE_AED == mMapRole) {
                    bitmapDrawable = getActivity().getApplication().getResources().getDrawable(
                            R.mipmap.icon_positioning_aid);
                }
                mBackDrawAbles.put(1, bitmapDrawable);
            }
            Logger.d("角色：--》" + mMapRole);
            return bitmapDrawable;
        } else if (clusterNum < 5) {
            Drawable bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(159, 210, 154, 6)));
                mBackDrawAbles.put(2, bitmapDrawable);
            }

            return bitmapDrawable;
        } else if (clusterNum < 10) {
            Drawable bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(199, 217, 114, 0)));
                mBackDrawAbles.put(3, bitmapDrawable);
            }

            return bitmapDrawable;
        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(4);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(235, 215, 66, 2)));
                mBackDrawAbles.put(4, bitmapDrawable);
            }

            return bitmapDrawable;
        }
    }

    private Bitmap drawCircle(int radius, int color) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    @Override
    public void onHiddenChanged(boolean hidd) {
        if (hidd) {

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }


    @Override
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null) {
            infoWindow = LayoutInflater.from(requireActivity()).inflate(R.layout.homepage_click_item, null);
        }
        render(marker, infoWindow);
        return infoWindow;
        // TODO: 2021-01-21 地图点击显示View  暂时注释
    }

    private void render(Marker marker, View view) {
//        TextView mName = view.findViewById(R.id.tv_name);
//        TextView mGps = view.findViewById(R.id.tv_gps);
//        TextView mDistance = view.findViewById(R.id.tv_distance);
//
//        TextView mAddress = view.findViewById(R.id.tv_address);
//        DPoint dmPoint = new DPoint();
//        dmPoint.setLatitude(myMapLocation.getLatitude());
//        dmPoint.setLongitude(myMapLocation.getLongitude());
//        DPoint dhPoint = new DPoint();
//        dhPoint.setLatitude(marker.getPosition().latitude);
//        dhPoint.setLongitude(marker.getPosition().longitude);
//        CoordinateConverter.calculateLineDistance(dmPoint, dhPoint);
//        // dmPoint      我的点
//        // dhPoint      目标点
//        float distance = CoordinateConverter.calculateLineDistance(dmPoint, dhPoint);
//        mName.setText("急救AED设备（边防总队）");
//        mDistance.setText("距您 " + distance / 1000 + "km");
//        mAddress.setText("新城区乌兰恰特东街8号");
//
//        mGps.setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//                //startActivity(NaviActivity.class);
////                LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
//                LatLng p2 = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
//                AmapNaviPage.getInstance().showRouteActivity(requireActivity(), new AmapNaviParams(null, null, new Poi(myMapLocation.getAddress(), p2, ""), AmapNaviType.DRIVER), HomePageFragment.this);
//            }
//        });
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 地图点击事件
     * 点击地图区域让当前展示的窗体隐藏
     */
    @Override
    public void onMapClick(LatLng latLng) {
        //判断当前marker信息窗口是否显示
        if (clickMaker != null && clickMaker.isInfoWindowShown()) {
            clickMaker.hideInfoWindow();
        }
    }


    @Override
    public void queryMapDataSuccess(List<MapDataBeen> mapDataBeens) {
        if (mClusterOverlay != null) {
            mClusterOverlay.onDestroy();
        }
        List<ClusterItem> items = new ArrayList<ClusterItem>();
        int role = 0;
        for (MapDataBeen mapDataBeen : mapDataBeens) {
            LatLng latLng = new LatLng(mapDataBeen.getLat(), mapDataBeen.getLng(), false);
            RegionItem regionItem = new RegionItem(latLng, mapDataBeen.getAddress(), mapDataBeen.getType(), mapDataBeen.getAddress());
            role = mapDataBeen.getType();
            items.add(regionItem);
        }
        mClusterOverlay = new ClusterOverlay(aMap, items,
                dp2px(getActivity().getApplicationContext(), clusterRadius),
                getActivity().getApplicationContext(), role);
        mClusterOverlay.setClusterRenderer(HomePageFragment.this);
        mClusterOverlay.setOnClusterClickListener(HomePageFragment.this);
    }

    @Override
    public void queryMapDataFail(String info) {

    }

    @Override
    public void updateAddressSuccess() {

    }

    @Override
    public void updateAddressFail(String info) {

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


