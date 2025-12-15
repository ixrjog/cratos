package com.baiyi.cratos.eds.computer.context;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 11:50
 * &#064;Version 1.0
 */
@Data
@Builder
public class CloudComputerContext<Config extends HasEdsConfig> {

    private EdsAsset asset;
    private EdsInstance edsInstance;
    private EdsConfig edsConfig;
    private Config config;

    public String getComputerInstanceId() {
        return getAsset().getAssetId();
    }

    public String getRegionId() {
        return getAsset().getRegion();
    }

}
