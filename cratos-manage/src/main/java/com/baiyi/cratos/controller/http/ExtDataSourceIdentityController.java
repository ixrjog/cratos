package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/27 09:50
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/eds/identity")
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

    @Operation(summary = "Create cloud account")
    @PostMapping(value = "/cloud/account/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsIdentityVO.CloudAccount> createCloudAccount(
            @RequestBody @Valid EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        return HttpResult.of(edsIdentityFacade.createCloudAccount(createCloudAccount));
    }

    @Operation(summary = "Grant cloud account permission")
    @PostMapping(value = "/cloud/account/permission/grant", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantCloudAccountPermission(
            @RequestBody @Valid EdsIdentityParam.GrantPermission grantPermission) {
        edsIdentityFacade.grantCloudAccountPermission(grantPermission);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Revoke cloud account permission")
    @PostMapping(value = "/cloud/account/permission/revoke", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeCloudAccountPermission(
            @RequestBody @Valid EdsIdentityParam.RevokePermission revokePermission) {
        edsIdentityFacade.revokeCloudAccountPermission(revokePermission);
        return HttpResult.SUCCESS;
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

    @Operation(summary = "Delete eds ldap identity")
    @PostMapping(value = "/ldap/user/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLdapIdentity(
            @RequestBody @Valid EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity) {
        edsIdentityFacade.deleteLdapIdentity(deleteLdapIdentity);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add ldap user to the group")
    @PostMapping(value = "/ldap/user/addToGroup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLdapUserToTheGroup(
            @RequestBody @Valid EdsIdentityParam.AddLdapUserToGroup addLdapUserToGroup) {
        edsIdentityFacade.addLdapUserToGroup(addLdapUserToGroup);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Remove ldap user from group")
    @PostMapping(value = "/ldap/user/removeFromGroup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> removeLdapUserFromGroup(
            @RequestBody @Valid EdsIdentityParam.RemoveLdapUserFromGroup removeLdapUserFromGroup) {
        edsIdentityFacade.removeLdapUserFromGroup(removeLdapUserFromGroup);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Reset ldap user password")
    @PutMapping(value = "/ldap/user/password/reset", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsIdentityVO.LdapIdentity> resetLdapUserPassword(
            @RequestBody @Valid EdsIdentityParam.ResetLdapUserPassword resetLdapUserPassword) {
        return HttpResult.of(edsIdentityFacade.resetLdapUserPassword(resetLdapUserPassword));
    }

    @Operation(summary = "Remove ldap user from group")
    @PostMapping(value = "/ldap/group/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Set<String>> queryLdapGroups(
            @RequestBody @Valid EdsIdentityParam.QueryLdapGroups queryLdapGroups) {
        return HttpResult.of(edsIdentityFacade.queryLdapGroups(queryLdapGroups));
    }

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

    @Operation(summary = "Query eds asset mail identity by username")
    @PostMapping(value = "/mail/details/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsIdentityVO.MailIdentityDetails> queryMailIdentityDetails(
            @RequestBody @Valid EdsIdentityParam.QueryMailIdentityDetails queryMailIdentityDetails) {
        return HttpResult.of(edsIdentityFacade.queryMailIdentityDetails(queryMailIdentityDetails));
    }

}
