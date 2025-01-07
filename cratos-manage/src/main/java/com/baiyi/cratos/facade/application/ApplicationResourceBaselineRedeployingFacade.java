package com.baiyi.cratos.facade.application;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/7 10:50
 * &#064;Version 1.0
 */
public interface ApplicationResourceBaselineRedeployingFacade {

    boolean isRedeploying(int baselineId);

    void deploying(int baselineId);

}
