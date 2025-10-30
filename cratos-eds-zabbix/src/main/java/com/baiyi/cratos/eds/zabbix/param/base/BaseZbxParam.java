package com.baiyi.cratos.eds.zabbix.param.base;

import com.baiyi.cratos.eds.zabbix.annotation.ZbxParamMethod;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIGroup;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.aop.support.AopUtils;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 10:14
 * &#064;Version 1.0
 */
public class BaseZbxParam {

    public interface BaseRequest {

        void setMethod(String method);

        void setAuth(String auth);

        String getMethod();

        String getAuth();
    }

    interface HasMethodAnnotate {
        default String acqMethod() {
            ZbxParamMethod zbxParamMethod = AopUtils.getTargetClass(this)
                    .getAnnotation(ZbxParamMethod.class);
            if (zbxParamMethod != null) {
                return zbxParamMethod.group()
                        .name()
                        .toLowerCase() + "." + zbxParamMethod.action()
                        .name()
                        .toLowerCase();
            } else {
                return null; // 或者抛出异常，或者返回默认值
            }
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    public static class DefaultParam extends HasZbxMethod implements BaseZbxParam.BaseRequest {
        private final String jsonrpc = "2.0";
        @Builder.Default
        private Map<String, Object> params = Maps.newHashMap();
        private String auth;
        @Builder.Default
        private Integer id = 1;
    }

    @SuperBuilder(toBuilder = true)
    @ZbxParamMethod(group = ZbxAPIGroup.APIINFO, action = ZbxAPIAction.VERSION)
    public static class InfoVersionParam extends BaseZbxParam.DefaultParam {
    }

}
