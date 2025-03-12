package com.baiyi.cratos.eds.alimail.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.alimail.model.AlimailUser;
import com.baiyi.cratos.eds.alimail.repo.AlimailUserRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAlimailConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIMAIL_USER_DEPARTMENT_IDS;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.USER_MAIL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 09:42
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIMAIL, assetTypeOf = EdsAssetTypeEnum.ALIMAIL_USER)
public class EdsAlimailUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsAlimailConfigModel.Alimail, AlimailUser.User> {

    private final AlimailUserRepo alimailUserRepo;

    public EdsAlimailUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                       CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                       EdsAssetIndexFacade edsAssetIndexFacade,
                                       UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                       EdsInstanceProviderHolderBuilder holderBuilder,
                                       AlimailUserRepo alimailUserRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.alimailUserRepo = alimailUserRepo;
    }

    @Override
    protected List<AlimailUser.User> listEntities(
            ExternalDataSourceInstance<EdsAlimailConfigModel.Alimail> instance) throws EdsQueryEntitiesException {
        try {
            List<EdsAsset> departments = queryByInstanceAssets(instance, EdsAssetTypeEnum.ALIMAIL_DEPARTMENT);
            if (CollectionUtils.isEmpty(departments)) {
                return List.of();
            }
            List<AlimailUser.User> entities = Lists.newArrayList();
            departments.forEach(department -> {
                List<AlimailUser.User> users = alimailUserRepo.listUsersOfDepartment(instance.getEdsConfigModel(),
                        department.getAssetId());
                if (!CollectionUtils.isEmpty(users)) {
                    entities.addAll(users);
                }
            });
            return entities.stream().distinct().collect(Collectors.toList());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAlimailConfigModel.Alimail> instance,
                                  AlimailUser.User entity) {
        return newEdsAssetBuilder(instance, entity)
                // 资源 ID
                .assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsAlimailConfigModel.Alimail> instance, EdsAsset edsAsset,
            AlimailUser.User entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(entity.getDepartmentIds())) {
            indices.add(toEdsAssetIndex(edsAsset, ALIMAIL_USER_DEPARTMENT_IDS, Joiner.on(INDEX_VALUE_DIVISION_SYMBOL)
                    .join(entity.getDepartmentIds())));
        }
        indices.add(toEdsAssetIndex(edsAsset, USER_MAIL, entity.getEmail()));
        return indices;
    }

}
