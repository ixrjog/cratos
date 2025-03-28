package com.baiyi.cratos.common.util;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/3/1 09:58
 * @Version 1.0
 */
@Slf4j
public final class TimeUtils {

    public static final String YEAR = "yyyy";
    public static final int THE_NUMBER_OF_SECONDS_IN_A_DAY = 86_400;
    public static final TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");

    private TimeUtils() {
    }

    /**
     * @param d
     * @param format "yyyy-MM-dd"
     * @return
     * @throws ParseException
     */

    public static Date strToDate(String d, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(UTC_TZ);
        return simpleDateFormat.parse(d);
    }

    public static Date toDate(String time, TimeZoneEnum timeZoneEnum) {
        if (StringUtils.isEmpty(time)) {
            return new Date();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(timeZoneEnum.getFormat());
        formatter.setTimeZone(TimeZone.getTimeZone(timeZoneEnum.name()));
        try {
            return formatter.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String parse(Date date, String fmt) {
        SimpleDateFormat formatter = new SimpleDateFormat(fmt);
        return formatter.format(date);
    }

    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 由于月份从0开始，所以需要+1
        int month = calendar.get(Calendar.MONTH) + 1;
        return month / 4 + 1;
    }

    public static int getDaysByQuarter(int isoYear, int quarter) {
        if (quarter == 4) {
            return 92;
        }
        if (quarter == 3) {
            return 92;
        }
        if (quarter == 2) {
            return 91;
        }
        if (quarter == 1) {
            Year year = Year.of(isoYear);
            return year.isLeap() ? 62 + 29 : 62 + 28;
        }
        return 0;
    }

    /**
     * 获取年的总天数
     *
     * @param isoYear
     * @return
     */
    public static int getDaysByYear(int isoYear) {
        Year year = Year.of(isoYear);
        return year.isLeap() ? 366 : 365;
    }

    public static String convertSecondsToHMS(long seconds) {
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long remainingSeconds = seconds - TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(remainingSeconds);
        remainingSeconds = remainingSeconds - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    private static void millisecondsSleep(long m) {
        try {
            TimeUnit.MILLISECONDS.sleep(m);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread()
                    .interrupt();
        }
    }

    public static int calculateDateDifferenceByDay(Date from, Date to){
        long subTime = to.getTime() - from.getTime();
        if (subTime < 0) {
            throw new RuntimeException("计算时间有误!");
        }
        long diff = subTime / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        return (int) diff;
    }

}
