package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.facade.InspectionNotificationFacade;
import com.baiyi.cratos.facade.inspection.InspectionFactory;
import com.baiyi.cratos.facade.inspection.InspectionTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 上午10:05
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class InspectionNotificationFacadeImpl implements InspectionNotificationFacade {

    @Override
    public void doTask() {
        List<InspectionTask> tasks = InspectionFactory.getTasks();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(this::doTask);
    }

    private void doTask(InspectionTask inspectionTask){
        try {
            inspectionTask.inspectionTask();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
