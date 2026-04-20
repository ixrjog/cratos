package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.channel.ChannelNetworkParam;
import com.baiyi.cratos.domain.param.http.channel.OrganizationParam;
import com.baiyi.cratos.domain.view.channel.ChannelNetworkVO;
import com.baiyi.cratos.domain.view.channel.OrganizationVO;
import com.baiyi.cratos.facade.channel.ChannelNetworkFacade;
import com.baiyi.cratos.facade.channel.OrganizationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/channel")
@Tag(name = "ChannelNetwork")
@RequiredArgsConstructor
public class ChannelController {

    private final OrganizationFacade organizationFacade;
    private final ChannelNetworkFacade channelNetworkFacade;

    @Operation(summary = "Pagination query organization")
    @PostMapping(value = "/organization/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OrganizationVO.Organization>> queryOrganizationPage(
            @RequestBody @Valid OrganizationParam.OrganizationPageQuery pageQuery) {
        return HttpResult.of(organizationFacade.queryOrganizationPage(pageQuery));
    }

    @Operation(summary = "Add organization")
    @PostMapping(value = "/organization/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addOrganization(
            @RequestBody @Valid OrganizationParam.AddOrganization addOrganization) {
        organizationFacade.addOrganization(addOrganization);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update organization")
    @PutMapping(value = "/organization/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateOrganization(
            @RequestBody @Valid OrganizationParam.UpdateOrganization updateOrganization) {
        organizationFacade.updateOrganization(updateOrganization);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete organization by id")
    @DeleteMapping(value = "/organization/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteOrganizationById(@RequestParam int id) {
        organizationFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    // Network

    @Operation(summary = "Add channelNetwork")
    @PostMapping(value = "/network/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannelNetwork(
            @RequestBody @Valid ChannelNetworkParam.AddChannelNetwork addChannelNetwork) {
        channelNetworkFacade.addChannelNetwork(addChannelNetwork);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channelNetwork")
    @PutMapping(value = "/network/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateChannelNetwork(
            @RequestBody @Valid ChannelNetworkParam.UpdateChannelNetwork updateChannelNetwork) {
        channelNetworkFacade.updateChannelNetwork(updateChannelNetwork);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channelNetwork valid")
    @PutMapping(value = "/network/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setChannelNetworkValidById(@RequestParam int id) {
        channelNetworkFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query channelNetwork")
    @PostMapping(value = "/network/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ChannelNetworkVO.ChannelNetwork>> queryChannelNetworkPage(
            @RequestBody @Valid ChannelNetworkParam.ChannelNetworkPageQuery pageQuery) {
        return HttpResult.of(channelNetworkFacade.queryChannelNetworkPage(pageQuery));
    }

    @Operation(summary = "Delete channelNetwork by id")
    @DeleteMapping(value = "/network/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelNetworkById(@RequestParam int id) {
        channelNetworkFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
