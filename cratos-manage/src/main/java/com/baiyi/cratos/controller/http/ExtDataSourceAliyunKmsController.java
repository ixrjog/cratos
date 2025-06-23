package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.kms.AliyunKmsParam;
import com.baiyi.cratos.domain.view.aliyun.AliyunKmsVO;
import com.baiyi.cratos.facade.EdsAliyunKmsFacade;
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
 * &#064;Date  2025/6/23 10:01
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/eds/aliyun/kms")
@Tag(name = "External Datasource Aliyun KMS")
@RequiredArgsConstructor
public class ExtDataSourceAliyunKmsController {

    private final EdsAliyunKmsFacade edsAliyunKmsFacade;

    @Operation(summary = "Pagination query secret")
    @PostMapping(value = "/secret/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunKmsVO.Secret>> queryApplicationPage(
            @RequestBody @Valid AliyunKmsParam.SecretPageQuery pageQuery) {
        return HttpResult.of(edsAliyunKmsFacade.queryAliyunKmsSecretPage(pageQuery));
    }

}
