package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.facade.EdsIdentityFacade;
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
 * &#064;Date  2025/2/27 09:50
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/eds/identity/")
@Tag(name = "External Datasource Identity")
@RequiredArgsConstructor
public class ExtDataSourceIdentityController {

    private final EdsIdentityFacade edsIdentityFacade;

    @Operation(summary = "Query eds asset cloud identity by username")
    @PostMapping(value = "/cloud/details/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsIdentityVO.CloudIdentityDetails> queryCloudIdentityDetails(
            @RequestBody @Valid EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails) {
        return HttpResult.of(edsIdentityFacade.queryCloudIdentityDetails(queryCloudIdentityDetails));
    }

    @Operation(summary = "Query eds asset ldap identity by username")
    @PostMapping(value = "/ldap/details/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsIdentityVO.LdapIdentityDetails> queryLdapIdentityDetails(
            @RequestBody @Valid EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails) {
        return HttpResult.of(edsIdentityFacade.queryLdapIdentityDetails(queryLdapIdentityDetails));
    }

    @Operation(summary = "Create eds ldap identity")
    @PostMapping(value = "/ldap/user/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsIdentityVO.LdapIdentity> createLdapIdentity(
            @RequestBody @Valid EdsIdentityParam.CreateLdapIdentity createLdapIdentity) {
        return HttpResult.of(edsIdentityFacade.createLdapIdentity(createLdapIdentity));
    }

    @Operation(summary = "Add ldap user to the group")
    @PostMapping(value = "/ldap/user/addToGroup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLdapUserToTheGroup(
            @RequestBody @Valid EdsIdentityParam.AddLdapUserToTheGroup addLdapUserToTheGroup) {
        edsIdentityFacade.addLdapUserToTheGroup(addLdapUserToTheGroup);
        return HttpResult.SUCCESS;
    }

    // Remove users from the group

    // attach detach


    @Operation(summary = "Query eds asset dingtalk identity by username")
    @PostMapping(value = "/dingtalk/details/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsIdentityVO.DingtalkIdentityDetails> queryDingtalkIdentityDetails(
            @RequestBody @Valid EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
        return HttpResult.of(edsIdentityFacade.queryDingtalkIdentityDetails(queryDingtalkIdentityDetails));
    }

    @Operation(summary = "Query eds asset gitLab identity by username")
    @PostMapping(value = "/gitlab/details/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsIdentityVO.GitLabIdentityDetails> queryGitLabIdentityDetails(
            @RequestBody @Valid EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails) {
        return HttpResult.of(edsIdentityFacade.queryGitLabIdentityDetails(queryGitLabIdentityDetails));
    }

}
