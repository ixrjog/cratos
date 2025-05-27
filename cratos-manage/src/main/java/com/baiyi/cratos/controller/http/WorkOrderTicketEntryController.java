package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.model.LdapUserGroupModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.facade.work.WorkOrderTicketEntryFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:31
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/workorder/ticket/entry")
@Tag(name = "WorkOrder Ticket Entry")
@RequiredArgsConstructor
public class WorkOrderTicketEntryController {

    private final WorkOrderTicketEntryFacade ticketEntryFacade;

    // query entry
    @Operation(summary = "Query aliyun dataWorks instance")
    @PostMapping(value = "/aliyun/dataworks/instance/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<EdsInstanceVO.EdsInstance>> queryDataWorksInstanceTicketEntry() {
        return HttpResult.of(ticketEntryFacade.queryDataWorksInstanceTicketEntry());
    }

    @Operation(summary = "Query application resource deployment")
    @PostMapping(value = "/application/resource/deployment/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<EdsAssetVO.Asset>> queryApplicationResourceDeploymentTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.QueryApplicationResourceDeploymentTicketEntry queryApplicationResourceDeploymentTicketEntry) {
        return HttpResult.of(ticketEntryFacade.queryApplicationResourceDeploymentTicketEntry(
                queryApplicationResourceDeploymentTicketEntry));
    }

    @Operation(summary = "Query LDAP group options")
    @GetMapping(value = "/ldap/group/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getLdapGroupOptions() {
        return HttpResult.of(ticketEntryFacade.getLdapGroupOptions());
    }

    @Operation(summary = "Query LDAP role permission ticket entry")
    @PostMapping(value = "/ldap/role/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LdapUserGroupModel.Role>> queryLdapRolePermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.QueryLdapRolePermissionTicketEntry queryLdapRolePermissionTicketEntry) {
        return HttpResult.of(ticketEntryFacade.queryLdapRolePermissionTicketEntry(
                queryLdapRolePermissionTicketEntry));
    }

    // add entry

    @Operation(summary = "Add application delete pod ticket entry")
    @PostMapping(value = "/application/pod/delete/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplicationDeletePodTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddApplicationDeletePodTicketEntry addTicketEntry) {
        ticketEntryFacade.addApplicationDeletePodTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add aliyun dataWorks instance ticket entry")
    @PostMapping(value = "/aliyun/dataworks/instance/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addDataWorksInstanceTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry addTicketEntry) {
        ticketEntryFacade.addAliyunDataWorksInstanceTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add create aliyun ram user instance ticket entry")
    @PostMapping(value = "/aliyun/ram/user/instance/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCreateAliyunRamUserTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry addTicketEntry) {
        ticketEntryFacade.addCreateAliyunRamUserTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add reset aliyun ram user ticket entry")
    @PostMapping(value = "/aliyun/ram/user/reset/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addResetAliyunRamUserTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry addTicketEntry) {
        ticketEntryFacade.addResetAliyunRamUserTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add aliyun ram policy permission ticket entry")
    @PostMapping(value = "/aliyun/ram/policy/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAliyunRamPolicyPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addAliyunRamPolicyPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add application permission ticket entry")
    @PostMapping(value = "/application/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplicationPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addApplicationPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add application elastic scaling ticket entry")
    @PostMapping(value = "/application/elastic/scaling/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addElasticScalingOfApplicationReplicasTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry addTicketEntry) {
        ticketEntryFacade.addApplicationElasticScalingTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add application deployment elastic scaling ticket entry")
    @PostMapping(value = "/application/deployment/elastic/scaling/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addElasticScalingOfApplicationDeploymentReplicasTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry addTicketEntry) {
        ticketEntryFacade.addApplicationDeploymentElasticScalingTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add computer permission ticket entry")
    @PostMapping(value = "/computer/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addComputerPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addComputerPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add gitLab project permission ticket entry")
    @PostMapping(value = "/gitlab/project/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGitLabProjectPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addGitLabProjectPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add gitLab group permission ticket entry")
    @PostMapping(value = "/gitlab/group/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGitLabGroupPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addGitLabGroupPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add revoke user permission ticket entry")
    @PostMapping(value = "/user/revoke/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRevokeUserPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addRevokeUserPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    // other

    @Operation(summary = "Update ticket entry valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setTicketEntryValidById(@RequestParam int id) {
        ticketEntryFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete ticket entry by id")
    @DeleteMapping(value = "/del/by/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTicketEntryById(@RequestParam int id) {
        ticketEntryFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete all ticket entry by ticketId")
    @DeleteMapping(value = "/del/all/by/ticketId", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAllTicketEntryByTicketId(@RequestParam int ticketId) {
        ticketEntryFacade.deleteAllByTicketId(ticketId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete ticket entry")
    @DeleteMapping(value = "/del", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.DeleteTicketEntry deleteTicketEntry) {
        ticketEntryFacade.deleteTicketEntry(deleteTicketEntry);
        return HttpResult.SUCCESS;
    }

}
