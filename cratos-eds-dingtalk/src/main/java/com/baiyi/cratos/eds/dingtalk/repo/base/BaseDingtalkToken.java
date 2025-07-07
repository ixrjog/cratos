package com.baiyi.cratos.eds.dingtalk.repo.base;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkTokenModel;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;

import static com.baiyi.cratos.common.constants.CacheKeyConstants.DINGTALK_TOKEN;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午11:24
 * @Version 1.0
 */
public abstract class BaseDingtalkToken {

    private final RedisUtil redisUtil;
    protected final DingtalkService dingtalkService;

    public BaseDingtalkToken(RedisUtil redisUtil, DingtalkService dingtalkService) {
        this.redisUtil = redisUtil;
        this.dingtalkService = dingtalkService;
    }

    private String generateCacheKey(String name) {
        return StringFormatter.format(DINGTALK_TOKEN, name);
    }

    protected DingtalkTokenModel.TokenResult getToken(EdsDingtalkConfigModel.Dingtalk dingtalk) {
        String key = generateCacheKey(dingtalk.getApp()
                .getName());
        if (redisUtil.hasKey(key)) {
            return (DingtalkTokenModel.TokenResult) redisUtil.get(key);
        }
        DingtalkTokenModel.TokenResult token = dingtalkService.getToken(dingtalk.getApp()
                .getAppKey(), dingtalk.getApp()
                .getAppSecret());
        redisUtil.set(key, token, token.getExpiresIn());
        return token;
    }

}
