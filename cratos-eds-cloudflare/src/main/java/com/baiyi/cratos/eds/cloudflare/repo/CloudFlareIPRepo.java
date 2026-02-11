package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.CloudFlareServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/6 10:34
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudFlareIPRepo {

    public static List<String> getIpsV4() {
        return CloudFlareServiceFactory.createIPsService()
                .getIpsV4();
    }

}
