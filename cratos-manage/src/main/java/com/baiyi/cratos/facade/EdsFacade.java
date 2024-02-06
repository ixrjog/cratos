package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.eds.EdsConfigParam;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:07
 * @Version 1.0
 */
public interface EdsFacade {

    DataTable<EdsInstanceVO.EdsInstance> queryEdsInstancePage(EdsInstanceParam.EdsInstancePageQuery pageQuery);

    void registerEdsInstance(EdsInstanceParam.RegisterEdsInstance registerEdsInstance);

    DataTable<EdsConfigVO.EdsConfig> queryEdsConfigPage(EdsConfigParam.EdsConfigPageQuery pageQuery);

}
