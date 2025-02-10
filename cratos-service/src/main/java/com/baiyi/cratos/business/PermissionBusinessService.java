package com.baiyi.cratos.business;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.SupportBusinessService;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 11:14
 * &#064;Version 1.0
 */
public interface PermissionBusinessService<T> extends SupportBusinessService, InitializingBean {

    DataTable<PermissionBusinessVO.PermissionBusiness> queryUserPermissionBusinessPage(
            UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery);

    @SuppressWarnings("unchecked")
    default PermissionBusinessVO.PermissionBusiness getByBusinessId(int businessId) {
        if (this instanceof BaseService<?, ?> baseService) {
            return toPermissionBusiness((T) baseService.getById(businessId));
        }
        return null;
    }

    default PermissionBusinessVO.PermissionBusiness getByBusinessName(String name) {
        return null;
    }

    default PermissionBusinessVO.PermissionBusiness getByBusinessPermission(
            UserPermissionBusinessParam.BusinessPermission businessPermission) {
        if (BusinessTypeEnum.TAG_GROUP.name()
                .equals(getBusinessType())) {
            return getByBusinessName(businessPermission.getName());
        }
        return getByBusinessId(businessPermission.getBusinessId());
    }

    PermissionBusinessVO.PermissionBusiness toPermissionBusiness(T recode);

    default void afterPropertiesSet() {
        PermissionBusinessServiceFactory.register(this);
    }

}
