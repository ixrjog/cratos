package com.baiyi.cratos.eds.core.builder;

import com.baiyi.cratos.common.util.YamlUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2024/2/28 10:15
 * @Version 1.0
 */
public class EdsAssetBuilder<C extends IEdsConfigModel, A> {

    private final EdsAsset edsAsset;

    private EdsAssetBuilder(ExternalDataSourceInstance<C> instance, A entity) {
        this.edsAsset = EdsAsset.builder()
                .parentId(0)
                .instanceId(instance.getEdsInstance()
                        .getId())
                .originalModel(YamlUtil.dump(entity))
                .valid(true)
                .build();
    }

    public static <C extends IEdsConfigModel, A> EdsAssetBuilder<C, A> newBuilder(
            ExternalDataSourceInstance<C> instance, A entity) {
        return new EdsAssetBuilder<>(instance, entity);
    }

    public EdsAssetBuilder<C, A> nameOf(String name) {
        edsAsset.setName(name);
        return this;
    }

    public EdsAssetBuilder<C, A> assetTypeOf(String assetType) {
        edsAsset.setAssetType(assetType);
        return this;
    }

    public EdsAssetBuilder<C, A> assetIdOf(String assetId) {
        edsAsset.setAssetId(assetId);
        return this;
    }

    public EdsAssetBuilder<C, A> assetIdOf(Integer assetId) {
        edsAsset.setAssetId(String.valueOf(assetId));
        return this;
    }

    public EdsAssetBuilder<C, A> assetIdOf(Long assetId) {
        edsAsset.setAssetId(String.valueOf(assetId));
        return this;
    }

    public EdsAssetBuilder<C, A> assetKeyOf(String assetKey) {
        edsAsset.setAssetKey(assetKey);
        return this;
    }

    public EdsAssetBuilder<C, A> kindOf(String kind) {
        edsAsset.setKind(kind);
        return this;
    }

    public EdsAssetBuilder<C, A> regionOf(String region) {
        edsAsset.setRegion(region);
        return this;
    }

    public EdsAssetBuilder<C, A> statusOf(String status) {
        edsAsset.setAssetStatus(status);
        return this;
    }

    public EdsAssetBuilder<C, A> zoneOf(String zone) {
        edsAsset.setZone(zone);
        return this;
    }

    public EdsAssetBuilder<C, A> validOf(Boolean valid) {
        edsAsset.setValid(valid);
        return this;
    }

    public EdsAssetBuilder<C, A> descriptionOf(String desc) {
        edsAsset.setDescription(desc);
        return this;
    }

    public EdsAssetBuilder<C, A> createdTimeOf(Long createdTime) {
        edsAsset.setCreatedTime(new Date(createdTime));
        return this;
    }

    public EdsAssetBuilder<C, A> createdTimeOf(OffsetDateTime createdTime) {
        edsAsset.setCreatedTime(Date.from(createdTime.toInstant()));
        return this;
    }

    public EdsAssetBuilder<C, A> createdTimeOf(Date createdTime) {
        edsAsset.setCreatedTime(createdTime);
        return this;
    }

    public EdsAssetBuilder<C, A> expiredTimeOf(Long expiredTime) {
        if (expiredTime != null) {
            edsAsset.setExpiredTime(new Date(expiredTime));
        }
        return this;
    }

    public EdsAssetBuilder<C, A> expiredTimeOf(Date expiredTime) {
        if (expiredTime != null) {
            edsAsset.setExpiredTime(expiredTime);
        }
        return this;
    }

    public EdsAssetBuilder<C, A> expiredTimeOf(OffsetDateTime expiredTime) {
        if (expiredTime != null) {
            edsAsset.setExpiredTime(Date.from(expiredTime.toInstant()));
        }
        return this;
    }

    public EdsAsset build() {
        if (edsAsset.getAssetId() == null) {
            edsAsset.setAssetId(UUID.randomUUID()
                    .toString());
        }
        if (edsAsset.getAssetKey() == null) {
            edsAsset.setAssetKey(edsAsset.getAssetId());
        }
        return edsAsset;
    }

}
