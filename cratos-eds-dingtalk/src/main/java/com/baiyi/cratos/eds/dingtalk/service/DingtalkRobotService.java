package com.baiyi.cratos.eds.dingtalk.service;

import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobot;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 上午10:23
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json")
public interface DingtalkRobotService {

    @PostExchange("/robot/send?access_token={token}")
    Object send(@PathVariable String token, @RequestBody DingtalkRobot.Msg msg);

}
