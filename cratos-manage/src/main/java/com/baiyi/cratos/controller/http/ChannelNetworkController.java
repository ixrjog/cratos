package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.channel.ChannelNetworkParam;
import com.baiyi.cratos.domain.view.channel.ChannelNetworkVO;
import com.baiyi.cratos.facade.ChannelNetworkFacade;
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
@RequestMapping("/api/channel/network")
@Tag(name = "ChannelNetwork")
@RequiredArgsConstructor
public class ChannelNetworkController {

    private final ChannelNetworkFacade channelNetworkFacade;

    @Operation(summary = "Add channelNetwork")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addChannelNetwork(@RequestBody @Valid ChannelNetworkParam.AddChannelNetwork addChannelNetwork) {
        channelNetworkFacade.addChannelNetwork(addChannelNetwork);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channelNetwork")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateChannelNetwork(@RequestBody @Valid ChannelNetworkParam.UpdateChannelNetwork updateChannelNetwork) {
        channelNetworkFacade.updateChannelNetwork(updateChannelNetwork);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update channelNetwork valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setChannelNetworkValidById(@RequestParam int id) {
        channelNetworkFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query channelNetwork")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ChannelNetworkVO.ChannelNetwork>> queryChannelNetworkPage(@RequestBody @Valid ChannelNetworkParam.ChannelNetworkPageQuery pageQuery) {
        return new HttpResult<>(channelNetworkFacade.queryChannelNetworkPage(pageQuery));
    }

    @Operation(summary = "Delete channelNetwork by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteChannelNetworkById(@RequestParam int id) {
        channelNetworkFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
