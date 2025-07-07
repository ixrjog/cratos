package com.baiyi.cratos.facade.application.resource.scanner;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/18 10:22
 * &#064;Version 1.0
 */
@Slf4j
public class ResourceScannerFactory {

    public enum Type {
        KUBERNETES_RESOURCE,
        REPOSITORY_RESOURCE
    }

    private ResourceScannerFactory() {
    }

    private static final Map<ResourceScannerFactory.Type, ResourceScanner> CONTEXT = new ConcurrentHashMap<>();

    public static void register(ResourceScanner bean) {
        CONTEXT.put(bean.getType(), bean);
        log.debug(StringFormatter.inDramaFormat("ResourceScannerFactory"));
        log.debug("ResourceScannerFactory Registered: beanName={}, type={}", bean.getClass()
                .getSimpleName(), bean.getType()
                .name());
    }

    private static ResourceScanner getScanner(ResourceScannerFactory.Type type) {
        if (CONTEXT.containsKey(type)) {
            return CONTEXT.get(type);
        }
        return null;
    }

    public static void scanAndBindAssets(Application application, ApplicationConfigModel.Config config) {
        if (CollectionUtils.isEmpty(CONTEXT)) {
            return;
        }
        CONTEXT.forEach((k, v) -> v.scanAndBindAssets(application, config));
    }

}
