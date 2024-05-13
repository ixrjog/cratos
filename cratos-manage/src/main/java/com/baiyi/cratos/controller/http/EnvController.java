package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.env.EnvParam;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.facade.EnvFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        return new HttpResult<>(envFacade.queryEnvPage(pageQuery));
    }

    @Operation(summary = "Update env valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setEnvValidById(@RequestParam @Valid int id) {
        envFacade.setEnvValidById(id);
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
