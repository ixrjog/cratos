package com.baiyi.cratos.eds.ldap.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.baiyi.cratos.eds.ldap.repo.LdapGroupRepo;
import com.baiyi.cratos.eds.ldap.repo.LdapPersonRepo;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.LDAP_USER_DN;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.LDAP_USER_GROUPS;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:47
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.LDAP, assetTypeOf = EdsAssetTypeEnum.LDAP_PERSON)
public class LdapPersonProvider extends BaseEdsAssetProvider<EdsConfigs.Ldap, LdapPerson.Person> {

    private final LdapPersonRepo ldapPersonRepo;
    private final LdapGroupRepo ldapGroupRepo;

    public LdapPersonProvider(EdsAssetProviderContext context, LdapPersonRepo ldapPersonRepo,
                              LdapGroupRepo ldapGroupRepo) {
        super(context);
        this.ldapPersonRepo = ldapPersonRepo;
        this.ldapGroupRepo = ldapGroupRepo;
    }

    @Override
    protected List<LdapPerson.Person> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Ldap> instance) throws EdsQueryEntitiesException {
        return ldapPersonRepo.queryPerson(instance.getConfig());
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Ldap> instance,
                                         LdapPerson.Person entity) {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getUsername())
                .nameOf(entity.getDisplayName())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> buildIndexes(ExternalDataSourceInstance<EdsConfigs.Ldap> instance, EdsAsset edsAsset,
                                               LdapPerson.Person entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(
                edsAsset, LDAP_USER_DN, Joiner.on(",")
                        .skipNulls()
                        .join(
                                instance.getConfig()
                                        .getUser()
                                        .getDn(), instance.getConfig()
                                        .getBase()
                        )
        ));
        List<LdapGroup.Group> groups = ldapGroupRepo.searchGroupByUsername(instance.getConfig(), entity.getUsername());
        indices.add(createEdsAssetIndex(
                edsAsset, LDAP_USER_GROUPS, Joiner.on(";")
                        .join(groups.stream()
                                      .map(LdapGroup.Group::getGroupDn)
                                      .toList())
        ));
        return indices;
    }

}
