package com.baiyi.cratos.facade.validator.credential;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/2/4 10:34
 * @Version 1.0
 */
@Slf4j
public class CredentialValidatorFactory {

    private CredentialValidatorFactory() {
    }

    private static final Map<CredentialTypeEnum, ICredentialValidator> CONTEXT = new ConcurrentHashMap<>();

    public static void register(ICredentialValidator bean) {
        CONTEXT.put(bean.getType(), bean);
        log.debug(StringFormatter.inDramaFormat("CredentialValidatorFactory"));
        log.debug("CredentialValidatorFactory Registered: beanName={}, type={}", bean.getClass()
                .getSimpleName(), bean.getType().name());
    }

    public static ICredentialValidator getValidator(CredentialTypeEnum typeEnum) {
        if (CONTEXT.containsKey(typeEnum)) {
            return CONTEXT.get(typeEnum);
        }
        return null;
    }

}
