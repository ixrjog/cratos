package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.user.UserParam;
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
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 13:42
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/external/user")
@Tag(name = "User")
@RequiredArgsConstructor
public class ExternalUserController {

    private final UserFacade userFacade;

    @Operation(summary = "Pagination query user")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.User>> queryUserPage(@RequestBody @Valid UserParam.UserPageQuery pageQuery) {
        return HttpResult.of(userFacade.queryUserPage(pageQuery));
    }
}
