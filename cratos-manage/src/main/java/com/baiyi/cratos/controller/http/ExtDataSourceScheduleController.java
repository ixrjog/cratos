package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.eds.EdsScheduleParam;
import com.baiyi.cratos.domain.view.schedule.ScheduleVO;
import com.baiyi.cratos.facade.EdsScheduleFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/8 18:15
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/eds/instance/schedule")
@Tag(name = "External Datasource Instance Schedule")
@RequiredArgsConstructor
public class ExtDataSourceScheduleController {

    private final EdsScheduleFacade scheduleFacade;

    @Operation(summary = "查询指定ID的数据源实例任务")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ScheduleVO.Job>> queryScheduleById(@RequestParam int id) {
        return new HttpResult<>(scheduleFacade.queryJob(id));
    }

    @Operation(summary = "新增数据源实例任务")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addSchedule(@RequestBody @Valid EdsScheduleParam.AddJob addJob) {
        scheduleFacade.addJob(addJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "暂停数据源实例任务")
    @PostMapping(value = "/pause", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pauseSchedule(@RequestBody @Valid EdsScheduleParam.UpdateJob updateJob) {
        scheduleFacade.pauseJob(updateJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "恢复数据源实例任务")
    @PostMapping(value = "/resume", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> resumeSchedule(@RequestBody @Valid EdsScheduleParam.UpdateJob updateJob) {
        scheduleFacade.resumeJob(updateJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除数据源实例任务")
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteSchedule(@RequestBody @Valid EdsScheduleParam.DeleteJob deleteJob) {
        scheduleFacade.deleteJob(deleteJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "检查Cron表达式")
    @PostMapping(value = "/cron/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> checkCron(@RequestBody @Valid EdsScheduleParam.CheckCron checkCron) {
        return new HttpResult<>(scheduleFacade.checkCron(checkCron));
    }

}
