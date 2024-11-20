package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.user.UserParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public HttpResult<DataTable<UserVO.User>> queryUserPage(@RequestBody @Valid UserParam.UserPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserPage(pageQuery));
    }

    @Operation(summary = "Get user by username")
    @GetMapping(value = "/username/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.User> getUserByUsername(@RequestParam @Valid String username) {
        return new HttpResult<>(userFacade.getUserByUsername(username));
    }

    @Operation(summary = "Add user")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addUser(@RequestBody @Valid UserParam.AddUser addUser) {
        userFacade.addUser(addUser);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update user")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUser(@RequestBody @Valid UserParam.UpdateUser updateUser) {
        userFacade.updateUser(updateUser);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update user")
    @PutMapping(value = "/my/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateMy(@RequestBody @Valid UserParam.UpdateMy updateMy) {
        userFacade.updateUser(updateMy);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query user sshkey")
    @PostMapping(value = "/my/sshkey/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<CredentialVO.Credential>> queryMySshKey() {
        return new HttpResult<>(userFacade.queryMySshKey());
    }

    @Operation(summary = "Update user valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setUserValidById(@RequestParam int id) {
        userFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Reset user password")
    @PostMapping(value = "/password/reset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> resetUserPassword(@RequestBody @Valid UserParam.ResetPassword resetPassword) {
        userFacade.resetUserPassword(resetPassword);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Grant user permission")
    @PostMapping(value = "/permission/grant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserPermission(@RequestBody @Valid UserParam.ResetPassword resetPassword) {
        userFacade.resetUserPassword(resetPassword);
        return HttpResult.SUCCESS;
    }

}
