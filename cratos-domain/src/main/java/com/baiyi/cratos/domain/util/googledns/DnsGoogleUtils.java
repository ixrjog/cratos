package com.baiyi.cratos.domain.util.googledns;

import com.baiyi.cratos.domain.util.googledns.model.DnsGoogleModel;
import com.baiyi.cratos.domain.util.googledns.service.DnsGoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/16 14:21
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DnsGoogleUtils {

    private final DnsGoogleService dnsGoogleService;

    public DnsGoogleModel.DnsResolve resolve(String name) {
        Map<String, String> param = Map.of("name", name, "type", "CNAME");
        return dnsGoogleService.resolve(param);
    }

}
