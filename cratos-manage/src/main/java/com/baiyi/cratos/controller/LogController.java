package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;
import com.baiyi.cratos.facade.AuthFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/10 11:35
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/log")
@Tag(name = "Login")
@RequiredArgsConstructor
public class LogController {

    private final AuthFacade authFacade;

    @Operation(summary = "Login")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LoginVO.Login> login(@RequestBody LoginParam.Login loginParam) {
        return new HttpResult<>(authFacade.login(loginParam));
    }

    @Operation(summary = "Logout")
    @PutMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> logout() {
        authFacade.logout();
        return HttpResult.SUCCESS;
    }

}
