package com.tjmedicine.emergency.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.mob.pushsdk.MobPushUtils;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.ActivityManager;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.dialog.DialogManage;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.HttpsUtils;
import com.tjmedicine.emergency.model.widget.CustomScrollViewPager;
import com.tjmedicine.emergency.ui.uart.database.DatabaseHelper;
import com.tjmedicine.emergency.utils.DevicePermissionsUtils;
import com.tjmedicine.emergency.utils.GsonUtils;
import com.tjmedicine.emergency.utils.PlayloadDelegate;
import com.tjmedicine.emergency.utils.ToastUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {


    @BindView(R.id.vp_content)
    CustomScrollViewPager mViewPager;
    MainViewPagerAdapter mAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    private long exitTime = 0;
    public static MainActivity mainActivity;

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mApp = new DialogManage(this);


        if (Build.VERSION.SDK_INT >= 21) {
            MobPush.setNotifyIcon(R.mipmap.ic_launcher);
        } else {
            MobPush.setNotifyIcon(R.mipmap.ic_launcher);
        }
        dealPushResponse(getIntent());

        //initPermission();
        initViewPager();


        if (Build.VERSION.SDK_INT >= 21) {
            MobPush.setNotifyIcon(R.mipmap.ic_launcher);
        } else {
            MobPush.setNotifyIcon(R.mipmap.ic_launcher);
        }
        dealPushResponse(getIntent());
// ???????????????
//        for (int i = 0; i < 10; i++) {
//            Map<String,Object> stringObjectMap = new HashMap<>();
//            stringObjectMap.put("id",System.currentTimeMillis()+i);
//            stringObjectMap.put("data","<data>Start<data/>"+i);
//            Logger.d(i +"????????????"+System.currentTimeMillis()+i);
//
//           // SQLiteHelper.getInstance().mUartDB.insertData(SQLiteHelper.getInstance().getDBInstance(),stringObjectMap);
//        }
//
//
//        List<Map<String, Object>> maps = SQLiteHelper.getInstance().mUartDB.queryUserInfo(SQLiteHelper.getInstance().getDBInstance(), null);
//
//        // Logger.d("???????????????????????????"+map1.size());
//        Logger.d("?????????????????????"+new Gson().toJson(maps));
        initRegistrationId();

    }

    /**
     * ????????????ID
     */
    private void initRegistrationId() {
        MobPush.getRegistrationId(new MobPushCallback<String>() {
            @Override
            public void onCallback(String rid) {
                System.out.println("RegistrationId:" + rid);
                HttpProvider.doGet(GlobalConstants.APP_PUSH_ID + "?rid=" + rid, null);
            }
        });
    }

    public void initPermission() {
        requestRunPermission(DevicePermissionsUtils.autoObtainNeedAllPermission(this), new BaseActivity.PermissionListener() {
            @Override
            public void onGranted() {
                Logger.d("????????????--cityCode->");
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                Logger.d("????????????");
//                initPermission();
                //userRefusePermissionsDialog();
            }
        });
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }

    private void initViewPager() {
        fragmentList.add(new HomePageFragment());
        fragmentList.add(new TeachingFragment());
        fragmentList.add(new MineFragment());
        mAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mAdapter);
    }

    @OnClick({R.id.rb_tab_menu_homepage, R.id.rb_tab_menu_material,
            R.id.rb_tab_menu_mypage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_tab_menu_homepage:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rb_tab_menu_material:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rb_tab_menu_mypage:
                mViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }


    /**
     * ????????????
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // ??????2?????????????????????
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showTextToas(getApplicationContext(), "????????????????????????");
                exitTime = System.currentTimeMillis();
            } else {
                //   finish();
                ActivityManager.getInstance().kill();
            }
        }
        return false;
    }

    /**
     * ????????????????????????
     *
     * @param millis1
     * @param millis2
     * @param timeZone
     * @return
     */
    public static boolean isSameDay(long millis1, long millis2, TimeZone timeZone) {
        long interval = millis1 - millis2;
        return interval < 86400000 && interval > -86400000 && millis2Days(millis1, timeZone) == millis2Days(millis2, timeZone);
    }

    private static long millis2Days(long millis, TimeZone timeZone) {
        return (((long) timeZone.getOffset(millis)) + millis) / 86400000;
    }

    /**
     * ?????????????????????????????????
     */
    public void userRefusePermissionsDialog() {
     /*   mApp.getConfirmDialog().show("????????????????????????????????????????????????????????????????????????",
                "??????", "??????", new ConfirmDialog.ConfirmCallback() {
                    @Override
                    public void onOk() {
                        //???????????????????????????????????????
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        ActivityManager.getInstance().kill();
                    }
                });*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //????????????setIntent?????????????????????????????????getIntent???????????????????????????
        setIntent(intent);
        dealPushResponse(intent);
    }

    private void dealPushResponse(Intent intent) {
        if (intent != null) {
            //????????????????????????????????????
            JSONArray jsonArray = MobPushUtils.parseMainPluginPushIntent(intent);
            System.out.println("parseMainPluginPushIntent:" + jsonArray);

            new PlayloadDelegate().playload(this, intent.getExtras());
        }
    }
}