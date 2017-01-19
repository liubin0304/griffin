package com.forbidden.griffin.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lazy on 16/12/20.
 */

public class GinCalendar {
    private static final String TAG = "GinCalendar";
    private final static GinCalendar INSTANCE = new GinCalendar();
    /**
     * 完整的日期时间格式
     */
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 只有日期的格式
     */
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 只有时间的格式
     */
    public final static String TIME_FORMAT = "HH:mm:ss";
    /**
     * 带中文的日期格式(2000年01月01日)
     */
    public final static String DATE_FORMAT_WITH_CHINESE = "yyyy年MM月dd日";
    /**
     * 短时间格式(HH:mm)
     */
    public final static String SHORT_TIME_FORMAT = "HH:mm";

    /**
     * 私有构造
     */
    private GinCalendar() {
    }

    public static GinCalendar getInstance() {
        return INSTANCE;
    }

    /**
     * 把String类型的日期转换成Calendar对象
     *
     * @param string （日期时间:2000-00-00 00:00:00)
     * @return
     */
    public Calendar transformStringToCalendar(String string) {
        return transformStringToCalendar(string, DATE_TIME_FORMAT);
    }

    /**
     * 通过SimpleDataFormat格式把string转换成Calendar
     *
     * @param string 日期字符串
     * @param format 目标日期格式
     * @return
     */
    public Calendar transformStringToCalendar(String string, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        Calendar calendar = null;
        try {
            date = sdf.parse(string);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * 把日期字符串转换成TimeMillis
     *
     * @param string
     * @return
     */
    public long transformStringToMillis(String string) {
        return transformStringToCalendar(string).getTimeInMillis();
    }

    /**
     * 通过TimeMillis转换成秒钟
     *
     * @param millis TimeMillis
     * @return
     */
    public long getSecondWithTimeMillis(long millis) {
        return millis / 1000;
    }

    /**
     * 返回两个日期相差的秒
     *
     * @param calendar
     * @param targetCalendar
     * @return
     */
    public long getIntervalInSeconds(Calendar calendar, Calendar targetCalendar) {
        return (calendar.getTimeInMillis() - targetCalendar.getTimeInMillis()) / 1000;
    }

    /**
     * 格式化日期
     *
     * @param string 有效的日期字符
     * @param format 格式化的格式
     * @return
     */
    public String formatWithString(String string, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(transformStringToCalendar(string).getTime());
    }

    /**
     * 格式化日期
     *
     * @param src          源日期字符
     * @param srcFormat    源日期格式
     * @param targetFormat 目标日期格式
     * @return
     */
    public String formatWithString(String src, String srcFormat, String targetFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormat);
        try {
            Date date = sdf.parse(src);
            SimpleDateFormat targetSdf = new SimpleDateFormat(targetFormat);
            return targetSdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化成日期时间
     *
     * @param string 将要被格式化的日期字符串
     * @return 格式:(2000-00-00 00:00:00)
     */
    public String formatDateTimeWithString(String string) {
        return formatWithString(string, DATE_TIME_FORMAT);
    }

    /**
     * 格式化日期
     *
     * @param srcDate       源日期字符
     * @param srcDateFormat 源日期格式
     * @param targetFormat  目标格式
     * @return
     */
    public String formatDateTimeWithString(String srcDate, String srcDateFormat, String targetFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(srcDateFormat);
        try {
            Date date = sdf.parse(srcDate);
            SimpleDateFormat parseSdf = new SimpleDateFormat(targetFormat);
            return parseSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期
     *
     * @param string 将要被格式化的日期字符串
     * @return 格式:(2000-00-00)
     */
    public String formatDateWithString(String string) {
        return formatWithString(string, DATE_FORMAT);
    }

    /**
     * 格式化日期
     *
     * @param string 将要被格式化的日期字符串
     * @return 格式:(00:00:00)
     */
    public String formatTimeWithString(String string) {
        return formatWithString(string, TIME_FORMAT);
    }

    /**
     * 格式化日期
     *
     * @param string 将要被格式化的日期字符串
     * @return 格式:(2000年01月01日)
     */
    public String formatStringToChinese(String string) {
        return formatWithString(string, DATE_FORMAT_WITH_CHINESE);
    }

    /**
     * Data日期格式化成String
     *
     * @param date 将要被格式化的data
     * @return 格式:(2000-00-00 00:00:00)
     */
    public String formatDateTimeWithDate(Date date) {
        return formatStringWithDate(date, DATE_TIME_FORMAT);
    }

    /**
     * 时间戳格式的数据格式化成需要的格式
     *
     * @param string 将要被格式化的时间戳:01234567890
     * @return 格式:(2000-00-00 00:00:00)
     */
    public String formatDateTimeWithTime(String string) {
        return formatStringWithDate(timeToData(string), DATE_TIME_FORMAT);
    }

    /**
     * 格式化当前日期
     *
     * @param format
     * @return
     */
    public String formatNowDate(String format) {
        return formatStringWithDate(Calendar.getInstance().getTime(), format);
    }

    /**
     * 把中文日期（2000年01月01日)格式化成标准日期(2000-01-01)
     *
     * @param data 将要格式化的日期字符串
     * @return 格式:(2000-00-00);
     */
    public String formatChineseDataToData(String data) {
        data = data.replace("年", "-");
        data = data.replace("月", "-");
        data = data.replace("日", "");
        return data;
    }

    /**
     * 日期格式化成String
     *
     * @param date 将要格式化的日期字符串
     * @return 格式：（format格式）
     */
    public String formatStringWithDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 把String类型的时间转换成Calendar
     *
     * @param time 时间格式：00:00:00
     * @return
     */
    public Calendar transformStringTimeToCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        String[] split = time.split(":");
        if (split.length > 0) {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
        }
        if (split.length > 1) {
            calendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));
        }
        if (split.length > 2) {
            calendar.set(Calendar.SECOND, Integer.parseInt(split[2]));
        }
        return calendar;
    }

    /**
     * 把String类型的日期转换成Calendar
     *
     * @param date 日期格式:2000-00-00
     * @return
     */
    public Calendar transformStringDataToCanlendar(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] split = date.split("-");
        if (split.length > 0) {
            calendar.set(Calendar.YEAR, Integer.parseInt(split[0]));
        }
        if (split.length > 1) {
            calendar.set(Calendar.MONTH, Integer.parseInt(split[1]) - 1);
        }
        if (split.length > 2) {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(split[2]));
        }
        return calendar;
    }

