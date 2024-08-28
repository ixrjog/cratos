package com.baiyi.cratos.scheduler.task;

import com.baiyi.cratos.configuration.condition.EnvCondition;
import com.baiyi.cratos.facade.inspection.InspectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 下午2:42
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Conditional(EnvCondition.class)
public class InspectionNotificationTask {

    @Scheduled(cron = "0 30 9 * * ?")
    @SchedulerLock(name = "INSPECTION_NOTIFICATION_TASK", lockAtMostFor = "5m", lockAtLeastFor = "5m")
    public void task() {
        InspectionFactory.doTask();
    }

}
