package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.mapper.ApplicationResourceMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 13:35
 * &#064;Version 1.0
 */
public interface ApplicationResourceService extends BaseUniqueKeyService<ApplicationResource, ApplicationResourceMapper> {

    List<ApplicationResource> queryByApplicationName(String applicationName);

}