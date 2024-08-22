package com.baiyi.cratos.scheduler.job;

import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.facade.EdsFacade;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/11 11:20
 * @Version 1.0
 */
@Slf4j
@Component
public class ImportAssetJob extends QuartzJobBean {

    public static final String ASSET_TYPE = "assetType";

    public static final String INSTANCE_ID = "instanceId";

    private static EdsFacade edsFacade;

    @Autowired
    public void setEdsFacade(EdsFacade edsFacade) {
        setFacade(edsFacade);
    }

    private static void setFacade(EdsFacade edsFacade) {
        ImportAssetJob.edsFacade = edsFacade;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取参数
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail()
                .getJobDataMap();
        // 任务
        String assetType = jobDataMap.getString(ASSET_TYPE);
        Integer instanceId = jobDataMap.getInt(INSTANCE_ID);
        // 任务开始时间
        EdsInstanceParam.ImportInstanceAsset importInstanceAsset = EdsInstanceParam.ImportInstanceAsset.builder()
                .assetType(assetType)
                .instanceId(instanceId)
                .build();
        try {
            edsFacade.importEdsInstanceAsset(importInstanceAsset);
            log.info("Import asset job: assetType={}, instanceId={}, trigger={}", assetType, instanceId,
                    jobExecutionContext.getTrigger());
        } catch (Exception e) {
            log.error("Import asset job error: assetType={}, instanceId={}, trigger={}, {}", assetType, instanceId,
                    jobExecutionContext.getTrigger(), e.getMessage());
            throw new JobExecutionException(e.getMessage());
        }
    }

}

