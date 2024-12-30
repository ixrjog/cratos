package com.baiyi.cratos.facade.application.baseline.factory;

import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.facade.application.baseline.ContainerBaselineMemberProcessor;
import io.fabric8.kubernetes.api.model.Container;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 13:49
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaselineMemberProcessorFactory {

    private static final Map<ResourceBaselineTypeEnum, ContainerBaselineMemberProcessor> CONTEXT = new ConcurrentHashMap<>();

    public static void register(ContainerBaselineMemberProcessor bean) {
        CONTEXT.put(bean.getType(), bean);
    }

    public static void saveMemberAll(ApplicationResourceBaseline baseline, Container container) {
        CONTEXT.forEach((key, bean) -> bean.saveMember(baseline, container));
    }

}
