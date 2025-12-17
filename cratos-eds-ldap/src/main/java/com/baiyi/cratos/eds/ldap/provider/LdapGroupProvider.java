package com.baiyi.cratos.eds.ldap.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import com.baiyi.cratos.eds.ldap.repo.LdapGroupRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.LDAP_GROUP_DN;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.LDAP_GROUP_MEMBERS;

/**
 * @Author baiyi
 * @Date 2024/3/8 16:15
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.LDAP, assetTypeOf = EdsAssetTypeEnum.LDAP_GROUP)
public class LdapGroupProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Ldap, LdapGroup.Group> {

    private final LdapGroupRepo ldapGroupRepo;
    private static final String USER_DN_TPL = "{}={},{},{}";

    public LdapGroupProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                             EdsAssetIndexFacade edsAssetIndexFacade,
                             AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                             EdsInstanceProviderHolderBuilder holderBuilder, LdapGroupRepo ldapGroupRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
        this.ldapGroupRepo = ldapGroupRepo;
    }

    @Override
    protected List<LdapGroup.Group> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Ldap> instance) throws EdsQueryEntitiesException {
        return ldapGroupRepo.queryGroup(instance.getConfig());
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Ldap> instance,
                                  LdapGroup.Group entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getGroupName())
                .nameOf(entity.getGroupName())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsConfigs.Ldap> instance,
                                            EdsAsset edsAsset, LdapGroup.Group entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, LDAP_GROUP_DN, Joiner.on(",")
                .skipNulls()
                .join(instance.getConfig()
                        .getGroup()
                        .getDn(), instance.getConfig()
                        .getBase())));
        List<String> members = ldapGroupRepo.queryGroupMember(instance.getConfig(), entity.getGroupName());
        indices.add(createEdsAssetIndex(edsAsset, LDAP_GROUP_MEMBERS, Joiner.on(";")
                .join(members.stream()
                        .map(e -> StringFormatter.arrayFormat(USER_DN_TPL, instance.getConfig()
                                .getUser()
                                .getId(), e, instance.getConfig()
                                .getUser()
                                .getDn(), instance.getConfig()
                                .getBase()))
                        .toList())));
        return indices;
    }

}
