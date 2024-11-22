package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.ssh.SshCommandParam;
import com.baiyi.cratos.domain.param.http.ssh.SshSessionParam;
import com.baiyi.cratos.domain.view.ssh.SshCommandVO;
import com.baiyi.cratos.domain.view.ssh.SshSessionVO;
import com.baiyi.cratos.facade.SshSessionFacade;
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
 * &#064;Date  2024/5/27 上午11:09
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/ssh/session")
@Tag(name = "SSH Session")
@RequiredArgsConstructor
public class SshSessionController {

    private final SshSessionFacade sshSessionFacade;

    @Operation(summary = "Pagination query session")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<SshSessionVO.Session>> querySshSessionPage(
            @RequestBody @Valid SshSessionParam.SshSessionPageQuery pageQuery) {
        return new HttpResult<>(sshSessionFacade.querySshSessionPage(pageQuery));
    }

    @Operation(summary = "Pagination query command")
    @PostMapping(value = "/instance/command/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<SshCommandVO.Command>> querySshCommandPage(
            @RequestBody @Valid SshCommandParam.SshCommandPageQuery pageQuery) {
        return new HttpResult<>(sshSessionFacade.querySshCommandPage(pageQuery));
    }

}
