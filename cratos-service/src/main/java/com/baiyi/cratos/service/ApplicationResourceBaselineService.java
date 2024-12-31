package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.param.http.application.ApplicationResourceBaselineParam;
import com.baiyi.cratos.mapper.ApplicationResourceBaselineMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 10:00
 * &#064;Version 1.0
 */
public interface ApplicationResourceBaselineService extends BaseUniqueKeyService<ApplicationResourceBaseline, ApplicationResourceBaselineMapper> {

    DataTable<ApplicationResourceBaseline> queryApplicationResourceBaselinePage(
            ApplicationResourceBaselineParam.ApplicationResourceBaselinePageQuery pageQuery);

}
