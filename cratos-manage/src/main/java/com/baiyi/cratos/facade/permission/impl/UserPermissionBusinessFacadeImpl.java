package com.baiyi.cratos.facade.permission.impl;

import com.baiyi.cratos.business.PermissionBusinessServiceFactory;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.facade.permission.UserPermissionBusinessFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 10:11
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPermissionBusinessFacadeImpl implements UserPermissionBusinessFacade {

    @Override
    public DataTable<PermissionBusinessVO.PermissionBusiness> queryUserPermissionBusinessPage(
            UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery) {
        PermissionBusinessServiceFactory.trySupport(pageQuery.getBusinessType());
        return PermissionBusinessServiceFactory.getService(pageQuery.getBusinessType())
                .queryUserPermissionBusinessPage(pageQuery);
    }

}
