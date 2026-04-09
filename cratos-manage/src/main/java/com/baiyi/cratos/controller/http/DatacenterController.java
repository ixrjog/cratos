package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.datacenter.DatacenterNetworkParam;
import com.baiyi.cratos.domain.view.datacenter.DatacenterVO;
import com.baiyi.cratos.facade.DatacenterFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 17:12
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/datacenter")
@Tag(name = "Datacenter")
@RequiredArgsConstructor
public class DatacenterController {

    private final DatacenterFacade datacenterFacade;

    @Operation(summary = "Add datacenter network")
    @PostMapping(value = "/network/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addNetwork(@RequestBody @Valid DatacenterNetworkParam.AddNetwork addNetwork) {
        datacenterFacade.addNetwork(addNetwork);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update datacenter network")
    @PutMapping(value = "/network/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateNetwork(@RequestBody @Valid DatacenterNetworkParam.UpdateNetwork updateNetwork) {
        datacenterFacade.updateNetwork(updateNetwork);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Scan datacenter network allocation")
    @PutMapping(value = "/network/allocation/scan", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanNetworkAllocation(@RequestParam int networkId) {
        datacenterFacade.scanNetworkAllocation(networkId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query datacenter network")
    @PostMapping(value = "/network/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DatacenterVO.Network>> queryNetworkPage(
            @RequestBody @Valid DatacenterNetworkParam.NetworkPageQuery pageQuery) {
        return HttpResult.of(datacenterFacade.queryNetworkPage(pageQuery));
    }

    @Operation(summary = "Find available CIDRs in a private address range")
    @PostMapping(value = "/allocation/cidr/available/find", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DatacenterVO.AvailableCidrResult> findAvailableCidrs(
            @RequestBody @Valid DatacenterNetworkParam.FindAvailableCidr findAvailableCidr) {
        return HttpResult.of(datacenterFacade.findAvailableCidrs(findAvailableCidr));
    }

    @Operation(summary = "Check CIDR conflict")
    @PostMapping(value = "/allocation/cidr/conflict/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DatacenterVO.CidrConflictResult> checkCidrConflict(
            @RequestBody @Valid DatacenterNetworkParam.CheckCidrConflict checkCidrConflict) {
        return HttpResult.of(datacenterFacade.checkCidrConflict(checkCidrConflict));
    }

    @Operation(summary = "Add datacenter allocation")
    @PostMapping(value = "/allocation/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAllocation(@RequestBody @Valid DatacenterNetworkParam.AddAllocation addAllocation) {
        datacenterFacade.addAllocation(addAllocation);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update datacenter allocation")
    @PutMapping(value = "/allocation/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateAllocation(
            @RequestBody @Valid DatacenterNetworkParam.UpdateAllocation updateAllocation) {
        datacenterFacade.updateAllocation(updateAllocation);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete datacenter allocation by id")
    @DeleteMapping(value = "/allocation/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAllocationById(@RequestParam int id) {
        datacenterFacade.deleteAllocationById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query datacenter allocation")
    @PostMapping(value = "/allocation/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DatacenterVO.Allocation>> queryAllocationPage(
            @RequestBody @Valid DatacenterNetworkParam.AllocationPageQuery pageQuery) {
        return HttpResult.of(datacenterFacade.queryAllocationPage(pageQuery));
    }

}
