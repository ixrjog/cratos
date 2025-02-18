package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.facade.CommandExecFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 15:11
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/command/exec")
@Tag(name = "Command")
@RequiredArgsConstructor
public class CommandController {

    private final CommandExecFacade commandExecFacade;

    @Operation(summary = "Pagination query command exec")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CommandExecVO.CommandExec>> queryCommandExecPage(
            @RequestBody @Valid CommandExecParam.CommandExecPageQuery pageQuery) {
        return HttpResult.of(commandExecFacade.queryCommandExecPage(pageQuery));
    }

    @Operation(summary = "Add command exec")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCommandExec(@RequestBody @Valid CommandExecParam.AddCommandExec addCommandExec) {
        commandExecFacade.addCommandExec(addCommandExec);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Approve command exec")
    @PostMapping(value = "/approve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> approveCommandExec(@RequestBody @Valid CommandExecParam.ApproveCommandExec approveCommandExec) {
        commandExecFacade.approveCommandExec(approveCommandExec);
        return HttpResult.SUCCESS;
    }

}
