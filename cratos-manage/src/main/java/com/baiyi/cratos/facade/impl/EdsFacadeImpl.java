package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsConfigParam;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.wrapper.EdsConfigWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:07
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class EdsFacadeImpl implements EdsFacade {

    private final EdsInstanceService edsInstanceService;

    private final EdsInstanceWrapper edsInstanceWrapper;

    private final EdsConfigService edsConfigService;

    private final EdsConfigWrapper edsConfigWrapper;

    @Override
    public DataTable<EdsInstanceVO.EdsInstance> queryEdsInstancePage(EdsInstanceParam.EdsInstancePageQuery pageQuery) {
        DataTable<EdsInstance> table = edsInstanceService.queryEdsInstancePage(pageQuery);
        return edsInstanceWrapper.wrapToTarget(table);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void registerEdsInstance(EdsInstanceParam.RegisterEdsInstance registerEdsInstance) {
        EdsInstance edsInstance = registerEdsInstance.toTarget();
        // 校验配置文件是否被占用

        //
    }

    @Override
    public DataTable<EdsConfigVO.EdsConfig> queryEdsConfigPage(EdsConfigParam.EdsConfigPageQuery pageQuery){
        DataTable<EdsConfig> table = edsConfigService.queryEdsConfigPage(pageQuery);
        return edsConfigWrapper.wrapToTarget(table);
    }

}
