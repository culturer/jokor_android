package vip.jokor.im.util.base;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/6.
 */

public class TimeUtil {

    public static String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date current = new Date(System.currentTimeMillis());
        String str = format.format(current);
        return str;
    }

    public static String getDataToString(long time){
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        return format.format(date);
    }
    
    public static String getDateToString(long time){
        Date date = new Date(time);
        SimpleDateFormat formatDay = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        if (formatDay.format(date).equals(formatDay.format(new Date(getTime())))){
            return formatTime.format(date);
        }else {
            return formatDay.format(date);
        }
    }
    
    //获取时间戳
    public static long getTime(){
        long time= System.currentTimeMillis();
        return time;
    }

    public static Date String2Date(String str){
        if (str == null || str.equals("")){ return new Date(); }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(str, pos);
    }

    public static String date2Str(Date dateDate) {
        Date now = new Date();
        Calendar dateC = Calendar.getInstance();
        dateC.setTime(dateDate);
        Calendar nowC = Calendar.getInstance();
        nowC.setTime(now);

        if (dateC.get(Calendar.YEAR) == nowC.get(Calendar.YEAR)){
            if (dateC.get(Calendar.MONTH) == nowC.get(Calendar.MONTH)){
                if ( nowC.get(Calendar.DAY_OF_MONTH) - dateC.get(Calendar.DAY_OF_MONTH)<3 && nowC.get(Calendar.DAY_OF_MONTH) - dateC.get(Calendar.DAY_OF_MONTH)>0){
                    switch (nowC.get(Calendar.DAY_OF_MONTH) - dateC.get(Calendar.DAY_OF_MONTH)){
                        case 1: return "昨天";
                        case 2: return "前天";
                    }
                }
                if (dateC.get(Calendar.WEEK_OF_MONTH) == nowC.get(Calendar.WEEK_OF_MONTH)){
                    if (dateC.get(Calendar.DAY_OF_WEEK) ==nowC.get(Calendar.DAY_OF_WEEK)){
                        if (dateC.get(Calendar.HOUR) == nowC.get(Calendar.HOUR)){
                            if (nowC.get(Calendar.MINUTE) == dateC.get(Calendar.MINUTE))return "刚刚";
                            return nowC.get(Calendar.MINUTE) - dateC.get(Calendar.MINUTE)+"分钟前";
                        }else {
                            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                            return formatTime.format(dateDate);
                        }
                    }else {
                        switch (dateC.get(Calendar.DAY_OF_WEEK)){
                            case Calendar.MONDAY:return "周一";
                            case Calendar.TUESDAY:return "周二";
                            case Calendar.WEDNESDAY:return "周三";
                            case Calendar.THURSDAY:return "周四";
                            case Calendar.FRIDAY:return "周五";
                            case Calendar.SATURDAY:return "周六";
                            case Calendar.SUNDAY:return "周日";
                        }
                    }
                }else {
                    return dateC.get(Calendar.MONTH)+"月"+dateC.get(Calendar.DAY_OF_MONTH)+"号";
                }
            }else {
                if ( nowC.get(Calendar.DAY_OF_MONTH) - dateC.get(Calendar.DAY_OF_MONTH)<3 && nowC.get(Calendar.DAY_OF_MONTH) - dateC.get(Calendar.DAY_OF_MONTH)>0){
                    switch (nowC.get(Calendar.DAY_OF_MONTH) - dateC.get(Calendar.DAY_OF_MONTH)){
                        case 1: return "昨天";
                        case 2: return "前天";
                    }
                }
                return dateC.get(Calendar.MONTH)+"月"+dateC.get(Calendar.DAY_OF_MONTH)+"日";
            }
        }else {
            //返回年份
            return dateC.get(Calendar.YEAR)+"年"+dateC.get(Calendar.MONTH)+"月"+dateC.get(Calendar.DAY_OF_MONTH)+"日";
        }
        return "";
    }

}
