package com.baiyi.cratos.eds.dingtalk.provider;

import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkDepartmentModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkUserModel;
import com.baiyi.cratos.eds.dingtalk.repo.DingtalkUserRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * @Author baiyi
 * @Date 2024/5/6 下午2:23
 * @Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.DINGTALK_APP, assetTypeOf = EdsAssetTypeEnum.DINGTALK_USER)
public class EdsDingtalkUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsDingtalkConfigModel.Dingtalk, DingtalkUserModel.User> {

    private static final long DEPT_ROOT_ID = 1L;

    private final DingtalkUserRepo dingtalkUserRepo;

    public EdsDingtalkUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                        CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                        EdsAssetIndexFacade edsAssetIndexFacade,
                                        UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                        EdsInstanceProviderHolderBuilder holderBuilder,
                                        DingtalkUserRepo dingtalkUserRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.dingtalkUserRepo = dingtalkUserRepo;
    }

    @Override
    protected List<DingtalkUserModel.User> listEntities(
            ExternalDataSourceInstance<EdsDingtalkConfigModel.Dingtalk> instance) throws EdsQueryEntitiesException {
        List<DingtalkDepartmentModel.Department> results = Lists.newArrayList();
        Map<String, DingtalkUserModel.User> userMap = Maps.newHashMap();
        EdsDingtalkConfigModel.Dingtalk dingtalk = instance.getEdsConfigModel();
        try {
            Set<Long> deptIdSet = queryDeptSubIds(instance);
            List<DingtalkDepartmentModel.Department> entities = Lists.newArrayList();
            for (Long deptId : deptIdSet) {
                List<DingtalkUserModel.User> users = dingtalkUserRepo.queryUserByDeptId(dingtalk, deptId);
                if (!CollectionUtils.isEmpty(users)) {
                    users.forEach(e -> userMap.put(e.getUserid(), e));
                }
            }
            return new ArrayList<>(userMap.values());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    /**
     * Query by eds instance assets
     *
     * @param instance
     * @return
     */
    private Set<Long> queryDeptSubIds(ExternalDataSourceInstance<EdsDingtalkConfigModel.Dingtalk> instance) {
        List<EdsAsset> deptAssets = queryAssetsByInstanceAndType(instance, EdsAssetTypeEnum.DINGTALK_DEPARTMENT);
        return deptAssets.stream()
                .map(e -> Long.valueOf(e.getAssetId()))
                .collect(Collectors.toSet());
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsDingtalkConfigModel.Dingtalk> instance, EdsAsset edsAsset,
            DingtalkUserModel.User entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, DINGTALK_USER_USERNAME, entity.getUsername()));
        String mobilePhone = Joiner.on("-")
                .skipNulls()
                .join(entity.getStateCode(), entity.getMobile());
        indices.add(createEdsAssetIndex(edsAsset, DINGTALK_USER_MOBILE, mobilePhone));
        indices.add(createEdsAssetIndex(edsAsset, DINGTALK_USER_LEADER, entity.getLeader()
                .toString()));
        indices.add(createEdsAssetIndex(edsAsset, DINGTALK_USER_AVATAR, entity.getAvatar()));
        indices.add(createEdsAssetIndex(edsAsset, DINGTALK_USER_BOSS, entity.getBoss()
                .toString()));
        indices.add(createEdsAssetIndex(edsAsset, DINGTALK_USER_JOB_NUMBER, entity.getJobNumber()));
        // 头像
        if (ValidationUtils.isURL(entity.getAvatar())) {
            indices.add(createEdsAssetIndex(edsAsset, USER_AVATAR, entity.getAvatar()));
        }
        // Email
        if (StringUtils.hasText(entity.getEmail())) {
            indices.add(createEdsAssetIndex(edsAsset, USER_MAIL, entity.getEmail()));
        }
        // Manager
        try {
            DingtalkUserModel.GetUser getUser = dingtalkUserRepo.getUser(instance.getEdsConfigModel(),
                    entity.getUserid());
            if (StringUtils.hasText(getUser.getManagerUserid())) {
                indices.add(createEdsAssetIndex(edsAsset, DINGTALK_MANAGER_USER_ID, getUser.getManagerUserid()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return indices;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsDingtalkConfigModel.Dingtalk> instance,
                                         DingtalkUserModel.User entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getUserid())
                .assetKeyOf(entity.getUnionid())
                .nameOf(entity.getName())
                .descriptionOf(entity.getTitle())
                .validOf(entity.getActive())
                .build();
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.builder()
                .comparisonName(true)
                .build()
                .compare(a1, a2);
    }

}
