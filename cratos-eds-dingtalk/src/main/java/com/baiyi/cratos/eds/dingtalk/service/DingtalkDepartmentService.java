package com.baiyi.cratos.eds.dingtalk.service;

import com.baiyi.cratos.eds.dingtalk.model.DingtalkDepartment;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkDepartmentParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午10:21
 * @Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json;charset=utf-8")
public interface DingtalkDepartmentService {

    @PostExchange("/topapi/v2/department/listsubid?access_token={accessToken}")
    DingtalkDepartment.DepartmentSubIdResult listSubId(@PathVariable String accessToken, @RequestBody DingtalkDepartmentParam.ListSubDepartmentId listSubDepartmentId);

    @PostExchange("/topapi/v2/department/get?access_token={accessToken}")
    DingtalkDepartment.GetDepartmentResult get(@PathVariable String accessToken, @RequestBody DingtalkDepartmentParam.GetDepartment getDepartment);

}
