package com.baiyi.cratos.eds.dingtalk.repo;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkDepartmentModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkTokenModel;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkDepartmentParam;
import com.baiyi.cratos.eds.dingtalk.repo.base.BaseDingtalkToken;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.SHORT_TERM;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午11:11
 * @Version 1.0
 */
@Slf4j
@Component
public class DingtalkDepartmentRepo extends BaseDingtalkToken {

    public DingtalkDepartmentRepo(RedisUtil redisUtil, DingtalkService dingtalkService) {
        super(redisUtil, dingtalkService);
    }

    @Cacheable(cacheNames = SHORT_TERM, key = "'V3:DINGTALK:DEPT:LISTSUBID:CORPID:'+ #dingtalk.corpId + ':DEPTID:' + #listSubDepartmentId.deptId", unless = "#result == null")
    public DingtalkDepartmentModel.DepartmentSubIdResult listSubId(EdsConfigs.Dingtalk dingtalk,
                                                                   DingtalkDepartmentParam.ListSubDepartmentId listSubDepartmentId) {
        DingtalkTokenModel.TokenResult tokenResult = getToken(dingtalk);
        return dingtalkService.listSubId(tokenResult.getAccessToken(), listSubDepartmentId);
    }

    @Cacheable(cacheNames = SHORT_TERM, key = "'V3:DINGTALK:DEPT:GET:CORPID:'+ #dingtalk.corpId + ':DEPTID:' + #getDepartment.deptId", unless = "#result == null")
    public DingtalkDepartmentModel.GetDepartmentResult get(EdsConfigs.Dingtalk dingtalk,
                                                           DingtalkDepartmentParam.GetDepartment getDepartment) {
        DingtalkTokenModel.TokenResult tokenResult = getToken(dingtalk);
        return dingtalkService.get(tokenResult.getAccessToken(), getDepartment);
    }

}
