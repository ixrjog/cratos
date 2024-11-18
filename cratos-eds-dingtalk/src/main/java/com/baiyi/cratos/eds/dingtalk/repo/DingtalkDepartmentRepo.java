package com.baiyi.cratos.eds.dingtalk.repo;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkDepartment;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkToken;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkDepartmentParam;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkDepartmentService;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkTokenService;
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

    private final DingtalkDepartmentService dingtalkDepartmentService;

    public DingtalkDepartmentRepo(RedisUtil redisUtil, DingtalkTokenService dingtalkTokenService,
                                  DingtalkDepartmentService dingtalkDepartmentService) {
        super(redisUtil, dingtalkTokenService);
        this.dingtalkDepartmentService = dingtalkDepartmentService;
    }

    @Cacheable(cacheNames = SHORT_TERM, key = "'DINGTALK:DEPT:LISTSUBID:CORPID:'+ #dingtalk.corpId + ':DEPTID:' + #listSubDepartmentId.deptId", unless = "#result == null")
    public DingtalkDepartment.DepartmentSubIdResult listSubId(EdsDingtalkConfigModel.Dingtalk dingtalk,
                                                              DingtalkDepartmentParam.ListSubDepartmentId listSubDepartmentId) {
        DingtalkToken.TokenResult tokenResult = getToken(dingtalk);
        // log.debug("未命中缓存: method=listSubId, deptId={}", listSubDepartmentId.getDeptId());
        return dingtalkDepartmentService.listSubId(tokenResult.getAccessToken(), listSubDepartmentId);
    }

    @Cacheable(cacheNames = SHORT_TERM, key = "'DINGTALK:DEPT:GET:CORPID:'+ #dingtalk.corpId + ':DEPTID:' + #getDepartment.deptId", unless = "#result == null")
    public DingtalkDepartment.GetDepartmentResult get(EdsDingtalkConfigModel.Dingtalk dingtalk,
                                                      DingtalkDepartmentParam.GetDepartment getDepartment) {
        DingtalkToken.TokenResult tokenResult = getToken(dingtalk);
        // log.debug("未命中缓存: method=get, deptId={}", getDepartment.getDeptId());
        return dingtalkDepartmentService.get(tokenResult.getAccessToken(), getDepartment);
    }

}
