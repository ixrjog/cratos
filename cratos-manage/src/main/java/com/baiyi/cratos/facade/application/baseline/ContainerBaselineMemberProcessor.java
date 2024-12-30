package com.baiyi.cratos.facade.application.baseline;

import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import io.fabric8.kubernetes.api.model.Container;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 13:55
 * &#064;Version 1.0
 */
public interface ContainerBaselineMemberProcessor {

    ResourceBaselineTypeEnum getType();

    void saveMember(ApplicationResourceBaseline baseline, Container container);

}
