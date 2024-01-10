package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.user.UserParam;
import com.baiyi.cratos.domain.view.user.UserVO;
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

/**
 * @Author baiyi
 * @Date 2024/1/10 10:14
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @Operation(summary = "Pagination query user")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.User>> queryTagPage(@RequestBody @Valid UserParam.UserPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserPage(pageQuery));
    }

}
