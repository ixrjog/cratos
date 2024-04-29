package com.baiyi.cratos.eds.ldap.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.baiyi.cratos.eds.ldap.repo.LdapPersonRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:47
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.LDAP, assetType = EdsAssetTypeEnum.LDAP_PERSON)
public class LdapPersonProvider extends BaseEdsInstanceAssetProvider<EdsLdapConfigModel.Ldap, LdapPerson.Person> {

    private final LdapPersonRepo ldapPersonRepo;

    public LdapPersonProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                             EdsAssetIndexFacade edsAssetIndexFacade, LdapPersonRepo ldapPersonRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.ldapPersonRepo = ldapPersonRepo;
    }

    @Override
    protected List<LdapPerson.Person> listEntities(ExternalDataSourceInstance<EdsLdapConfigModel.Ldap> instance) throws EdsQueryEntitiesException {
        return ldapPersonRepo.queryPerson(instance.getEdsConfigModel());
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsLdapConfigModel.Ldap> instance, LdapPerson.Person entity) {
        return newEdsAssetBuilder(instance, entity)
                .assetIdOf(entity.getUsername())
                .nameOf(entity.getDisplayName())
                .build();
    }

}
