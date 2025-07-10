package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.env.EnvParam;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.facade.EnvFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/19 14:15
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/env")
@Tag(name = "Environment")
@RequiredArgsConstructor
public class EnvController {

    private final EnvFacade envFacade;

    @Operation(summary = "Pagination query env")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EnvVO.Env>> queryEnvPage(@RequestBody @Valid EnvParam.EnvPageQuery pageQuery) {
        return HttpResult.ofBaseException(envFacade.queryEnvPage(pageQuery));
    }

    @Operation(summary = "Query env by group value")
    @PostMapping(value = "/query/by/group/value", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<EnvVO.Env>> queryEnvByGroupValue(@RequestBody @Valid EnvParam.QueryEnvByGroupValue queryEnvByGroupValue) {
        return HttpResult.ofBaseException(envFacade.queryEnvByGroupValue(queryEnvByGroupValue));
    }

    @Operation(summary = "Update env valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setEnvValidById(@RequestParam int id) {
        envFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add env")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addEnv(@RequestBody @Valid EnvParam.AddEnv addEnv) {
        envFacade.addEnv(addEnv);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update env")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateEnv(@RequestBody @Valid EnvParam.UpdateEnv updateEnv) {
        envFacade.updateEnv(updateEnv);
        return HttpResult.SUCCESS;
    }

}
