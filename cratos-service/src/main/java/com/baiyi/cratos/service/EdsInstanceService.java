package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.mapper.EdsInstanceMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessTagService;

/**
 * @Author baiyi
 * @Date 2024/2/5 16:51
 * @Version 1.0
 */
public interface EdsInstanceService extends BaseUniqueKeyService<EdsInstance>, BaseValidService<EdsInstance, EdsInstanceMapper>, SupportBusinessTagService {

    DataTable<EdsInstance> queryEdsInstancePage(EdsInstanceParam.EdsInstancePageQuery pageQuery);

}