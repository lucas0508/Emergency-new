package com.tjmedicine.emergency.common.global;


public class GlobalConstants {


    public static final String BASE_SERVER_ = "https://www.mengyuanyiliao.com";


  //public static final String BASE_SERVER_ = "http://192.168.3.211:8081";

//  public static final String BASE_SERVER_ = "http://192.168.3.202:8081";

    /**
     * 七牛云
     */
    public static final String BASE_QI_NIU_URL = "http://qiniu.app.mengyuanyiliao.com";


    public static final String BASE_SERVER_URL = BASE_SERVER_ + "/mobile";

    /**
     * 登录
     */
    public static final String APP_LOGIN_INTERFACE = BASE_SERVER_URL + "/user/login";

    /**
     * 验证码
     */
    public static final String APP_LOAD_CAPTCHA = BASE_SERVER_URL + "/user/sendSms";


    /**
     * 获取个人信息
     */
    public static final String APP_GETUSERINFO = BASE_SERVER_URL + "/user/findUserInfo";


    /**
     * 修改个人信息
     */
    public static final String APP_MODIFY_USER_INFORMATION = BASE_SERVER_URL + "/user/editUserInfo";


    /**
     * 退出登录
     */
    public static final String APP_LOGOUT = BASE_SERVER_URL + "/user/logout";


    /**
     * 用户协议
     */
    public static final String AGREEMENT_URL = BASE_SERVER_ + "/static/privacy.html";


    /**
     * 志愿者需知
     */
    public static final String PRIVACYPOLICY_URL = BASE_SERVER_ + "/static/privacy.html";

    /**
     * 问题反馈
     */
    public static final String USER_ADUSERFEEDBACK = BASE_SERVER_URL + "/user/feedback";


    /**
     * 版本更新
     */
    public static final String APP_VERSION_UPDATE = BASE_SERVER_URL + "/user/app/version";


    //----------------------------------------模拟人----------------------------------------------------------------

    /**
     * 模拟人数据上传
     * type  1测试，2练习
     */
    public static final String APP_UART_TRAIN_ADD = BASE_SERVER_URL + "/train/add";


    /**
     * 模拟人数据列表
     */
    public static final String APP_UART_TRAIN_LIST = BASE_SERVER_URL + "/train/list";


    /**
     * 模拟人数据详情
     */
    public static final String APP_UART_TRAIN_INFO = BASE_SERVER_URL + "/train/info";


    //申请志愿者
    public static final String APP_USER_APPLY_VOLUNTEER = BASE_SERVER_URL + "/user/applyVolunteer";

    //查询志愿者
    public static final String APP_USER_QUERY_VOLUNTEER = BASE_SERVER_URL + "/user/findVolunteer";


    /**
     * 新增紧急联系人
     */
    public static final String APP_USER_ADD_CONTACT = BASE_SERVER_URL + "/user/addContact";


    /**
     * 查找紧急联系人
     */
    public static final String APP_USER_FIND_CONTACT = BASE_SERVER_URL + "/user/findContact";

    /**
     * 删除联系人
     */
    public static final String APP_USER_DELEATE_CONTACT = BASE_SERVER_URL + "/user/editContact";


    /**
     * 用户实名认证
     */
    public static final String APP_USER_AUTH = BASE_SERVER_URL + "/user/realAuth";

    /**
     * 查询实名认证信息
     */
    public static final String APP_USER_FINDREALAUTH = BASE_SERVER_URL + "/user/findRealAuth";


    /**
     * 添加健康档案
     */
    public static final String APP_USER_ADDHEALTH_RECORDS = BASE_SERVER_URL + "/user/addHealthRecords";


    /**
     * 删除健康档案
     */
    public static final String APP_USER_EDITHEALTH_RECORDS = BASE_SERVER_URL + "/user/editHealthRecords";


    /**
     * 查询健康档案列表
     */
    public static final String APP_USER_FINDHEALTH_RECORD_LIST = BASE_SERVER_URL + "/user/findHealthRecordsList";

    /**
     * 查询健康档案详情
     */
    public static final String APP_USER_FINDHEALTH_RECORDS_DETAIL = BASE_SERVER_URL + "/user/findHealthRecordsInfo";


    /**
     * 查询健康档案标签
     */
    public static final String APP_USER_FINDHEALTH_RECORDS_TAG = BASE_SERVER_URL + "/user/findHealthRecordsTag";

    /**
     * 我的报名
     */
    public static final String APP_USER_FIND_SIGNUP = BASE_SERVER_URL + "/course/findSignUp";


    /**
     * 活动报名
     */
    public static final String APP_USER_SIGNUP = BASE_SERVER_URL + "/course/signUp?activityId=";


    /**
     * 查询轮播图
     */
    public static final String APP_USER_BANNER = BASE_SERVER_URL + "/course/findBanner";

    /**
     * 查询课程列表
     */
    public static final String APP_USER_FIND_COURSE = BASE_SERVER_URL + "/course/findList";


    /**
     * 查询课程详情
     */
    public static final String APP_USER_FINDINFO = BASE_SERVER_URL + "/course/findInfo";


    /**
     * 首页地图数据
     */
    public static final String APP_USER_MAP_DATA = BASE_SERVER_URL + "/user/map";


    /**
     * 更新位置信息
     */
    public static final String APP_USER_UPDATE_ADDRESS = BASE_SERVER_URL + "/user/updateAddress";


    /**
     * 埋点
     * b_ble_start
     * b_ble_end
     */
    public static final String APP_BURIED_POINT = BASE_SERVER_URL + "/train/count?type=";


    /**
     * 发送服务器推送id
     */
    public static final String APP_PUSH_ID = BASE_SERVER_URL + "/user/addRid";


}






