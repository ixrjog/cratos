package com.baiyi.cratos.scheduler.task;

import com.baiyi.cratos.common.constants.SchedulerLockNameConstants;
import com.baiyi.cratos.configuration.condition.EnvCondition;
import com.baiyi.cratos.facade.identity.ResignationUsersProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/21 11:23
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Conditional(EnvCondition.class)
public class RevokingPermissionsForResignedUsersTask {

    private final ResignationUsersProcessor resignationUsersProcessor;

    @Scheduled(cron = "0 15 */2 * * ?")
    @SchedulerLock(name = SchedulerLockNameConstants.REVOKING_PERMISSIONS_FOR_RESIGNED_USERS_TASK, lockAtMostFor = "10m", lockAtLeastFor = "10m")
    public void task() {
        resignationUsersProcessor.doTask();
    }

}
