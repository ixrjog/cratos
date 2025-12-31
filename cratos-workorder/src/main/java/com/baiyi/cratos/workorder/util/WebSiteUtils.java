package com.baiyi.cratos.workorder.util;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/24 17:11
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSiteUtils {

    public static String generateWebSite(String domain, String mappingsPath) {
        String path;
        if (mappingsPath.isEmpty() || mappingsPath.equals("/")) {
            return StringFormatter.format("https://{}/", domain);
        } else {
            return StringFormatter.arrayFormat("https://{}/{}", domain, processPath(mappingsPath));
        }
    }

    private static String processPath(String mappingsPath) {
        return mappingsPath.replaceFirst("^/+", "")
                .replaceFirst("/*$", "/");
    }

}
