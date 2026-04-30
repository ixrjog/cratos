package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.enums.ChannelBusinessTypeEnum;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.channel.*;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.channel.*;
import com.baiyi.cratos.facade.channel.*;
import com.baiyi.cratos.service.channel.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
    private final ChannelBusinessFacade channelBusinessFacade;
    private final ChannelNetworkFacade channelNetworkFacade;
    private final ChannelFacade channelFacade;
    private final ChannelNodeFacade channelNodeFacade;
    private final ChannelService channelService;

    // Channel

    @Operation(summary = "Get channel country options")
    @GetMapping(value = "/country/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getChannelCountryOptions() {
        return HttpResult.of(channelService.queryCountryOptions());
    }

    @Operation(summary = "Pagination query channel")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ChannelVO.Channel>> queryChannelPage(
            @RequestBody @Valid ChannelParam.ChannelPageQuery pageQuery) {
        return HttpResult.of(channelFacade.queryChannelPage(pageQuery));
    }

    @Operation(summary = "Add channel")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannel(@RequestBody @Valid ChannelParam.AddChannel addChannel) {
        channelFacade.addChannel(addChannel);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channel")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateChannel(@RequestBody @Valid ChannelParam.UpdateChannel updateChannel) {
        channelFacade.updateChannel(updateChannel);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channel valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setChannelValidById(@RequestParam int id) {
        channelFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete channel by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelById(@RequestParam int id) {
        channelFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Call channel alert")
    @PostMapping(value = "/alert/call", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> callChannelAlert(@RequestBody @Valid ChannelParam.CallAlert callAlert) {
        channelFacade.callChannelAlert(callAlert);
        return HttpResult.SUCCESS;
    }

    // Organization
    @PostMapping(value = "/organization/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OrganizationVO.Organization>> queryOrganizationPage(
            @RequestBody @Valid OrganizationParam.OrganizationPageQuery pageQuery) {
        return HttpResult.of(organizationFacade.queryOrganizationPage(pageQuery));
    }

    @Operation(summary = "Add organization")
    @PostMapping(value = "/organization/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addOrganization(@RequestBody @Valid OrganizationParam.AddOrganization addOrganization) {
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

    // Extension

    @Operation(summary = "Query channel extensions by channelId")
    @GetMapping(value = "/extension/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<java.util.List<ChannelExtensionVO.Extension>> queryChannelExtensions(@RequestParam int channelId) {
        return HttpResult.of(channelFacade.queryChannelExtensions(channelId));
    }

    @Operation(summary = "Add channel extension")
    @PostMapping(value = "/extension/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannelExtension(
            @RequestBody @Valid ChannelExtensionParam.AddChannelExtension addChannelExtension) {
        channelFacade.addChannelExtension(addChannelExtension);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete channel extension by id")
    @DeleteMapping(value = "/extension/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelExtensionById(@RequestParam int id) {
        channelFacade.deleteChannelExtensionById(id);
        return HttpResult.SUCCESS;
    }

    // Business

    @Operation(summary = "Pagination query channel business")
    @PostMapping(value = "/business/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ChannelBusinessVO.Business>> queryChannelBusinessPage(
            @RequestBody @Valid ChannelBusinessParam.ChannelBusinessPageQuery pageQuery) {
        return HttpResult.of(channelBusinessFacade.queryChannelBusinessPage(pageQuery));
    }

    @Operation(summary = "Add channel business")
    @PostMapping(value = "/business/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannelBusiness(
            @RequestBody @Valid ChannelBusinessParam.AddChannelBusiness addChannelBusiness) {
        channelBusinessFacade.addChannelBusiness(addChannelBusiness);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channel business")
    @PutMapping(value = "/business/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateChannelBusiness(
            @RequestBody @Valid ChannelBusinessParam.UpdateChannelBusiness updateChannelBusiness) {
        channelBusinessFacade.updateChannelBusiness(updateChannelBusiness);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channel business valid")
    @PutMapping(value = "/business/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setChannelBusinessValidById(@RequestParam int id) {
        channelBusinessFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete channel business by id")
    @DeleteMapping(value = "/business/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelBusinessById(@RequestParam int id) {
        channelBusinessFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Get channel business type options")
    @GetMapping(value = "/business/type/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getChannelBusinessTypeOptions() {
        return HttpResult.of(OptionsVO.toOptions(Arrays.stream(ChannelBusinessTypeEnum.values())
                                                         .map(Enum::name)
                                                         .toList()));
    }

    // Node
    @Operation(summary = "Pagination query channel node")
    @PostMapping(value = "/node/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ChannelNodeVO.Node>> queryChannelNodePage(
            @RequestBody @Valid ChannelNodeParam.ChannelNodePageQuery pageQuery) {
        return HttpResult.of(channelNodeFacade.queryChannelNodePage(pageQuery));
    }

    @Operation(summary = "Add channel node")
    @PostMapping(value = "/node/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannelNode(@RequestBody @Valid ChannelNodeParam.AddChannelNode addChannelNode) {
        channelNodeFacade.addChannelNode(addChannelNode);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channel node")
    @PutMapping(value = "/node/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateChannelNode(
            @RequestBody @Valid ChannelNodeParam.UpdateChannelNode updateChannelNode) {
        channelNodeFacade.updateChannelNode(updateChannelNode);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete channel node by id")
    @DeleteMapping(value = "/node/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelNodeById(@RequestParam int id) {
        channelNodeFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    // Business Node
    @Operation(summary = "Query channel business nodes")
    @GetMapping(value = "/business/node/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ChannelNodeVO.Node>> queryChannelBusinessNodes(@RequestParam int channelBusinessId) {
        return HttpResult.of(channelNodeFacade.queryChannelBusinessNodes(channelBusinessId));
    }

    @Operation(summary = "Add channel business node")
    @PostMapping(value = "/business/node/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannelBusinessNode(
            @RequestBody @Valid ChannelBusinessNodeParam.AddChannelBusinessNode param) {
        channelNodeFacade.addChannelBusinessNode(param);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete channel business node by id")
    @DeleteMapping(value = "/business/node/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelBusinessNodeById(@RequestParam int businessId, int nodeId) {
        channelNodeFacade.deleteChannelBusinessNode(businessId, nodeId);
        return HttpResult.SUCCESS;
    }

}
