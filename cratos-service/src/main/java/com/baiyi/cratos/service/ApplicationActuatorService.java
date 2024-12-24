package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApplicationActuator;
import com.baiyi.cratos.domain.param.http.application.ApplicationActuatorParam;
import com.baiyi.cratos.mapper.ApplicationActuatorMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/23 15:48
 * &#064;Version 1.0
 */
public interface ApplicationActuatorService extends BaseUniqueKeyService<ApplicationActuator, ApplicationActuatorMapper> {

    DataTable<ApplicationActuator> queryApplicationActuatorPage(
            ApplicationActuatorParam.ApplicationActuatorPageQuery pageQuery);

}
