package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.application.ApplicationActuatorParam;
import com.baiyi.cratos.domain.view.application.ApplicationActuatorVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/23 15:33
 * &#064;Version 1.0
 */
public interface ApplicationActuatorFacade {

    void scanAll();

    DataTable<ApplicationActuatorVO.ApplicationActuator> queryApplicationActuatorPage(
            ApplicationActuatorParam.ApplicationActuatorPageQuery pageQuery);

}
