package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.CustomSchedulerException;
import com.baiyi.cratos.common.util.CronUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsScheduleParam;
import com.baiyi.cratos.domain.view.schedule.ScheduleVO;
import com.baiyi.cratos.facade.EdsScheduleFacade;
import com.baiyi.cratos.scheduler.SchedulerService;
import com.baiyi.cratos.scheduler.job.ImportAssetJob;
import com.baiyi.cratos.service.EdsInstanceService;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/3/8 18:20
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsScheduleFacadeImpl implements EdsScheduleFacade {

    private final SchedulerService schedulerService;
    private final EdsInstanceService instanceService;

    public static final String IMPORT_ASSET_JOB = "IMPORT_ASSET_JOB";

    private static final Map<String, Class<? extends QuartzJobBean>> jobClassMap = ImmutableMap.<String, Class<? extends QuartzJobBean>>builder()
            .put(IMPORT_ASSET_JOB, ImportAssetJob.class)
            .build();

    private String toScheduleGroup(int instanceId) {
        return StringFormatter.format("EDS:ID:{}", instanceId);
    }


    @Override
    public List<ScheduleVO.Job> queryJob(int instanceId) {
        Optional.ofNullable(instanceService.getById(instanceId))
                .orElseThrow(() -> new CustomSchedulerException("数据源实例不存在！"));
        try {
            return schedulerService.queryJob(toScheduleGroup(instanceId));
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            throw new CustomSchedulerException("查询数据源实例任务错误: {}", e.getMessage());
        }
    }

    @Override
    public void addJob(EdsScheduleParam.AddJob param) {
        if (!jobClassMap.containsKey(param.getJobType())) {
            throw new CustomSchedulerException("任务类型不存在！");
        }
        EdsInstance instance =  Optional.ofNullable(instanceService.getById(param.getInstanceId()))
                .orElseThrow(() -> new CustomSchedulerException("数据源实例不存在！"));
        if (schedulerService.checkJobExist(toScheduleGroup(instance.getId()), param.getAssetType())) {
            throw new CustomSchedulerException("任务已存在！");
        }
        addJob(instance, param);
    }

    private void addJob(EdsInstance instance, EdsScheduleParam.AddJob addJob) {
        Map<String, Object> map = ImmutableMap.<String, Object>builder()
                .put("assetType", addJob.getAssetType())
                .put("instanceId", instance.getId())
                .build();
        schedulerService.addJob(jobClassMap.get(addJob.getJobType()), toScheduleGroup(instance.getId()), addJob.getAssetType(), addJob.getJobTime(), addJob.getJobDescription(), map);
    }

    @Override
    public void pauseJob(EdsScheduleParam.UpdateJob updateJob) {
        schedulerService.pauseJob(updateJob.getGroup(), updateJob.getName());
    }

    @Override
    public void resumeJob(EdsScheduleParam.UpdateJob updateJob) {
        schedulerService.resumeJob(updateJob.getGroup(), updateJob.getName());
    }

    @Override
    public void deleteJob(EdsScheduleParam.DeleteJob deleteJob) {
        schedulerService.deleteJob(deleteJob.getGroup(), deleteJob.getName());
    }

    @Override
    public List<String> checkCron(EdsScheduleParam.CheckCron checkCron) {
        return CronUtil.recentTime(checkCron.getJobTime(), 5);
    }

}
