package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;

import java.util.List;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/19 下午3:48
 * &#064;Version 1.0
 */
public abstract class BaseHasEndpointsEdsAssetProvider<C extends IEdsConfigModel, A> extends BaseMultipleSourcesEdsAssetProvider<C, A> {

    public BaseHasEndpointsEdsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                            EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    abstract protected Set<String> listEndpoints(
            ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

    @Override
    protected Set<String> getSources(ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException {
        return listEndpoints(instance);
    }

    abstract protected List<A> listEntities(String endpoint,
                                            ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

}