package com.baiyi.cratos.eds.dingtalk.repo;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
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
        boolean hasMore = true;
        while (hasMore) {
            DingtalkUserModel.UserResult userResult = list(dingtalk, queryUserPage);
            DingtalkUserModel.Result result = Optional.ofNullable(userResult)
                    .map(DingtalkUserModel.UserResult::getResult)
                    .orElse(null);
            if (result != null && result.getList() != null) {
                users.addAll(result.getList());
                hasMore = Boolean.TRUE.equals(result.getHasMore());
                if (hasMore) {
                    queryUserPage.setCursor(result.getNextCursor());
                }
            } else {
                break;
            }
        }
        return users;
    }

    public DingtalkUserModel.GetUser getUser(EdsDingtalkConfigModel.Dingtalk dingtalk, String userId) {
        DingtalkTokenModel.TokenResult tokenResponse = getToken(dingtalk);
        DingtalkUserParam.GetUser param = DingtalkUserParam.GetUser.builder()
                .userId(userId)
                .build();
        DingtalkUserModel.GetUserResult getUserResult = dingtalkService.getUser(tokenResponse.getAccessToken(), param);
        return getUserResult.getResult();
    }

}
