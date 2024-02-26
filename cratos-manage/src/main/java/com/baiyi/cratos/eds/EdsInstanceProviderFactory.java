package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.facade.validator.credential.ICredentialValidator;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2024/2/23 17:30
 * @Version 1.0
 */
@Slf4j
public class EdsInstanceProviderFactory {

    private EdsInstanceProviderFactory() {
    }

    // private static final Map<CredentialTypeEnum, ?> CONTEXT = new ConcurrentHashMap<>();

    public static void register(ICredentialValidator bean) {
//        CONTEXT.put(bean.getType(), bean);
//        log.debug(StringFormatter.inDramaFormat("CredentialValidatorFactory"));
//        log.debug("CredentialValidatorFactory Registered: beanName={}, type={}", bean.getClass()
//                .getSimpleName(), bean.getType().name());
    }

    public static EdsInstanceProviderDelegate<IExternalDataSourceInstance, ?> buildDelegate(IExternalDataSourceInstance instance) {
        return EdsInstanceProviderDelegate.builder()
                .instance(instance)
                .build();
    }

}
