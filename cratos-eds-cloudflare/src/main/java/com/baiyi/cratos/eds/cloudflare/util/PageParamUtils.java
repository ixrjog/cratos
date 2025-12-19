package com.baiyi.cratos.eds.cloudflare.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/18 16:02
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageParamUtils {

    public static Map<String, String> newPage(int page) {
        return Map.ofEntries(entry("page", String.valueOf(page)));
    }

}
