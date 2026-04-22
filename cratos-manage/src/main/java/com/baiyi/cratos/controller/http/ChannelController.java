package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.enums.ChannelBusinessTypeEnum;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelExtension;
import com.baiyi.cratos.domain.param.http.channel.*;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.channel.*;
import com.baiyi.cratos.facade.channel.*;
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
    private final ChannelLineFacade channelLineFacade;

    // Channel

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
    public HttpResult<java.util.List<ChannelExtension>> queryChannelExtensions(@RequestParam int channelId) {
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

    // Line
    @Operation(summary = "Pagination query channel line")
    @PostMapping(value = "/line/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ChannelLineVO.Line>> queryChannelLinePage(@RequestBody @Valid ChannelLineParam.ChannelLinePageQuery pageQuery) {
        return HttpResult.of(channelLineFacade.queryChannelLinePage(pageQuery));
    }

    @Operation(summary = "Add channel line")
    @PostMapping(value = "/line/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannelLine(@RequestBody @Valid ChannelLineParam.AddChannelLine addChannelLine) {
        channelLineFacade.addChannelLine(addChannelLine);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channel line")
    @PutMapping(value = "/line/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateChannelLine(@RequestBody @Valid ChannelLineParam.UpdateChannelLine updateChannelLine) {
        channelLineFacade.updateChannelLine(updateChannelLine);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete channel line by id")
    @DeleteMapping(value = "/line/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelLineById(@RequestParam int id) {
        channelLineFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    // Business Line
    @Operation(summary = "Query channel business lines")
    @GetMapping(value = "/business/line/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ChannelLineVO.Line>> queryChannelBusinessLines(@RequestParam int channelBusinessId) {
        return HttpResult.of(channelLineFacade.queryChannelBusinessLines(channelBusinessId));
    }

    @Operation(summary = "Add channel business line")
    @PostMapping(value = "/business/line/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannelBusinessLine(@RequestBody @Valid ChannelBusinessLineParam.AddChannelBusinessLine param) {
        channelLineFacade.addChannelBusinessLine(param);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete channel business line by id")
    @DeleteMapping(value = "/business/line/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelBusinessLineById(@RequestParam int id) {
        channelLineFacade.deleteChannelBusinessLineById(id);
        return HttpResult.SUCCESS;
    }

}
