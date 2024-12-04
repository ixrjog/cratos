package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.view.base.OptionsVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 14:42
 * &#064;Version 1.0
 */
public interface ApplicationResourceFacade {

    void scan(String applicationName);

    void scanAll();

    void deleteById(int id);

    OptionsVO.Options getNamespaceOptions();

    void deleteByBusiness(BaseBusiness.HasBusiness byBusiness);

}
