package com.baiyi.cratos.business;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.service.base.SupportBusinessService;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 11:14
 * &#064;Version 1.0
 */
public interface PermissionBusinessService extends SupportBusinessService,InitializingBean {

    DataTable<PermissionBusinessVO.PermissionBusiness> queryUserPermissionBusinessPage(
            UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery);

    default void afterPropertiesSet() {
        PermissionBusinessServiceFactory.register(this);
    }

}
