package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.asset.AssetMaturityParam;
import com.baiyi.cratos.domain.view.asset.AssetMaturityVO;
import com.baiyi.cratos.facade.AssetMaturityFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午3:18
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/asset/maturity")
@Tag(name = "Asset Maturity")
@RequiredArgsConstructor
public class AssetMaturityController {

    private final AssetMaturityFacade assetMaturityFacade;

    @Operation(summary = "Pagination query assetMaturity")
    @PostMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AssetMaturityVO.AssetMaturity>> queryAssetMaturityPage(
            @RequestBody @Valid AssetMaturityParam.AssetMaturityPageQuery pageQuery) {
        return HttpResult.ofBaseException(assetMaturityFacade.queryAssetMaturityPage(pageQuery));
    }

    @Operation(summary = "Add assetMaturity")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAssetMaturity(@RequestBody @Valid AssetMaturityParam.AddAssetMaturity addAssetMaturity) {
        assetMaturityFacade.addAssetMaturity(addAssetMaturity);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update assetMaturity")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateAssetMaturity(@RequestBody @Valid AssetMaturityParam.UpdateAssetMaturity updateAssetMaturity) {
        assetMaturityFacade.updateAssetMaturity(updateAssetMaturity);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update assetMaturity valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setAssetMaturityValidById(@RequestParam int id) {
        assetMaturityFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete assetMaturity by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAssetMaturityById(@RequestParam int id) {
        assetMaturityFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
