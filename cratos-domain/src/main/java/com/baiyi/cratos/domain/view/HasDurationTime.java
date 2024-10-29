package com.baiyi.cratos.domain.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/29 13:55
 * &#064;Version 1.0
 */
public interface HasDurationTime {

    Date getStartTime();

    Date getEndTime();

    void setDurationTime(String durationTime);

    default void initDurationTime() {
        if (this.getStartTime() == null) {
            return;
        }
        final Date endTime = this.getEndTime() == null ? new Date() : this.getEndTime();
        long diffTime = endTime.getTime() - this.getStartTime()
                .getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.setDurationTime(formatter.format(diffTime));
    }

}
