package com.baiyi.cratos.facade.inspection;

import com.google.api.client.util.Lists;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/11 上午10:51
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class InspectionFactory {

    private final static ConcurrentHashMap<String, InspectionTask> CONTEXT = new ConcurrentHashMap<>();

    public static List<InspectionTask> getTasks() {
        List<InspectionTask> tasks = Lists.newArrayList();
        CONTEXT.forEach((k, v) -> tasks.add(v));
        return tasks;
    }

    public static void register(InspectionTask bean) {
        CONTEXT.put(bean.getClass()
                .getSimpleName(), bean);
    }

    public static void doTask() {
        if (CONTEXT.isEmpty()) {
            return;
        }
        InspectionFactory.getTasks()
                .forEach(InspectionFactory::doTask);
    }

    private static void doTask(InspectionTask inspectionTask) {
        try {
            inspectionTask.inspectionTask();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
