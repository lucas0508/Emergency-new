package com.tjmedicine.emergency.ui.teach.view;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.bean.StudyBean;
import com.tjmedicine.emergency.common.global.Constants;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.utils.DensityUtil;
import com.tjmedicine.emergency.utils.GsonUtils;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.GestureView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.player.VideoView;

public class MeStudyDetailActivity extends BaseActivity implements VideoView.OnStateChangeListener {

    private StudyBean.ListBean studyBean;
    @BindView(R.id.detail_player)
    VideoView detailPlayer;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.tv_time)
    TextView mTime;
    @BindView(R.id.tv_Caption)
    TextView mCaption;
    @BindView(R.id.ib_closevvv)
    ImageButton ibClose;
    @BindView(R.id.scroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.view_visibility)
    View view_visibility;

    private String playVideo = "2";//???????????? 1?????????   2:?????????
    private String videoTimeLength = "";
    private boolean isScroll = false;//  true????????????????????????

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_teaching_detail;
    }

    @Override
    protected void initView() {

        mTitle.setText("??????");
        studyBean = (StudyBean.ListBean) getIntent().getSerializableExtra("studyBean");

        mCaption.setText(studyBean.getTitle());
        mTime.setText(studyBean.getCreateAt());

//        Logger.d("tupian:" + studyBean.getContent());


        int type = studyBean.getType();
        if (!TextUtils.isEmpty(studyBean.getPlayUrl())) {
            detailPlayer.setVisibility(View.VISIBLE);
            playVideo(studyBean.getPlayUrl());
        }


        detailPlayer.setOnStateChangeListener(this);
        RichText.initCacheDir(this);
        RichText.debugMode = true;
        RichText.fromHtml(studyBean.getContent())
                .urlClick(new OnUrlClickListener() {
                    @Override
                    public boolean urlClicked(String url) {
                        if (url.startsWith("code://")) {
                            Toast.makeText(MeStudyDetailActivity.this, url.replaceFirst("code://", ""), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        return false;
                    }
                })
                .into(mContent);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("????????????-----??????????????????");
                pushdata();
            }
        });

        if (type == 2) {//??????

            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    boolean localVisibleRect = getLocalVisibleRect(MeStudyDetailActivity.this, view_visibility, scrollY);
                    if (localVisibleRect) {
                        Log.e("=====+++++++++++++++", "????????????");
                        isScroll = true;
                    } else {
                        Log.e("=====+++++++++++++++", "??????..???");
                    }
                }
            });
        }
    }

    /**
     * ????????????view?????????????????????
     */

    public static boolean getLocalVisibleRect(Activity activity, View view, int offsetY) {

        Point p = new Point();

        activity.getWindowManager().getDefaultDisplay().getSize(p);

        int screenWidth = p.x;

        int screenHeight = p.y;

        Rect rect = new Rect(0, 0, screenWidth, screenHeight);//??????????????????

        int[] location = new int[2];

        location[1] = location[1] + DensityUtil.dip2px(activity, offsetY);

        view.getLocationInWindow(location);

        view.setTag(location[1]);//??????y???????????????

        if (view.getLocalVisibleRect(rect)) {

            return true;

        } else {
            return false;
        }

    }

    private void loadCover(ImageView imageView, String url) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.upload_defaut);
        Glide.with(this.getApplicationContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(3000000)
                                .centerCrop()
                                .error(R.mipmap.upload_defaut)
                                .placeholder(R.mipmap.upload_defaut))
                .load(url)
                .into(imageView);
    }


    private void playVideo(String playUrl) {

        detailPlayer.setUrl(playUrl); //??????????????????

        StandardVideoController controller = new StandardVideoController(this);

        controller.addDefaultControlComponent(studyBean.getTitle(), false);
        //??????????????????????????????/????????????
        controller.setEnableOrientation(true);
        PrepareView prepareView = new PrepareView(this);//??????????????????
        prepareView.setClickStart();
        //????????????
        ImageView imageView = new ImageView(this);
        loadCover(imageView, playUrl);
        controller.addControlComponent(prepareView);

        controller.addControlComponent(new CompleteView(this));//????????????????????????

        controller.addControlComponent(new ErrorView(this));//????????????

        TitleView titleView = new TitleView(this);//?????????
        controller.addControlComponent(titleView);
        VodControlView vodControlView = new VodControlView(this);//???????????????
        //??????????????????????????????????????????
//                vodControlView.showBottomProgress(false);
        controller.addControlComponent(vodControlView);

        GestureView gestureControlView = new GestureView(this);//??????????????????
        controller.addControlComponent(gestureControlView);
        //?????????????????????????????????????????????????????????
        controller.setCanChangePosition(true);
        detailPlayer.setVideoController(controller); //???????????????
    }


    @Override
    protected void onPause() {
        super.onPause();
        detailPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailPlayer.resume();
    }
//
//    @Override
//    protected void onDestroy() {
////        pushdata();
//        super.onDestroy();
//        detailPlayer.release();
//    }


    @Override
    public void onBackPressed() {
        if (!detailPlayer.onBackPressed()) {
            super.onBackPressed();
        }
        pushdata();
    }

    private void pushdata() {
        if (studyBean.getType() == 2 && isScroll) {
            playVideo = "1";
        }
        long currentPosition = detailPlayer.getCurrentPosition();
        Map<String, Object> map = new HashMap<>();
        map.put("studyCourseId", studyBean.getId());
        map.put("completeStudy", playVideo);
        map.put("timeLength", currentPosition);
        HttpProvider.doPost(GlobalConstants.APP_USER_ME_STUDY_STUDYCOURSE,
                map, new HttpProvider.ResponseCallback() {
                    @Override
                    public void callback(String responseText) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(responseText);
                            String code = jsonObject.getString("code");
                            if (code.equals("200")) {
                                Logger.d("??????????????????????????????????????????????????????");
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
                break;
            case VideoView.STATE_PREPARING:
                break;
            case VideoView.STATE_PREPARED:
                String timeLength = studyBean.getTimeLength();
                if (!TextUtils.isEmpty(timeLength)) {
                    detailPlayer.skipPositionWhenPlay(Integer.parseInt(timeLength));
                }
                break;
            case VideoView.STATE_PLAYING:

                break;
            case VideoView.STATE_PAUSED:
                break;
            case VideoView.STATE_BUFFERING:
                break;
            case VideoView.STATE_BUFFERED:
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                Logger.d(detailPlayer.getDuration());
                Logger.d("????????????");
                playVideo = "1";
                break;
            case VideoView.STATE_ERROR:
                break;
        }
    }
}
