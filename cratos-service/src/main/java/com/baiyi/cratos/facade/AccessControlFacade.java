package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.view.access.AccessControlVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/13 16:09
 * &#064;Version 1.0
 */
@SuppressWarnings("rawtypes")
public interface AccessControlFacade {

    AccessControlVO.AccessControl generateAccessControl(BaseBusiness.HasBusiness hasBusiness, String namespace);

    AccessControlVO.AccessControl generateAccessControl(String username, BaseBusiness.HasBusiness hasBusiness,
                                                        String namespace);

    AccessControlVO.HasAccessControl invoke(AccessControlVO.HasAccessControl hasAccessControl);

}
