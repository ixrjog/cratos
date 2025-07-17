package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.command.CommandExecFacade;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

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
    private final EdsFacade edsFacade;
    private final UserFacade userFacade;

    @Operation(summary = "Pagination query command exec")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CommandExecVO.CommandExec>> queryCommandExecPage(
            @RequestBody @Valid CommandExecParam.CommandExecPageQuery pageQuery) {
        return HttpResult.ofBody(commandExecFacade.queryCommandExecPage(pageQuery));
    }

    @Operation(summary = "Add command exec")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCommandExec(@RequestBody @Valid CommandExecParam.AddCommandExec addCommandExec) {
        commandExecFacade.addCommandExec(addCommandExec);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Approve command exec")
    @PostMapping(value = "/approve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> approveCommandExec(
            @RequestBody @Valid CommandExecParam.ApproveCommandExec approveCommandExec) {
        commandExecFacade.approveCommandExec(approveCommandExec);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Do command exec")
    @PostMapping(value = "/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> doCommandExec(@RequestBody @Valid CommandExecParam.DoCommandExec doCommandExec) {
        commandExecFacade.doCommandExec(doCommandExec);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Admin do command exec")
    @PostMapping(value = "/admin/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> adminDoCommandExec(@RequestBody @Valid CommandExecParam.DoCommandExec doCommandExec) {
        commandExecFacade.adminDoCommandExec(doCommandExec);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query eds instance")
    @PostMapping(value = "/instance/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EdsInstanceVO.EdsInstance>> queryCommandExecEdsInstancePage(
            @RequestBody @Valid EdsInstanceParam.CommandExecInstancePageQuery pageQuery) {
        return HttpResult.ofBody(edsFacade.queryCommandExecEdsInstancePage(pageQuery));
    }

    @Operation(summary = "Pagination query eds instance namespaces")
    @PostMapping(value = "/instance/namespace/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Set<String>> queryCommandExecEdsInstanceNamespace(
            @RequestBody @Valid EdsInstanceParam.QueryCommandExecInstanceNamespace query) {
        return HttpResult.ofBody(edsFacade.queryCommandExecEdsInstanceNamespace(query));
    }

    @Operation(summary = "Pagination query user")
    @PostMapping(value = "/user/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.User>> queryCommandExecUserPage(
            @RequestBody @Valid UserParam.CommandExecUserPageQuery pageQuery) {
        return HttpResult.ofBody(userFacade.queryCommandExecUserPage(pageQuery));
    }

}
