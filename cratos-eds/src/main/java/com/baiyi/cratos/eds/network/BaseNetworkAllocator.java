package com.baiyi.cratos.eds.network;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.network.model.NetworkModel;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/9 16:15
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public abstract class BaseNetworkAllocator<Config extends HasEdsConfig, Subnet> implements NetworkAllocator {

    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final EdsInstanceService instanceService;
    private final EdsAssetService assetService;

    protected EdsInstanceProviderHolder<Config, Subnet> getEdsInstanceProviderHolder(int instanceId) {
        return (EdsInstanceProviderHolder<Config, Subnet>) edsProviderHolderFactory.createHolder(
                instanceId, getAssetType());
    }

    @Override
    public List<NetworkModel.Allocation> queryNetworkAllocations(int instanceId) {
        EdsInstance instance = instanceService.getById(instanceId);
        if (instance == null) {
            return List.of();
        }
        List<EdsAsset> assets = queryNetworkAssets(instance);
        if (CollectionUtils.isEmpty(assets)) {
            return List.of();
        }
        EdsInstanceProviderHolder<Config, Subnet> instanceProviderHolder = getEdsInstanceProviderHolder(instanceId);
        return assets.stream()
                .map(e -> {
                    Subnet subnet = instanceProviderHolder.getProvider()
                            .loadAsset(e.getOriginalModel());
                    return toAllocation(instance, e, subnet);
                })
                .toList();
    }

    abstract protected NetworkModel.Allocation toAllocation(EdsInstance instance, EdsAsset asset, Subnet subnet);

    protected List<EdsAsset> queryNetworkAssets(EdsInstance instance) {
        return assetService.queryInstanceAssets(instance.getId(), getAssetType());
    }

}
