package com.baiyi.cratos.eds.cratos.provider;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.cratos.CratosAssetParam;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsCratosConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/13 11:20
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CRATOS, assetTypeOf = EdsAssetTypeEnum.CRATOS_COMPUTER)
public class EdsCratosComputerAssetProvider extends BaseEdsInstanceAssetProvider<EdsCratosConfigModel.Cratos, CratosAssetParam.CratosAsset> {

    public EdsCratosComputerAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                          EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<CratosAssetParam.CratosAsset> listEntities(
            ExternalDataSourceInstance<EdsCratosConfigModel.Cratos> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Query not supported");
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsCratosConfigModel.Cratos> instance,
                                  CratosAssetParam.CratosAsset entity) throws EdsAssetConversionException {
        String assetId = StringUtils.hasText(entity.getAssetId()) ? entity.getAssetId() : IdentityUtil.randomUUID();
        if (!IpUtils.isIP(entity.getAssetKey())) {
            EdsAssetConversionException.runtime("`Remote Management IP` not a valid IP address.");
        }
        return newEdsAssetBuilder(instance, entity)
                .idOf(entity.getId())
                .assetIdOf(assetId)
                .nameOf(entity.getName())
                .assetKeyOf(entity.getAssetKey())
                .regionOf(entity.getRegion())
                .zoneOf(entity.getZone())
                .validOf(entity.getValid())
                .build();
    }

}