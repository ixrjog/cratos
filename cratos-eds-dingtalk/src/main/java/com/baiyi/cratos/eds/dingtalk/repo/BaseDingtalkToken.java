package com.baiyi.cratos.eds.dingtalk.repo;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkToken;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkTokenService;

import static com.baiyi.cratos.common.constants.CacheKeyConstants.DINGTALK_TOKEN;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午11:24
 * @Version 1.0
 */
public abstract class BaseDingtalkToken {

    private final RedisUtil redisUtil;

    private final DingtalkTokenService dingtalkTokenService;

    public BaseDingtalkToken(RedisUtil redisUtil, DingtalkTokenService dingtalkTokenService) {
        this.redisUtil = redisUtil;
        this.dingtalkTokenService = dingtalkTokenService;
    }

    private String generateCacheKey(String name) {
        return StringFormatter.format(DINGTALK_TOKEN, name);
    }

    protected DingtalkToken.TokenResult getToken(EdsDingtalkConfigModel.Dingtalk dingtalk) {
        String key = generateCacheKey(dingtalk.getApp()
                .getName());
        if (redisUtil.hasKey(key)) {
            return (DingtalkToken.TokenResult) redisUtil.get(key);
        }
        DingtalkToken.TokenResult token = dingtalkTokenService.getToken(dingtalk.getApp()
                .getAppKey(), dingtalk.getApp()
                .getAppSecret());
        redisUtil.set(key, token, token.getExpiresIn());
        return token;
    }

}
