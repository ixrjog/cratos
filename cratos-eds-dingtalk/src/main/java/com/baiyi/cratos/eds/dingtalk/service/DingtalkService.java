package com.baiyi.cratos.eds.dingtalk.service;

import com.baiyi.cratos.eds.dingtalk.model.*;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkDepartmentParam;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkMessageParam;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkUserParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午10:21
 * @Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json;charset=utf-8")
public interface DingtalkService {

    // Token
    @GetExchange("/gettoken?appkey={appkey}&appsecret={appsecret}")
    DingtalkTokenModel.TokenResult getToken(@PathVariable String appkey, @PathVariable String appsecret);

    // Department
    @PostExchange("/topapi/v2/department/listsubid?access_token={accessToken}")
    DingtalkDepartmentModel.DepartmentSubIdResult listSubId(@PathVariable String accessToken, @RequestBody DingtalkDepartmentParam.ListSubDepartmentId listSubDepartmentId);

    @PostExchange("/topapi/v2/department/get?access_token={accessToken}")
    DingtalkDepartmentModel.GetDepartmentResult get(@PathVariable String accessToken, @RequestBody DingtalkDepartmentParam.GetDepartment getDepartment);

   // Robot
    @PostExchange("/robot/send?access_token={token}")
    Object send(@PathVariable String token, @RequestBody DingtalkRobotModel.Msg msg);

    // User
    @PostExchange("/topapi/v2/user/list?access_token={accessToken}")
    DingtalkUserModel.UserResult list(@PathVariable String accessToken,
                                      @RequestBody DingtalkUserParam.QueryUserPage queryUserPage);

    /**
     * https://developers.dingtalk.com/document/app/asynchronous-sending-of-enterprise-session-messages
     * @param accessToken
     * @param asyncSendMessage
     * @return
     */
    @PostExchange("/topapi/message/corpconversation/asyncsend_v2?access_token={accessToken}")
    DingtalkMessageModel.AsyncSendResult asyncSend(@PathVariable String accessToken, @RequestBody DingtalkMessageParam.AsyncSendMessage asyncSendMessage);

}
