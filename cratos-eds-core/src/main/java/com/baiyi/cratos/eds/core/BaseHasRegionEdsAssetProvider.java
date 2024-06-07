package com.baiyi.cratos.eds.core;


import com.baiyi.cratos.eds.core.config.base.HasRegionModel;
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
import com.google.common.collect.Sets;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 下午4:13
 * &#064;Version 1.0
 */
public abstract class BaseHasRegionEdsAssetProvider<C extends HasRegionModel & IEdsConfigModel, A> extends BaseEdsInstanceAssetProvider<C, A> {

    public BaseHasRegionEdsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                         CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                         EdsAssetIndexFacade edsAssetIndexFacade,
                                         UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    /**
     * Region同步
     *
     * @param instance
     * @return
     * @throws EdsQueryEntitiesException
     */
    @Override
    protected List<A> listEntities(ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException {
        C configModel = instance.getEdsConfigModel();
        Set<String> regionIdSet = Sets.newHashSet(configModel.getRegionId());
        if (configModel.getRegionIds() != null) {
            regionIdSet.addAll(configModel.getRegionIds());
        }
        List<A> entities = Lists.newArrayList();
        regionIdSet.forEach(regionId -> {
            try {
                List<A> regionEntities = listEntities(regionId, configModel);
                if (!CollectionUtils.isEmpty(regionEntities)) {
                    entities.addAll(regionEntities);
                }
            } catch (Exception e) {
                throw new EdsQueryEntitiesException(e.getMessage());
            }
        });
        return entities;
    }

    abstract protected List<A> listEntities(String regionId, C configModel) throws EdsQueryEntitiesException;

}