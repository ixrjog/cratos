package com.baiyi.cratos.scheduler.task;

import com.baiyi.cratos.common.constants.SchedulerLockNameConstants;
import com.baiyi.cratos.configuration.condition.EnvCondition;
import com.baiyi.cratos.facade.application.OcApplicationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/29 10:41
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Conditional(EnvCondition.class)
public class ImportOcApplicationTask {

    private final OcApplicationFacade ocApplicationFacade;

    @Scheduled(cron = "0 */20 * * * ?")
    @SchedulerLock(name = SchedulerLockNameConstants.IMPORT_OC_APPLICATION_TASK, lockAtMostFor = "5m", lockAtLeastFor = "5m")
    public void task() {
        ocApplicationFacade.importAllApplication();
    }

}
