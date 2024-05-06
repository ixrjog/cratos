package com.baiyi.cratos.eds.dingtalk.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkDepartment;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkUser;
import com.baiyi.cratos.eds.dingtalk.repo.DingtalkUserRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/5/6 下午2:23
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.DINGTALK_APP, assetType = EdsAssetTypeEnum.DINGTALK_USER)
public class EdsDingtalkUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsDingtalkConfigModel.Dingtalk, DingtalkUser.User> {

    private static final long DEPT_ROOT_ID = 1L;

    private final DingtalkUserRepo dingtalkUserRepo;

    public EdsDingtalkUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                        CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                        EdsAssetIndexFacade edsAssetIndexFacade, DingtalkUserRepo dingtalkUserRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.dingtalkUserRepo = dingtalkUserRepo;
    }

    @Override
    protected List<DingtalkUser.User> listEntities(
            ExternalDataSourceInstance<EdsDingtalkConfigModel.Dingtalk> instance) throws EdsQueryEntitiesException {
        List<DingtalkDepartment.Department> results = Lists.newArrayList();
        Map<String, DingtalkUser.User> userMap = Maps.newHashMap();
        EdsDingtalkConfigModel.Dingtalk dingtalk = instance.getEdsConfigModel();
        try {
            Set<Long> deptIdSet = queryDeptSubIds(instance);
            List<DingtalkDepartment.Department> entities = Lists.newArrayList();
            for (Long deptId : deptIdSet) {
                List<DingtalkUser.User> users = dingtalkUserRepo.queryUserByDeptId(dingtalk, deptId);
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
        List<EdsAsset> deptAssets = queryByInstanceAssets(instance, EdsAssetTypeEnum.DINGTALK_DEPARTMENT);
        return deptAssets.stream()
                .map(e -> Long.valueOf(e.getAssetId()))
                .collect(Collectors.toSet());
    }

    private static final String GROUP = "dingtalk.user.";

    public static final String DINGTALK_USER_USERNAME = GROUP + "username";
    public static final String DINGTALK_USER_MOBILE = GROUP + "mobile";
    public static final String DINGTALK_USER_LEADER = GROUP + "leader";
    public static final String DINGTALK_USER_AVATAR = GROUP + "avatar";
    public static final String DINGTALK_USER_BOSS = GROUP + "boss";
    public static final String DINGTALK_USER_JOB_NUMBER = GROUP + "jobNumber";

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(EdsAsset edsAsset, DingtalkUser.User entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(toEdsAssetIndex(edsAsset, DINGTALK_USER_USERNAME, entity.getUsername()));
        indices.add(toEdsAssetIndex(edsAsset, DINGTALK_USER_MOBILE, entity.getMobile()));
        indices.add(toEdsAssetIndex(edsAsset, DINGTALK_USER_LEADER, entity.getLeader()
                .toString()));
        indices.add(toEdsAssetIndex(edsAsset, DINGTALK_USER_AVATAR, entity.getAvatar()));
        indices.add(toEdsAssetIndex(edsAsset, DINGTALK_USER_BOSS, entity.getBoss()
                .toString()));
        indices.add(toEdsAssetIndex(edsAsset, DINGTALK_USER_JOB_NUMBER, entity.getJobNumber()));
        return indices;
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsDingtalkConfigModel.Dingtalk> instance,
                                  DingtalkUser.User entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getUserid())
                .assetKeyOf(entity.getUnionid())
                .nameOf(entity.getName())
                .descriptionOf(entity.getTitle())
                .validOf(entity.getActive())
                .build();
    }

}
