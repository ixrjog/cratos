package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.param.http.eds.EdsConfigParam;
import com.baiyi.cratos.mapper.EdsConfigMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:55
 * @Version 1.0
 */
public interface EdsConfigService extends BaseUniqueKeyService<EdsConfig, EdsConfigMapper>, BaseValidService<EdsConfig, EdsConfigMapper>, SupportBusinessService {

    DataTable<EdsConfig> queryEdsConfigPage(EdsConfigParam.EdsConfigPageQuery pageQuery);

    List<EdsConfig> queryByEdsType(String edsType);

}
