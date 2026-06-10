package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.project.ProjectVO;
import com.baiyi.cratos.facade.project.ProjectFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum.CLOUD_LB_TYPES;

@RestController
@RequestMapping("/api/project")
@Tag(name = "Project Tenant")
@RequiredArgsConstructor
public class ProjectTenantController {

    private final ProjectFacade projectFacade;

    @Operation(summary = "Query project tenants")
    @GetMapping(value = "/tenant/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ProjectVO.Tenant>> queryProjectTenants(@RequestParam String projectKey) {
        return new HttpResult<>(projectFacade.queryProjectTenants(projectKey));
    }

    @Operation(summary = "Query project tenant view")
    @PostMapping(value = "/tenant/view/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ProjectVO.TenantView> queryProjectTenantView(
            @RequestBody @Valid ProjectParam.ProjectTenantViewQuery queryParam) {
        return new HttpResult<>(projectFacade.queryProjectTenantView(queryParam));
    }

    @Operation(summary = "Pagination query project tenant")
    @PostMapping(value = "/tenant/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ProjectVO.TenantDetail>> queryProjectTenantPage(
            @RequestBody @Valid ProjectParam.ProjectTenantPageQuery pageQuery) {
        return HttpResult.of(projectFacade.queryProjectTenantPage(pageQuery));
    }

    @Operation(summary = "Add project tenant")
    @PostMapping(value = "/tenant/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addProjectTenant(@RequestBody @Valid ProjectParam.AddProjectTenant addProjectTenant) {
        projectFacade.addProjectTenant(addProjectTenant);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update project tenant")
    @PutMapping(value = "/tenant/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateProjectTenant(
            @RequestBody @Valid ProjectParam.UpdateProjectTenant updateProjectTenant) {
        projectFacade.updateProjectTenant(updateProjectTenant);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete project tenant by id")
    @DeleteMapping(value = "/tenant/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteProjectTenantById(@RequestParam int id) {
        projectFacade.deleteProjectTenantById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query load balancers by tenant id")
    @GetMapping(value = "/lb/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ProjectVO.LoadBalancer>> queryLoadBalancersByTenantId(@RequestParam int tenantId) {
        return HttpResult.of(projectFacade.queryLoadBalancersByTenantId(tenantId));
    }

    @Operation(summary = "Add project load balancer")
    @PostMapping(value = "/lb/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addProjectLoadBalancer(@RequestBody @Valid ProjectParam.AddProjectLoadBalancer param) {
        projectFacade.addProjectLoadBalancer(param);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update project load balancer")
    @PutMapping(value = "/lb/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateProjectLoadBalancer(
            @RequestBody @Valid ProjectParam.UpdateProjectLoadBalancer param) {
        projectFacade.updateProjectLoadBalancer(param);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete project load balancer by id")
    @DeleteMapping(value = "/lb/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteProjectLoadBalancerById(@RequestParam int id) {
        projectFacade.deleteProjectLoadBalancerById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Get load balancer type options")
    @GetMapping(value = "/lb/type/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getLoadBalancerTypeOptions() {
        return HttpResult.of(OptionsVO.toOptions(CLOUD_LB_TYPES.stream()
                                                         .map(Enum::name)
                                                         .toList()));
    }

    @Operation(summary = "Query groups by tenant id")
    @GetMapping(value = "/group/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ProjectVO.GroupDetail>> queryGroupsByTenantId(@RequestParam int tenantId) {
        return HttpResult.of(projectFacade.queryGroupsByTenantId(tenantId));
    }

    @Operation(summary = "Add project group")
    @PostMapping(value = "/group/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addProjectGroup(@RequestBody @Valid ProjectParam.AddProjectGroup param) {
        projectFacade.addProjectGroup(param);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete project group by id")
    @DeleteMapping(value = "/group/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteProjectGroupById(@RequestParam int id) {
        projectFacade.deleteProjectGroupById(id);
        return HttpResult.SUCCESS;
    }

}
