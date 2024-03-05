package com.baiyi.cratos.eds.core.delegate;


import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.support.EdsInstanceProvider;
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
public class EdsInstanceProviderDelegate<C extends IEdsConfigModel, A> {

    @Schema(description = "Eds Instance")
    private ExternalDataSourceInstance<C> instance;

    @Schema(description = "Eds Provider")
    private EdsInstanceProvider<C, A> provider;

    public void importAssets() {
        provider.importAssets(instance);
    }

    public void pushAsset(A asset) {
        provider.importAsset(instance, asset);
    }

}
