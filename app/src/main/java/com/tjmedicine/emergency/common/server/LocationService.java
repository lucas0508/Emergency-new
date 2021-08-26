package com.tjmedicine.emergency.common.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.MyLocationStyle;
import com.facebook.stetho.common.LogUtil;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.common.net.HttpProvider;

import java.util.HashMap;
import java.util.Map;


public class LocationService extends Service {

/*    Logger.e("HomePageFragment 开启定位-->111111111");
    Intent service = new Intent(requireActivity(), LocationService.class);
    requireActivity().startService(service);
        if (LocationService.instance != null) {
        LocationService.instance.mLocationClient.stopLocation();
        LocationService.instance.initMap(2000);

    }*/



    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    public static LocationService instance;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        initMap(2000);

    }
    public void initMap(int time){
        Logger.e("LocationService 开启定位-->"+time);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        mLocationOption.setInterval(time);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //启动定位
        mLocationClient.startLocation();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = amapLocation -> {

        Logger.e("LocationService ",amapLocation.toString());

        if (amapLocation == null||amapLocation.getCityCode()==null||"".equals(amapLocation.getCityCode())) {
            return;
        }
        if (amapLocation.getErrorCode() != 0) {
            return;
        }
//            EventBus.getDefault().postSticky(new MessageEvent(amapLocation, "success"));
//            if (UserInfo.getUserRole() != UserInfo.WORKER) {
//                if(!CommonConfig.getLocationStatusBanner()&& EmployerHomeFragment.instance!=null) {
//                    EmployerHomeFragment.instance.queryBanner(amapLocation.getCityCode());
//                    CommonConfig.setLocationStatusBanner(true);
//                }
//                return;
//            }else{
//                if(!CommonConfig.getLocationStatusBanner()&&WorkerHomeFragment.instance!=null) {
//                    WorkerHomeFragment.instance.queryBanner(amapLocation.getCityCode());
//                    CommonConfig.setLocationStatusBanner(true);
//                }
//            }
//            LogUtil.e("LocationService saveLoaction+++UserId",UserInfo.getUserId());
//
//            if (UserInfo.getUserId() == null||"".equals(UserInfo.getUserId())) {
//                return;
//            }
//            LogUtil.e("LocationService saveLoaction+++UserId",UserInfo.getIsAuth()+"");
//            if(!UserInfo.getIsAuth()){
//                return;
//            }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("longtiude", amapLocation.getLongitude());
        data.put("latiude", amapLocation.getLatitude());
        data.put("cityCode", amapLocation.getCityCode());
//            data.put("workerId", UserInfo.getUserId());
        HttpProvider.doPost("location/saveLocation", data, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {

                Logger.e("LocationService saveLoaction++++++++++++++++++++++",responseText.toString());
            }
        });

        Logger.e("LocationService saveLoaction",data.toString());
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 停止定位
     *
     * @author
     * @since 2.8.0
     */
    public void stopLocation() {
        // 停止定位
        mLocationClient.stopLocation();
    }

    public void destroyLocation() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

}
