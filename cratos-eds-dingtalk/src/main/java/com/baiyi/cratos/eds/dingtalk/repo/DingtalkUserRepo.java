package com.baiyi.cratos.eds.dingtalk.repo;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkResponse;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkToken;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkUser;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkUserParam;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkTokenService;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkUserService;
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

    private final DingtalkUserService dingtalkUserService;

    public DingtalkUserRepo(RedisUtil redisUtil, DingtalkTokenService dingtalkTokenService,
                            DingtalkUserService dingtalkUserService) {
        super(redisUtil, dingtalkTokenService);
        this.dingtalkUserService = dingtalkUserService;
    }

    public DingtalkUser.UserResult list(EdsDingtalkConfigModel.Dingtalk dingtalk,
                                        DingtalkUserParam.QueryUserPage queryUserPage) {
        DingtalkToken.TokenResult tokenResponse = getToken(dingtalk);
        return dingtalkUserService.list(tokenResponse.getAccessToken(), queryUserPage);
    }

    public List<DingtalkUser.User> queryUserByDeptId(EdsDingtalkConfigModel.Dingtalk dingtalk, Long deptId) {
        List<DingtalkUser.User> users = Lists.newArrayList();
        DingtalkUserParam.QueryUserPage queryUserPage = DingtalkUserParam.QueryUserPage.builder()
                .deptId(deptId)
                .build();
        while (true) {
            DingtalkToken.TokenResult tokenResponse = getToken(dingtalk);
            DingtalkUser.UserResult userResult = list(dingtalk, queryUserPage);
            Optional<List<DingtalkUser.User>> optionalUsers = Optional.ofNullable(userResult)
                    .map(DingtalkUser.UserResult::getResult)
                    .map(DingtalkUser.Result::getList);
            if (optionalUsers.isPresent()) {
                users.addAll(optionalUsers.get());
                if (Optional.of(userResult)
                        .map(DingtalkUser.UserResult::getResult)
                        .map(DingtalkResponse.Result::getHasMore)
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
