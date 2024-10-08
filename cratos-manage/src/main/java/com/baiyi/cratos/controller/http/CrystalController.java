package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Query eds instance type options")
    @GetMapping(value = "/instance/asset/type/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getCrystalInstanceAssetTypeOptions(@RequestParam @Valid String instanceType) {
        return new HttpResult<>(EdsInstanceTypeEnum.toOptions());
    }

}
