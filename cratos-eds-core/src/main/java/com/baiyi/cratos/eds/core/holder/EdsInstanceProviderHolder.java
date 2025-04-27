package com.baiyi.cratos.eds.core.holder;


import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/2/23 17:43
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdsInstanceProviderHolder<Config extends IEdsConfigModel, A> {

    @Schema(description = "Eds instance")
    private ExternalDataSourceInstance<Config> instance;

    @Schema(description = "Eds provider")
    private EdsInstanceAssetProvider<Config, A> provider;

    @Schema(description = "导入所有资产")
    public void importAssets() {
        provider.importAssets(instance);
    }

    @Schema(description = "导入单个资产")
    public EdsAsset importAsset(A asset) {
        return provider.importAsset(instance, asset);
    }

}
