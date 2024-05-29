package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.AssetToBusinessWrapperFactory;
import com.baiyi.cratos.eds.business.IAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.baiyi.cratos.wrapper.builder.ResourceCountBuilder;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.baiyi.cratos.domain.enums.BusinessTypeEnum.EDS_ASSET_INDEX;

/**
 * @Author baiyi
 * @Date 2024/2/28 15:14
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsAssetWrapper extends BaseDataTableConverter<EdsAssetVO.Asset, EdsAsset> implements IBaseWrapper<EdsAssetVO.Asset> {

    private final EdsInstanceProviderHolderBuilder holderBuilder;

    private final EdsAssetIndexService edsAssetIndexService;

    public static final boolean SKIP_LOAD_ASSET = true;

    @Override
    public void wrap(EdsAssetVO.Asset asset) {
        EdsInstanceProviderHolder<?, ?> edsInstanceProviderHolder = holderBuilder.newHolder(asset.getInstanceId(), asset.getAssetType());
        // TODO 是否要序列化对象？
        asset.setOriginalAsset(edsInstanceProviderHolder.getProvider()
                .assetLoadAs(asset.getOriginalModel()));
        // ToBusiness
        IAssetToBusinessWrapper<?> assetToBusinessWrapper = AssetToBusinessWrapperFactory.getProvider(asset.getAssetType());
        if (assetToBusinessWrapper != null) {
            assetToBusinessWrapper.wrap(asset);
        }

        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(makeResourceCountForAssetIndex(asset))
                .build();
        asset.setResourceCount(resourceCount);
    }

    public void wrap(EdsAssetVO.Asset asset, boolean skipLoadAsset) {
        EdsInstanceProviderHolder<?, ?> providerHolder = holderBuilder.newHolder(asset.getInstanceId(), asset.getAssetType());
        if (!skipLoadAsset) {
            asset.setOriginalAsset(providerHolder.getProvider()
                    .assetLoadAs(asset.getOriginalModel()));
        }
        // ToBusiness
        IAssetToBusinessWrapper<?> assetToBusinessWrapper = AssetToBusinessWrapperFactory.getProvider(asset.getAssetType());
        if (assetToBusinessWrapper != null) {
            assetToBusinessWrapper.wrap(asset);
        }

        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(makeResourceCountForAssetIndex(asset))
                .build();
        asset.setResourceCount(resourceCount);
    }

    private Map<String, Integer> makeResourceCountForAssetIndex(EdsAssetVO.Asset asset) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(EDS_ASSET_INDEX.name(), edsAssetIndexService.selectCountByAssetId(asset.getId()));
        return resourceCount;
    }

}
