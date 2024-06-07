package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/3 下午2:07
 * &#064;Version 1.0
 */
public abstract class BaseHasNamespaceEdsAssetProvider<C extends IEdsConfigModel, A> extends BaseEdsInstanceAssetProvider<C, A> {

    public BaseHasNamespaceEdsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    /**
     * Namespace同步
     *
     * @param instance
     * @return
     * @throws EdsQueryEntitiesException
     */
    @Override
    protected List<A> listEntities(ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException {
        List<A> entities = Lists.newArrayList();
        Set<String> namespaces = listNamespace(instance);
        if (namespaces.isEmpty()) {
            return entities;
        }
        namespaces.forEach(namespace -> {
            try {
                List<A> namespaceEntities = listEntities(namespace, instance);
                if (!CollectionUtils.isEmpty(namespaceEntities)) {
                    entities.addAll(namespaceEntities);
                }
            } catch (Exception e) {
                throw new EdsQueryEntitiesException(e.getMessage());
            }
        });
        return entities;
    }

    abstract protected Set<String> listNamespace(
            ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

    abstract protected List<A> listEntities(String namespace,
                                            ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

}