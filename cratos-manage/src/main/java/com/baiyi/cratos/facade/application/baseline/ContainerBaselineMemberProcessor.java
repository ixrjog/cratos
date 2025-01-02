package com.baiyi.cratos.facade.application.baseline;

import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 13:55
 * &#064;Version 1.0
 */
public interface ContainerBaselineMemberProcessor {

    ResourceBaselineTypeEnum getType();

    void saveMember(ApplicationResourceBaseline baseline, Container container);

    void mergeToBaseline(ApplicationResourceBaseline baseline,ApplicationResourceBaselineMember baselineMember, Deployment deployment, Container container);

}
