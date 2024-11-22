package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.http.eds.EdsScheduleParam;
import com.baiyi.cratos.domain.view.schedule.ScheduleVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/8 18:20
 * @Version 1.0
 */
public interface EdsScheduleFacade {

    List<ScheduleVO.Job> queryJob(int instanceId);

    void addJob(EdsScheduleParam.AddJob param);

    void pauseJob(EdsScheduleParam.UpdateJob updateJob);

    void resumeJob(EdsScheduleParam.UpdateJob updateJob);

    void deleteJob(EdsScheduleParam.DeleteJob deleteJob);

    List<String> checkCron(EdsScheduleParam.CheckCron checkCron);

}
