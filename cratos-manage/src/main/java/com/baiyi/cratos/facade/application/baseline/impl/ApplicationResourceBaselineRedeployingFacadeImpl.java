package com.baiyi.cratos.facade.application.baseline.impl;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineRedeployingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/7 10:50
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationResourceBaselineRedeployingFacadeImpl implements ApplicationResourceBaselineRedeployingFacade {

    private final RedisUtil redisUtil;
    public static final String REDEPLOYING_KEY = "APPLICATION:RESOURCE:BASELINE:IS_REDEPLOYING:BASELINE_ID:{}";

    @Override
    public boolean isRedeploying(int baselineId) {
        return redisUtil.hasKey(getKey(baselineId));
    }

    @Override
    public void deploying(int baselineId) {
        redisUtil.set(getKey(baselineId), "TRUE", 60L);
    }

    private String getKey(int baselineId) {
        return StringFormatter.format(REDEPLOYING_KEY, baselineId);
    }

}
