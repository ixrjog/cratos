package com.baiyi.cratos.domain.util.dnsgoogle;

import com.baiyi.cratos.domain.util.dnsgoogle.enums.DnsTypes;
import com.baiyi.cratos.domain.util.dnsgoogle.model.DnsGoogleModel;
import com.baiyi.cratos.domain.util.dnsgoogle.service.DnsGoogleService;
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

    @Deprecated
    public DnsGoogleModel.DnsResolve resolve(String name) {
        return resolve(name, DnsTypes.CNAME);
    }

    public DnsGoogleModel.DnsResolve resolve(String name, DnsTypes type) {
        Map<String, String> param = Map.of("name", name, "type", type.name());
        return dnsGoogleService.resolve(param);
    }

}