    /**
     * 把时间戳:1234567890123,转换成Date对象
     *
     * @param time
     * @return
     */
    public Date timeToData(String time) {
        time = time.length() == 10 ? time + "000" : time;
        return new Date(Long.parseLong(time));
    }

    /**
     * 比较两个字符串日期的大小
     *
     * @param srcDate
     * @param tagDate
     * @return 如果小于0，则srcData 小于 tagData;如果等于0，则srcData = tagData;如果大于0，则srcData 大于 tagData
     */
    public int compare(String srcDate, String tagDate) {
        return srcDate.compareTo(tagDate);
    }

    /**
     * 返回现在的日期和时间
     *
     * @return 格式:2000-00-00 00:00:00
     */
    public String getNowDateTime() {
        Calendar calendar = Calendar.getInstance();
        return formatDateTimeWithDate(calendar.getTime());
    }

    /**
     * 返回当前的日期和时间，并使用format格式化
     *
     * @param format
     * @return
     */
    public String getNowDateTime(String format) {
        return formatWithString(getNowDateTime(), format);
    }

    /**
     * 返回当前的日期
     *
     * @return 格式：2000-00-00
     */
    public String getData() {
        return getNowDateTime().split(" ")[0];
    }

    /**
     * 返回当前时间
     *
     * @return 格式：00:00:00
     */
    public String getTime() {
        return getNowDateTime().split(" ")[1];
    }
}
