package com.baiyi.cratos.eds.core.holder;

import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.version.BaseEdsInstanceVersionProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 15:35
 * &#064;Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdsInstanceVersionProviderHolder<C extends HasEdsConfig> {

    @Schema(description = "Eds instance")
    private ExternalDataSourceInstance<C> instance;

    @Schema(description = "Eds instance version provider")
    private BaseEdsInstanceVersionProvider<C> provider;

    public String version() {
        return provider.getVersion(instance);
    }

}
