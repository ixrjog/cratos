package com.baiyi.cratos.eds.idc.provider;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.cratos.CustomIdcParam;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.CustomAsset;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsCustomIdcConfigModel;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/19 15:32
 * &#064;Version 1.0
 */
@Slf4j
@Component
@CustomAsset
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CUSTOM_IDC, assetTypeOf = EdsAssetTypeEnum.CUSTOM_IDC_HOST)
public class EdsCustomIDCHostAssetProvider extends BaseEdsAssetProvider<EdsConfigs.CustomIdc, CustomIdcParam.Host> {

    public EdsCustomIDCHostAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<CustomIdcParam.Host> listEntities(
            ExternalDataSourceInstance<EdsConfigs.CustomIdc> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Query not supported.");
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.CustomIdc> instance,
                                         CustomIdcParam.Host entity) throws EdsAssetConversionException {
        if (!IpUtils.isIP(entity.getAssetKey())) {
            throw new EdsAssetConversionException("`Remote Management IP` not a valid IP address.");
        }
        EdsCustomIdcConfigModel.HostConfig hostConfig = Optional.ofNullable(instance.getConfig().getHost())
                .orElseGet(EdsCustomIdcConfigModel.HostConfig::new);
        String assetId = StringUtils.hasText(entity.getAssetId()) ? entity.getAssetId() : IdentityUtils.randomUUID();
        String hostName = resolveHostName(entity.getName(), hostConfig.getPrefix());
        String region = StringUtils.hasText(entity.getRegion()) ? entity.getRegion() : defaultStr(hostConfig.getRegion());
        String zone = StringUtils.hasText(entity.getZone()) ? entity.getZone() : defaultStr(hostConfig.getZone());
        return createAssetBuilder(instance, entity).idOf(entity.getId())
                .assetIdOf(assetId)
                .nameOf(hostName)
                .assetKeyOf(entity.getAssetKey())
                .regionOf(region)
                .zoneOf(zone)
                .validOf(entity.getValid())
                .build();
    }

    private String resolveHostName(String name, String prefix) {
        if (!StringUtils.hasText(prefix) || name.startsWith(prefix)) {
            return name;
        }
        return prefix + name;
    }

    private String defaultStr(String value) {
        return StringUtils.hasText(value) ? value : "";
    }

    /**
     * 始终返回 false，表示每次导入都执行更新
     */
    @Override
    protected boolean isAssetUnchanged(EdsAsset existing, EdsAsset incoming) {
        return false;
    }

}