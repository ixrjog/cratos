package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import com.baiyi.cratos.eds.core.config.base.HasRegionsModel;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/28 09:38
 * &#064;Version 1.0
 */
@Slf4j
public abstract class BaseEdsRegionAssetProvider<Config extends HasRegionsModel & IEdsConfigModel, Asset extends HasRegionId> extends BaseEdsInstanceAssetProvider<Config, Asset> {

    public BaseEdsRegionAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
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
    protected List<Asset> listEntities(ExternalDataSourceInstance<Config> instance) throws EdsQueryEntitiesException {
        Config configModel = instance.getEdsConfigModel();
        List<Asset> entities = Lists.newArrayList();
        for (String regionId : getRegionSet(configModel)) {
            try {
                List<Asset> regionEntities = listEntities(regionId, configModel);
                if (!CollectionUtils.isEmpty(regionEntities)) {
                    entities.addAll(regionEntities);
                }
            } catch (Exception ex) {
                log.debug(ex.getMessage(), ex);
                throw new EdsQueryEntitiesException(ex.getMessage());
            }
        }
        return entities;
    }

    protected Set<String> getRegionSet(Config configModel) {
        Set<String> regionIdSet = Sets.newHashSet(configModel.getRegionId());
        if (configModel.getRegionIds() != null) {
            regionIdSet.addAll(configModel.getRegionIds());
        }
        return regionIdSet;
    }

    abstract protected List<Asset> listEntities(String regionId, Config configModel) throws EdsQueryEntitiesException;

}