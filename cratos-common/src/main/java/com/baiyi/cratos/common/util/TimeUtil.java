package com.baiyi.cratos.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
