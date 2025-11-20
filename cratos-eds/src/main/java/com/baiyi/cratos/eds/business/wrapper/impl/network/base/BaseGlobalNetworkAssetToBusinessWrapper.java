package com.baiyi.cratos.eds.business.wrapper.impl.network.base;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.exception.AssetToBusinessException;
import com.baiyi.cratos.eds.business.wrapper.impl.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.eds.business.wrapper.impl.network.model.MainModel;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsInstanceService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 11:32
 * &#064;Version 1.0
 */
public abstract class BaseGlobalNetworkAssetToBusinessWrapper<B> extends BaseAssetToBusinessWrapper<GlobalNetworkSubnet, B> {

    private final EdsInstanceService edsInstanceService;
    private final EdsAssetIndexService edsAssetIndexService;

    public BaseGlobalNetworkAssetToBusinessWrapper(BusinessAssetBoundService businessAssetBoundService,
                                                   EdsInstanceService edsInstanceService,
                                                   EdsAssetIndexService edsAssetIndexService) {
        super(businessAssetBoundService);
        this.edsInstanceService = edsInstanceService;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    protected MainModel.Main getMain(EdsAssetVO.Asset asset) {
        EdsInstance instance = edsInstanceService.getById(asset.getInstanceId());
        if (instance == null) {
            throw new AssetToBusinessException("The data source instance does not exist. instanceId={}",
                    asset.getInstanceId());
        }
        return MainModel.Main.builder()
                .id(instance.getId())
                .name(instance.getInstanceName())
                .type(instance.getEdsType())
                .build();
    }

    protected EdsAssetIndex getAssetIndex(EdsAssetVO.Asset asset, String name) {
        EdsAssetIndex uniqueKey = EdsAssetIndex.builder()
                .instanceId(asset.getInstanceId())
                .assetId(asset.getId())
                .name(name)
                .build();
        return edsAssetIndexService.getByUniqueKey(uniqueKey);
    }

    /**
     * 获取可用IP总数
     *
     * @param asset
     * @return
     */
    protected int getResourceTotal(EdsAssetVO.Asset asset) {
        return -1;
    }

    abstract protected String getCidrBlock(EdsAssetVO.Asset asset);

    @Override
    protected GlobalNetworkSubnet toTarget(EdsAssetVO.Asset asset) {
        //   AliyunVirtualSwitch.Switch model = getAssetModel(asset);
        MainModel.Main main = getMain(asset);
        return GlobalNetworkSubnet.builder()
                .name(asset.getName())
                .mainName(main.getName())
                .mainType(main.getType())
                .mainId(main.getId())
                .subnetKey(asset.getAssetId())
                .region(asset.getRegion())
                .zone(asset.getZone())
                .cidrBlock(getCidrBlock(asset))
                .resourceTotal(getResourceTotal(asset))
                .valid(asset.getValid())
                .comment(asset.getDescription())
                .build();
    }

}
