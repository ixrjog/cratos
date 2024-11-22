package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.mapper.ApplicationMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 11:18
 * &#064;Version 1.0
 */
public interface ApplicationService extends BaseValidService<Application, ApplicationMapper>, BaseUniqueKeyService<Application, ApplicationMapper> {

    DataTable<Application> queryApplicationPage(ApplicationParam.ApplicationPageQueryParam param);

    Application getByName(String name);

}
