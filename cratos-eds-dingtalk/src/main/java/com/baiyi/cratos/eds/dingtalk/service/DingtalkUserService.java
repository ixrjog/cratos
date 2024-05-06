package com.baiyi.cratos.eds.dingtalk.service;

import com.baiyi.cratos.eds.dingtalk.model.DingtalkUser;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkUserParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @Author baiyi
 * @Date 2024/5/6 下午2:15
 * @Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json;charset=utf-8")
public interface DingtalkUserService {

    @PostExchange("/topapi/v2/user/list?access_token={accessToken}")
    DingtalkUser.UserResult list(@PathVariable String accessToken,
                                 @RequestBody DingtalkUserParam.QueryUserPage queryUserPage);

}
