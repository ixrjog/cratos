package com.baiyi.cratos.eds.zabbix.provider.version;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.version.IEdsInstanceVersionProvider;
import com.baiyi.cratos.eds.zabbix.util.ZbxInfoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 09:49
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ZABBIX)
public class EdsZbxInstanceVersionProvider implements IEdsInstanceVersionProvider<EdsZabbixConfigModel.Zabbix> {

    @Override
    public String getVersion(ExternalDataSourceInstance<EdsZabbixConfigModel.Zabbix> instance) {
        return ZbxInfoUtils.getVersion(instance.getEdsConfigModel());
    }

}
