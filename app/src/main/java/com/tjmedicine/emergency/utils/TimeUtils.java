package com.tjmedicine.emergency.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/21 17:18
 */

public class TimeUtils {

    public static final String TIME_TYPE_YMD = "yyyy-MM-dd";
    public static final String TIME_TYPE_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_TYPE_YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String TIME_TYPE_YMD_HM_Str = "yyyy年MM月dd日 HH:mm";
    public static final String TIME_TYPE_HMS = "HH:mm:ss";
    /**
     * 获取本地时间
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 毫秒值转日期
     *
     * @param d
     * @param type
     * @return
     */
    public static String formatDateByTimeMillis(Long d, String type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        if (d != null) return simpleDateFormat.format(d);
        return "";
    }

    /**
     * 毫秒值转日期
     *
     * @param d
     * @param type
     * @return
     */
    public static String formatDateByTimeMillis(Date d, String type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        if (d != null) return simpleDateFormat.format(d);
        return "";
    }

    /**
     * 日期转毫秒值
     *
     * @param time
     * @param type
     * @return
     */
    public static Long formatTimeMillisByString(String time, String type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        long t = 0;
        try {
            Date date = simpleDateFormat.parse(time);
            t = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return t;
        }
        return t;
    }

    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getTimeCompareSize(String startTime, String endTime){
        int i=0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime()<date1.getTime()){
                i= 1;
            }else if (date2.getTime()==date1.getTime()){
                i= 2;
            }else if (date2.getTime()>date1.getTime()){
                //正常情况下的逻辑操作.
                i= 3;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  i;
    }
    /**
     * 毫秒值转 HH - MM - SS
     *
     * @param time
     * @return
     */
    public static String formatHMSByTimeMillis(Long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String format = formatter.format(time);
        return format;
    }


    /**
     * 日期 nice
     * @param type
     * @return
     */
    public static String formatDateByString(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String format = "";
        try {
            format = sdf.format(new SimpleDateFormat(TIME_TYPE_YMD_HMS).parse(type));
            //  System.out.println(sdf.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-10-15 00:00:00"/*你要转化的日期*/)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format;
    }
}
