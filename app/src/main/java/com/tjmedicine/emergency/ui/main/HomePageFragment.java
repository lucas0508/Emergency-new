package com.tjmedicine.emergency.ui.main;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.base.BaseFragment;
import com.tjmedicine.emergency.common.base.OnMultiClickListener;
import com.tjmedicine.emergency.common.bean.MapDataBeen;
import com.tjmedicine.emergency.common.cache.SharedPreferences.UserInfo;
import com.tjmedicine.emergency.common.dialog.DialogManage;
import com.tjmedicine.emergency.common.dialog.DistanceDialog;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.ui.device.DeviceActivity;
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
import com.tjmedicine.emergency.ui.transport.transportActivity;
import com.tjmedicine.emergency.ui.uart.UARTActivity;
import com.tjmedicine.emergency.utils.AnimUtil;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    @BindView(R.id.permission_rationale)
    View permissionRationale;
    private AMap aMap;
    public View mView;


    private Circle mcircle;
    private boolean isLoad = true;
    private LatLng latLngDistrict;
    private LatLng currLatlng;
    private AMapLocation myMapLocation;
    private int clusterRadius = 100;
    //??????AMapLocationClientOption??????
    private AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private ClusterOverlay mClusterOverlay;
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    private CustomMapStyleOptions mapStyleOptions;
    private View infoWindow;
    public DialogManage mApp;
    private IWXAPI api;
    //???????????????marker
    private Marker clickMaker;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34; // any 8-bit number

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //???activity??????onSaveInstanceState?????????mMapView.onSaveInstanceState (outState)??????????????????????????????
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
        //??????????????????????????????logo?????????????????????
        mapView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ((ViewGroup) mapView.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
        mapView.onCreate(savedInstanceState);
        mTitle.setText("121??????");
        init();
        initListener();

        return mView;
    }

    private void initListener() {
        ic_launcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("??????")
                        .setContentText("????????????????????????????????????????????????????????????5???????????????")
                        .setConfirmText("??????")
                        .setCancelText("??????")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                mApp.shortToast("?????????????????????");
//                                if (uartInterface != null) {
//                                    uartInterface.send("<MaxPressCal>");
//                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sDialog.setTitleText("????????????!")
                                                .setConfirmText("??????")
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
                    if (isBLEEnabled()) {
                        mApp.getOptionDialog().show("???????????????", new String[]{"1", "2"}, position -> {
                            /**
                             * type  1: ??????
                             *       2?????????
                             *       3?????????
                             */
                            Bundle bundle = new Bundle();
                            if (position == 0) {
                                bundle.putString("mode", "1");
                            } else if (position == 1) {
                                bundle.putString("mode", "2");
                            }
//                    else if (position == 2) {
//                        //??????????????????
//                        bundle.putString("mode", "3");
//                    }
                            startActivity(UARTActivity.class, bundle);
                        });
                    } else {
                        showBLEDialog();
                    }

                } else {
                    startActivity(LoginActivity.class);
                }

                /*if (mApp.isLogin()) {
                    mApp.getOptionDialog().show("???????????????", new String[]{"1", "2","3"}, position -> {
                        *//**
                 * type  1: ??????
                 *       2?????????
                 *       3?????????
                 *//*
                        Bundle bundle = new Bundle();
                        if (position == 0) {
                            bundle.putString("mode", "1");
                        } else if (position == 1) {
                            bundle.putString("mode", "2");
                        } else if (position == 2) {
                            //??????????????????
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
                AnimUtil.starAnim2(mEquipment);
                startActivity(transportActivity.class);
            }
        });
        mEquipment.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                AnimUtil.starAnim2(mEquipment);
                startActivity(DeviceActivity.class);
            }
        });
        iv_common_right.setOnClickListener(new OnMultiClickListener() {

            @Override
            public void onMultiClick(View v) {
                AnimUtil.starAnim2(mEquipment);
                startActivity(SystemInformationActivity.class);
            }
        });

    }

    protected boolean isBLEEnabled() {
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null && adapter.isEnabled();
    }

    protected void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    protected static final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void initView() {
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_home;
    }


    /**
     * ???????????????
     */
    private void init() {
        if (aMap != null) {
            aMap.clear();
        }
        aMap = mapView.getMap();
        //????????????????????????
        //setMapCustomStyleFile(requireActivity());
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMapLoadedListener(this);
        aMap.setOnCameraChangeListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// ????????????????????????????????????aMap.setMyLocationEnabled(true);// ?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false
        setupLocationStyle();
        aMap.setCustomMapStyle(mapStyleOptions);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        //mapDataPresenter.updateAddress(myMapLocation.getLatitude(),myMapLocation.getLongitude());
    }

    /**
     * ???????????????????????????
     */
    private void setupLocationStyle() {
        // ???????????????????????????
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // ???????????????????????????
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.icon_positioning_me));//R.drawable.gps_point)
        // ??????????????? myLocationStyle ????????????????????????
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // ??????????????????????????????????????????
        myLocationStyle.strokeWidth(0);
        // ???????????????????????????
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle);
        //???????????????????????????
        aMap.setInfoWindowAdapter(this);
        //??????????????????????????????
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
                // ?????????????????????
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
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have been granted the Manifest.permission.ACCESS_FINE_LOCATION permission. Now we may proceed with scanning.
                    mApp.getOptionDialog().show("???????????????", new String[]{"1", "2"}, position -> {
                        /**
                         * type  1: ??????
                         *       2?????????
                         *       3?????????
                         */
                        Bundle bundle = new Bundle();
                        if (position == 0) {
                            bundle.putString("mode", "1");
                        } else if (position == 1) {
                            bundle.putString("mode", "2");
                        }
//                    else if (position == 2) {
//                        //??????????????????
//                        bundle.putString("mode", "3");
//                    }
                        startActivity(UARTActivity.class, bundle);
                    });
                } else {
                    permissionRationale.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "????????????.", Toast.LENGTH_SHORT).show();
                }
                break;
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
        Log.e("?????????????????????", "onCameraChangeFinish:--??? " + cameraPosition.zoom);
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
        //???????????????
        mlocationClient = new AMapLocationClient(requireActivity());
        //?????????????????????
        mLocationOption = new AMapLocationClientOption();
        //????????????????????????
        mlocationClient.setLocationListener(this);

        mLocationOption.setOnceLocation(true);

        //??????????????????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //??????????????????
        mlocationClient.setLocationOption(mLocationOption);
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????????????????2000ms?????????????????????????????????stopLocation()???????????????????????????
        // ???????????????????????????????????????????????????onDestroy()??????
        // ?????????????????????????????????????????????????????????????????????stopLocation()???????????????????????????sdk???????????????
        mlocationClient.startLocation();//????????????
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
                mListener.onLocationChanged(aMapLocation);// ?????????????????????
                Log.e("AmapErr", aMapLocation.getLatitude() + "");
                Log.e("AmapErr", aMapLocation.getLongitude() + ":??????");
                UserInfo.setLang(String.valueOf(aMapLocation.getLongitude()));
                UserInfo.setLat(String.valueOf(aMapLocation.getLatitude()));
                // aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myMapLocation.getLatitude(), myMapLocation.getLongitude()), 17), 500, null);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myMapLocation.getLatitude(), myMapLocation.getLongitude()), 17));

            } else {
                String errText = "????????????," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
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
//     * ????????????
//     */
//    public void userOverlay(int mapRole) {
//        if (mClusterOverlay != null) {
//            mClusterOverlay.onDestroy();
//        }
//        //??????????????????
//        new Thread() {
//            public void run() {
//
//                List<ClusterItem> items = new ArrayList<ClusterItem>();
//
//                //??????10000??????
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
     * @param marker       ??????????????????
     * @param clusterItems
     */
    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        Cluster cluster = (Cluster) marker.getObject();
        clickMaker = marker;
        clusterItems.get(0).getPosition();
        Logger.d("?????????" + clusterItems.get(0).getPosition());

        DPoint dmPoint = new DPoint();
        dmPoint.setLatitude(myMapLocation.getLatitude());
        dmPoint.setLongitude(myMapLocation.getLongitude());
        DPoint dhPoint = new DPoint();
        dhPoint.setLatitude(marker.getPosition().latitude);
        dhPoint.setLongitude(marker.getPosition().longitude);
        CoordinateConverter.calculateLineDistance(dmPoint, dhPoint);
        // dmPoint      ?????????
        // dhPoint      ?????????
        float distance = CoordinateConverter.calculateLineDistance(dmPoint, dhPoint);
        BigDecimal bd = new BigDecimal(distance / 1000);
        //??????????????????n?????????????????????
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
            Logger.d("?????????--???" + mMapRole);
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
        //???activity??????onPause?????????mMapView.onPause ()????????????????????????
        mapView.onPause();
    }


    @Override
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null) {
            infoWindow = LayoutInflater.from(requireActivity()).inflate(R.layout.homepage_click_item, null);
        }
        render(marker, infoWindow);
        return null;
        // TODO: 2021-01-21 ??????????????????View  ????????????     ??????????????? return infoWindow;
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
//        // dmPoint      ?????????
//        // dhPoint      ?????????
//        float distance = CoordinateConverter.calculateLineDistance(dmPoint, dhPoint);
//        mName.setText("??????AED????????????????????????");
//        mDistance.setText("?????? " + distance / 1000 + "km");
//        mAddress.setText("???????????????????????????8???");
//
//        mGps.setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//                //startActivity(NaviActivity.class);
////                LatLng p2 = new LatLng(39.917337, 116.397056);//???????????????
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
     * ??????????????????
     * ????????????????????????????????????????????????
     */
    @Override
    public void onMapClick(LatLng latLng) {
        //????????????marker????????????????????????
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
                dp2px(EmergencyApplication.getContext(), clusterRadius),
                EmergencyApplication.getContext(), role);
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


