package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.crystal.CrystalServerVO;
import com.baiyi.cratos.domain.view.server.ServerAccountVO;
import com.baiyi.cratos.facade.CrystalFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/8 16:51
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/crystal")
@Tag(name = "Crystal")
@RequiredArgsConstructor
public class CrystalController {

    private final CrystalFacade crystalFacade;

    @Operation(summary = "Query eds asset type options")
    @GetMapping(value = "/instance/asset/type/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getInstanceAssetTypeOptions(@RequestParam @Valid String instanceType) {
        return new HttpResult<>(crystalFacade.getInstanceAssetTypeOptions(instanceType));
    }

    @Operation(summary = "Get server account options")
    @GetMapping(value = "/server/account/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ServerAccountVO.ServerAccount>> getServerAccountOptions(@RequestParam int size) {
        return new HttpResult<>(crystalFacade.getServerAccountOptions(size));
    }

    @Operation(summary = "Pagination query eds instance asset（server）")
    @PostMapping(value = "/instance/asset/server/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CrystalServerVO.AssetServer>> queryEdsInstanceAssetPage(
            @RequestBody @Valid EdsInstanceParam.AssetPageQuery assetPageQuery) {
        return new HttpResult<>(crystalFacade.queryEdsInstanceAssetPage(assetPageQuery));
    }

}
