package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.application.ApplicationResourceBaselineParam;
import com.baiyi.cratos.domain.view.application.ApplicationResourceBaselineVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 10:57
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/application/resource/baseline")
@Tag(name = "Application Resource Baseline")
@RequiredArgsConstructor
public class ApplicationResourceBaselineController {

    private final ApplicationResourceBaselineFacade applicationResourceBaselineFacade;

    @Operation(summary = "Scan application resource baseline")
    @PostMapping(value = "/scanAll", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanAllBaseline() {
        applicationResourceBaselineFacade.scanAll();
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Rescan application resource baseline")
    @PutMapping(value = "/rescan", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> rescanBaselineById(int baselineId) {
        applicationResourceBaselineFacade.rescan(baselineId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query application resource baseline type options")
    @GetMapping(value = "/type/options", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getBaselineTypeOptions() {
        return new HttpResult<>(ResourceBaselineTypeEnum.toOptions());
    }

    @Operation(summary = "Pagination query application resource baseline")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationResourceBaselineVO.ResourceBaseline>> queryApplicationResourceBaselinePage(
            @RequestBody @Valid ApplicationResourceBaselineParam.ApplicationResourceBaselinePageQuery pageQuery) {
        return new HttpResult<>(applicationResourceBaselineFacade.queryApplicationResourceBaselinePage(pageQuery));
    }

}
