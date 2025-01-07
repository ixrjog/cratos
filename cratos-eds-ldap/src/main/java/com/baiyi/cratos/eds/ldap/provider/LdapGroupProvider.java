package com.baiyi.cratos.eds.ldap.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import com.baiyi.cratos.eds.ldap.repo.LdapGroupRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/8 16:15
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.LDAP, assetTypeOf = EdsAssetTypeEnum.LDAP_GROUP)
public class LdapGroupProvider extends BaseEdsInstanceAssetProvider<EdsLdapConfigModel.Ldap, LdapGroup.Group> {

    private final LdapGroupRepo ldapGroupRepo;

    public LdapGroupProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                             EdsAssetIndexFacade edsAssetIndexFacade,
                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                             EdsInstanceProviderHolderBuilder holderBuilder, LdapGroupRepo ldapGroupRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.ldapGroupRepo = ldapGroupRepo;
    }


    @Override
    protected List<LdapGroup.Group> listEntities(
            ExternalDataSourceInstance<EdsLdapConfigModel.Ldap> instance) throws EdsQueryEntitiesException {
        return ldapGroupRepo.queryGroup(instance.getEdsConfigModel());
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsLdapConfigModel.Ldap> instance,
                                  LdapGroup.Group entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getGroupName())
                .nameOf(entity.getGroupName())
                .build();
    }

}
