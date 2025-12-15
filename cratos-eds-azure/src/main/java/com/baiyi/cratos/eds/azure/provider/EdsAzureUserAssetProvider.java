package com.baiyi.cratos.eds.azure.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.azure.graph.model.GraphUserModel;
import com.baiyi.cratos.eds.azure.repo.GraphUserRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsAzureConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/12 18:00
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AZURE, assetTypeOf = EdsAssetTypeEnum.AZURE_USER)
public class EdsAzureUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsAzureConfigModel.Azure, GraphUserModel.User> {

    public EdsAzureUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                     EdsInstanceProviderHolderBuilder holderBuilder) {
        super(
                edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder
        );
    }

    @Override
    protected List<GraphUserModel.User> listEntities(
            ExternalDataSourceInstance<EdsAzureConfigModel.Azure> instance) throws EdsQueryEntitiesException {
        return GraphUserRepo.listUsers(instance.getConfig())
                .stream()
                .peek(e -> wrapUser(instance.getConfig(), e))
                .toList();
    }

    private void wrapUser(EdsAzureConfigModel.Azure azure, GraphUserModel.User user) {
        GraphUserModel.User base = GraphUserRepo.getUserById(azure, user.getId());
        user.setAccountEnabled(base.getAccountEnabled());
        List<String> roleIds = GraphUserRepo.getUserDirectoryRoleIds(azure, user.getId());
        user.setDirectoryRoleIds(roleIds);
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAzureConfigModel.Azure> instance,
                                         GraphUserModel.User entity) throws EdsAssetConversionException {
        String name = Joiner.on("|")
                .skipNulls()
                .join(entity.getDisplayName(), entity.getSurname(), entity.getGivenName());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(name)
                .assetKeyOf(entity.getUserPrincipalName())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsAzureConfigModel.Azure> instance, EdsAsset edsAsset,
            GraphUserModel.User entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        String username = StringUtils.substringBefore(entity.getUserPrincipalName(), "@");
        indices.add(createEdsAssetIndex(edsAsset, CLOUD_ACCOUNT_USERNAME, username));
        indices.add(createEdsAssetIndex(
                edsAsset, CLOUD_LOGIN_PROFILE,
                entity.getAccountEnabled() ? "Enabled" : "Disabled"
        ));
        // 角色
        if (!CollectionUtils.isEmpty(entity.getDirectoryRoleIds())) {
            java.util.Set<String> dirRoleIdsSet = Set.copyOf(entity.getDirectoryRoleIds());
            String joined = queryAssetsByInstanceAndType(instance, EdsAssetTypeEnum.AZURE_DIRECTORY_ROLE).stream()
                    .filter(role -> dirRoleIdsSet.contains(role.getAssetId()))
                    .map(EdsAsset::getName)
                    .filter(StringUtils::isNotBlank)
                    .collect(java.util.stream.Collectors.joining(INDEX_VALUE_DIVISION_SYMBOL));
            if (StringUtils.isNotBlank(joined)) {
                indices.add(createEdsAssetIndex(edsAsset, AZURE_DIRECTORY_ROLES, joined));
            }
        }
        return indices;
    }

}
