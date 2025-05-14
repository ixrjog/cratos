package com.baiyi.cratos.eds.cratos.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
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
import com.baiyi.cratos.domain.model.cratos.CratosComputerModel;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/13 11:20
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CRATOS, assetTypeOf = EdsAssetTypeEnum.CRATOS_COMPUTER)
public class EdsCratosComputerAssetProvider extends BaseEdsInstanceAssetProvider<EdsCratosConfigModel.Cratos, CratosComputerModel.Computer> {

    public EdsCratosComputerAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                          EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<CratosComputerModel.Computer> listEntities(
            ExternalDataSourceInstance<EdsCratosConfigModel.Cratos> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Query not supported");
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsCratosConfigModel.Cratos> instance,
                                  CratosComputerModel.Computer entity) throws EdsAssetConversionException {
        return null;
    }

}