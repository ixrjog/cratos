package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.tag.TagGroupParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.facade.tag.TagGroupFacade;
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
 * &#064;Date  2025/4/2 14:48
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/tag/group")
@Tag(name = "Tag Group")
@RequiredArgsConstructor
public class TagGroupController {

    private final TagGroupFacade tagGroupFacade;

    @Operation(summary = "Get tag group options")
    @PostMapping(value = "/options/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getGroupOptions(
            @RequestBody @Valid TagGroupParam.GetGroupOptions getGroupOptions) {
        return HttpResult.ofBaseException(tagGroupFacade.getGroupOptions(getGroupOptions));
    }

    @Operation(summary = "Pagination query tag group asset")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EdsAssetVO.Asset>> queryGroupAssetPage(
            @RequestBody @Valid TagGroupParam.GroupAssetPageQuery pageQuery) {
        return HttpResult.ofBaseException(tagGroupFacade.queryGroupAssetPage(pageQuery));
    }

}
