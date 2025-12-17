package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;

import java.util.List;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/3 下午2:07
 * &#064;Version 1.0
 */
public abstract class BaseHasNamespaceEdsAssetProvider<C extends HasEdsConfig, A> extends BaseMultipleSourcesEdsAssetProvider<C, A> {

    public BaseHasNamespaceEdsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                            EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
    }

    abstract protected Set<String> listNamespace(
            ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

    @Override
    protected Set<String> getSources(ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException {
        return listNamespace(instance);
    }

    abstract protected List<A> listEntities(String namespace,
                                            ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

}