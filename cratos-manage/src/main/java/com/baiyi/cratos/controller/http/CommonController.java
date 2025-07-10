package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.util.dnsgoogle.DnsGoogleUtils;
import com.baiyi.cratos.domain.util.dnsgoogle.model.DnsGoogleModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/16 14:33
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/common")
@Tag(name = "Application")
@RequiredArgsConstructor
public class CommonController {

    private final DnsGoogleUtils dnsGoogleUtils;

    @Operation(summary = "Resolve DNS(CNAME)")
    @GetMapping(value = "/util/dns/google/resolve", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DnsGoogleModel.DnsResolve> resolveDns(@RequestParam String name) {
        return HttpResult.ofBaseException(dnsGoogleUtils.resolve(name));
    }

}
