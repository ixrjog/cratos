package com.baiyi.cratos.common.util;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Author baiyi
 * @Date 2024/3/1 09:58
 * @Version 1.0
 */
public class TimeUtil {

    private TimeUtil() {
    }

    /**
     *
     * @param d
     * @param format "yyyy-MM-dd"
     * @return
     * @throws ParseException
     */

    public static Date strToDate(String d, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
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

}
