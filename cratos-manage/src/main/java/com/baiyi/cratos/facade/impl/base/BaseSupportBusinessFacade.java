package com.baiyi.cratos.facade.impl.base;

import com.baiyi.cratos.common.exception.BusinessException;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.SupportBusinessService;
import com.baiyi.cratos.service.factory.SupportBusinessServiceFactory;

/**
 * @Author baiyi
 * @Date 2024/2/19 10:56
 * @Version 1.0
 */
public abstract class BaseSupportBusinessFacade<T extends BaseBusiness.IBusiness> {

    protected BaseService<?, ?> getBusinessService(BaseBusiness.IBusiness business) {
        SupportBusinessService supportBusinessService = SupportBusinessServiceFactory.getService(business.getBusinessType());
        if (supportBusinessService == null) {
            throw new BusinessException("BusinessType {} does not support business factory.", business.getBusinessType());
        }
        if (supportBusinessService instanceof BaseService<?, ?> baseService) {
            return baseService;
        } else {
            throw new BusinessException("SupportBusinessService does not BaseService.");
        }
    }

    protected boolean isSupported(BaseBusiness.IBusiness business) {
        return SupportBusinessServiceFactory.getService(business.getBusinessType()) != null;
    }

    protected void trySupported(BaseBusiness.IBusiness business) {
        if (SupportBusinessServiceFactory.getService(business.getBusinessType()) == null) {
            throw new BusinessException("BusinessType {} does not support business factory.", business.getBusinessType());
        }
    }

}
