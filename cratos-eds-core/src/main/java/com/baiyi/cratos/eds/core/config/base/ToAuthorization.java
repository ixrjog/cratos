package com.baiyi.cratos.eds.core.config.base;

import com.baiyi.cratos.common.util.StringFormatter;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午4:58
 * &#064;Version 1.0
 */
public class ToAuthorization {

    public interface ToAuthorizationBearer {

        String getToken();

        default String toBearer() {
            return StringFormatter.format("Bearer {}", getToken());
        }

        default boolean hasToken() {
            return StringUtils.hasText(getToken());
        }

    }

    public interface ToAuthorizationApikey {

        String getApikey();

        default String toApikey() {
            return StringFormatter.format("Apikey {}", getApikey());
        }

        default boolean hasApikey() {
            return StringUtils.hasText(getApikey());
        }

    }

}
