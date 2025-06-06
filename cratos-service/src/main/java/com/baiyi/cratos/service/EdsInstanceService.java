package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.mapper.EdsInstanceMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/5 16:51
 * @Version 1.0
 */
public interface EdsInstanceService extends BaseUniqueKeyService<EdsInstance, EdsInstanceMapper>, BaseValidService<EdsInstance, EdsInstanceMapper>, SupportBusinessService {

    DataTable<EdsInstance> queryEdsInstancePage(EdsInstanceParam.InstancePageQueryParam param);

    int selectCountByConfigId(Integer configId);

    List<EdsInstance> queryValidEdsInstanceByType(String edsType);

    List<EdsInstance> queryEdsInstanceByType(String edsType);

    EdsInstance getByName(String instanceName);

}
