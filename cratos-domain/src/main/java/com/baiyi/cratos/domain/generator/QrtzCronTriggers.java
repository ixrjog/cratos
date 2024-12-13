package com.baiyi.cratos.domain.generator;

import javax.persistence.*;
import lombok.Data;

/**
 * 表名：qrtz_cron_triggers
*/
@Data
@Table(name = "qrtz_cron_triggers")
public class QrtzCronTriggers {
    @Id
    @Column(name = "SCHED_NAME")
    private String schedName;

    @Id
    @Column(name = "TRIGGER_NAME")
    private String triggerName;

    @Id
    @Column(name = "TRIGGER_GROUP")
    private String triggerGroup;

    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;

    @Column(name = "TIME_ZONE_ID")
    private String timeZoneId;
}