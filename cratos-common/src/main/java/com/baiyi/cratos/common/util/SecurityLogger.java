package com.baiyi.cratos.common.util;

import com.baiyi.cratos.domain.util.JSONUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/28 13:53
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityLogger {

    private static final Logger SECURITY = LoggerFactory.getLogger("SECURITY");

    public interface Action {
        String LOGIN_SUCCESS = "login_success";
        String LOGIN_FAILED = "login_failed";
        String LOGOUT = "logout";

        // 权限相关
        String GRANT_ROLE = "grant_role";
        String REVOKE_ROLE = "revoke_role";
        String GRANT_PERMISSION = "grant_permission";
        String REVOKE_PERMISSION = "revoke_permission";

        // 操作相关
        String CREATE = "create";
        String UPDATE = "update";
        String DELETE = "delete";
        String QUERY = "query";
        String EXPORT = "export";
        String IMPORT = "import";

        // 配置相关
        String UPDATE_CONFIG = "update_config";
        String DELETE_CONFIG = "delete_config";

        // API相关
        String API_CALL = "api_call";

        // 系统相关
        String START = "start";
        String STOP = "stop";
        String RESTART = "restart";
    }

    public enum EventType {
        LOGIN,
        LOGOUT,
        PERMISSION,
        OPERATION,
        CONFIG,
        API,
        DATA_ACCESS,
        SYSTEM
    }

    public static void log(EventType eventType, String user, String action, String resource) {
        log(eventType.name(), user, action, resource);
    }

    public static void log(String eventType, String user, String action, String resource) {
        Map<String, Object> event = Map.of(
                "timestamp", Instant.now()
                        .toString(), "event_type", eventType, "user", user, "action", action, "resource", resource
        );
        SECURITY.info(JSONUtils.writeValueAsString(event));
    }

}
