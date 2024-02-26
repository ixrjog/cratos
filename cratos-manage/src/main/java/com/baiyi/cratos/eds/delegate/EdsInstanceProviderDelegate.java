package com.baiyi.cratos.eds.delegate;

import com.baiyi.cratos.eds.IExternalDataSourceInstance;
import com.baiyi.cratos.eds.support.EdsInstanceProvider;
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
public class EdsInstanceProviderDelegate<I extends IExternalDataSourceInstance, A> {

    @Schema(description = "Eds Instance")
    private I instance;

    @Schema(description = "Eds Provider;")
    private EdsInstanceProvider<I, A> provider;

    public void importAssets() {
        provider.importAssets(instance);
    }

    public void pushAsset(A asset) {
        provider.pushAsset(instance, asset);
    }

}
