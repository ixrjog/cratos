package com.baiyi.cratos.service.access;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.view.access.AccessControlVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/13 16:09
 * &#064;Version 1.0
 */
public interface AccessControlFacade {

    AccessControlVO.AccessControl generateAccessControl(BaseBusiness.HasBusiness hasBusiness, String namespace);

    AccessControlVO.HasAccessControl invoke(AccessControlVO.HasAccessControl hasAccessControl);

}