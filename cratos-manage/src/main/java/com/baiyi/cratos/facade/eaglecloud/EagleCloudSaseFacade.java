package com.baiyi.cratos.facade.eaglecloud;

import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:04
 * &#064;Version 1.0
 */
public interface EagleCloudSaseFacade {

    void consumeEvent(EagleCloudEventParam.SaseHook saseHook, String authorization);

}
