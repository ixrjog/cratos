package com.baiyi.cratos.eds.alimail.service;

import com.baiyi.cratos.eds.alimail.model.AlimailDepartment;
import com.baiyi.cratos.eds.alimail.model.AlimailToken;
import com.baiyi.cratos.eds.alimail.model.AlimailUser;
import com.baiyi.cratos.eds.alimail.param.AlimailTokenParam;
import com.baiyi.cratos.eds.alimail.param.AlimailUserParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:22
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json")
public interface AlimailService {

    @GetExchange("/oauth2/v2.0/token")
    AlimailToken.Token getToken(@RequestBody AlimailTokenParam.GetToken getTokenParam);

    @GetExchange("/v2/departments/{id}")
    AlimailToken.Token departments(@RequestBody AlimailTokenParam.GetToken getTokenParam);

    /**
     * https://alimail-cn.aliyuncs.com/v2/departments/{id}/departments
     *
     * @param authorization
     * @param id
     * @param limit
     * @param offset
     * @return
     */
    @GetExchange("/v2/departments/{id}/departments?limit={limit}&offset={offset}")
    AlimailDepartment.ListSubDepartmentsResult listSubDepartments(@RequestHeader("Authorization") String authorization,
                                                                  @PathVariable String id, @PathVariable Integer limit,
                                                                  @PathVariable Integer offset);

    /**
     * https://mailhelp.aliyun.com/openapi/index.html#/operations/alimailpb_ud_DepartmentService_ListUsersOfDepartment
     *
     * @param authorization
     * @param id
     * @param limit
     * @param offset
     * @return
     */
    @GetExchange("/v2/departments/{id}/users?limit={limit}&offset={offset}")
    AlimailUser.ListUsersOfDepartmentResult listUsersOfDepartment(@RequestHeader("Authorization") String authorization,
                                                                  @PathVariable String id, @PathVariable Integer limit,
                                                                  @PathVariable Integer offset);


    @PatchExchange("/v2/users/{id}")
    void freezeUser(@RequestHeader("Authorization") String authorization, @PathVariable String id,
                    @RequestBody AlimailUserParam.UpdateUser freezeUser);

}
