package com.baiyi.cratos.eds.dingtalk.repo;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkResponseModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkTokenModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkUserModel;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkUserParam;
import com.baiyi.cratos.eds.dingtalk.repo.base.BaseDingtalkToken;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/5/6 下午2:18
 * @Version 1.0
 */
@Slf4j
@Component
public class DingtalkUserRepo extends BaseDingtalkToken {


    public DingtalkUserRepo(RedisUtil redisUtil, DingtalkService dingtalkService) {
        super(redisUtil, dingtalkService);
    }

    public DingtalkUserModel.UserResult list(EdsDingtalkConfigModel.Dingtalk dingtalk,
                                             DingtalkUserParam.QueryUserPage queryUserPage) {
        DingtalkTokenModel.TokenResult tokenResponse = getToken(dingtalk);
        return dingtalkService.list(tokenResponse.getAccessToken(), queryUserPage);
    }

    public List<DingtalkUserModel.User> queryUserByDeptId(EdsDingtalkConfigModel.Dingtalk dingtalk, Long deptId) {
        List<DingtalkUserModel.User> users = Lists.newArrayList();
        DingtalkUserParam.QueryUserPage queryUserPage = DingtalkUserParam.QueryUserPage.builder()
                .deptId(deptId)
                .build();
        while (true) {
            DingtalkTokenModel.TokenResult tokenResponse = getToken(dingtalk);
            DingtalkUserModel.UserResult userResult = list(dingtalk, queryUserPage);
            Optional<List<DingtalkUserModel.User>> optionalUsers = Optional.ofNullable(userResult)
                    .map(DingtalkUserModel.UserResult::getResult)
                    .map(DingtalkUserModel.Result::getList);
            if (optionalUsers.isPresent()) {
                users.addAll(optionalUsers.get());
                if (Optional.of(userResult)
                        .map(DingtalkUserModel.UserResult::getResult)
                        .map(DingtalkResponseModel.Result::getHasMore)
                        .orElse(false)) {
                    queryUserPage.setCursor(userResult.getResult()
                            .getNextCursor());
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return users;
    }

}
