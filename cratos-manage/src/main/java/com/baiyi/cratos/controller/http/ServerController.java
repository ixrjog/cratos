package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.server.ServerParam;
import com.baiyi.cratos.domain.view.server.ServerVO;
import com.baiyi.cratos.facade.server.ServerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/4/15 上午11:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/server")
@Tag(name = "Server")
@RequiredArgsConstructor
public class ServerController {

    private final ServerFacade serverFacade;

    @Operation(summary = "Add server")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServer(@RequestBody @Valid ServerParam.AddServer addServer) {
        serverFacade.addServer(addServer);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update server")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServer(@RequestBody @Valid ServerParam.UpdateServer updateServer) {
        serverFacade.updateServer(updateServer);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update server valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setServerValidById(@RequestParam int id) {
        serverFacade.setServerValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query server")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerVO.Server>> queryServerPage(@RequestBody @Valid ServerParam.ServerPageQuery pageQuery) {
        return new HttpResult<>(serverFacade.queryServerPage(pageQuery));
    }

    @Operation(summary = "Delete server by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerById(@RequestParam int id) {
        serverFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
