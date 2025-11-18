package com.baiyi.cratos.eds.opscloud.service;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.eds.opscloud.model.OcApplicationVO;
import com.baiyi.cratos.eds.opscloud.model.OcLeoVO;
import com.baiyi.cratos.eds.opscloud.model.OcUserVO;
import com.baiyi.cratos.eds.opscloud.param.OcApplicationParam;
import com.baiyi.cratos.eds.opscloud.param.OcLeoParam;
import com.baiyi.cratos.eds.opscloud.param.OcUserParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 10:54
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface OpscloudService {

    @PostExchange("/api/application/page/query")
    HttpResult<DataTable<OcApplicationVO.Application>> queryApplicationPage(
            @RequestHeader("AccessToken") String accessToken,
            @RequestBody OcApplicationParam.ApplicationPageQuery pageQuery);

    @PostExchange("/api/user/page/query")
    HttpResult<DataTable<OcUserVO.User>> queryUserPage(@RequestHeader("AccessToken") String accessToken,
                                                       @RequestBody OcUserParam.UserPageQuery pageQuery);

    @PostExchange("/api/user/business/permission/query")
    HttpResult<DataTable<OcApplicationVO.Application>> queryUserApplicationPermissionPage(
            @RequestHeader("AccessToken") String accessToken,
            @RequestBody OcUserParam.UserBusinessPermissionPageQuery pageQuery);

    @PostExchange("/api/leo/build/image/version/query")
    HttpResult<OcLeoVO.BuildImage> queryBuildImageVersion(@RequestHeader("AccessToken") String accessToken,
                                                          @RequestBody OcLeoParam.QueryBuildImageVersion queryBuildImageVersion);


    @PostExchange("/api/user/add")
    HttpResult<OcUserVO.User> addUser(@RequestHeader("AccessToken") String accessToken,
                                                 @RequestBody OcUserParam.AddUser addUser);

}
