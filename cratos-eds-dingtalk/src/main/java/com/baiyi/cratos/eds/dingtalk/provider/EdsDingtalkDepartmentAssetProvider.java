package com.baiyi.cratos.eds.dingtalk.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkDepartmentModel;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkDepartmentParam;
import com.baiyi.cratos.eds.dingtalk.repo.DingtalkDepartmentRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DINGTALK_DEPT_PARENT_ID;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午10:49
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.DINGTALK_APP, assetTypeOf = EdsAssetTypeEnum.DINGTALK_DEPARTMENT)
public class EdsDingtalkDepartmentAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Dingtalk, DingtalkDepartmentModel.Department> {

    private final DingtalkDepartmentRepo dingtalkDepartmentRepo;

    public EdsDingtalkDepartmentAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                              CredentialService credentialService,
                                              ConfigCredTemplate configCredTemplate,
                                              EdsAssetIndexFacade edsAssetIndexFacade,
                                              AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                              EdsInstanceProviderHolderBuilder holderBuilder,DingtalkDepartmentRepo dingtalkDepartmentRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
        this.dingtalkDepartmentRepo = dingtalkDepartmentRepo;
    }

    private static final long DEPT_ROOT_ID = 1L;

    @Override
    protected List<DingtalkDepartmentModel.Department> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Dingtalk> instance) throws EdsQueryEntitiesException {
        List<DingtalkDepartmentModel.Department> results = Lists.newArrayList();
        EdsConfigs.Dingtalk dingtalk = instance.getConfig();
        try {
            Set<Long> deptIdSet = queryDeptSubIds(instance);
            List<DingtalkDepartmentModel.Department> entities = Lists.newArrayList();
            deptIdSet.forEach(deptId -> {
                DingtalkDepartmentParam.GetDepartment getDepartment = DingtalkDepartmentParam.GetDepartment.builder()
                        .deptId(deptId)
                        .build();
                DingtalkDepartmentModel.GetDepartmentResult getDepartmentResult = dingtalkDepartmentRepo.get(dingtalk,
                        getDepartment);
                if (getDepartmentResult.getResult() != null) {
                    entities.add(getDepartmentResult.getResult());
                }
            });
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private Set<Long> queryDeptSubIds(ExternalDataSourceInstance<EdsConfigs.Dingtalk> instance) {
        EdsConfigs.Dingtalk dingtalk = instance.getConfig();
        // 遍历参数
        Set<Long> queryDeptIdSet = Optional.ofNullable(dingtalk)
                .map(EdsConfigs.Dingtalk::getApp)
                .map(EdsDingtalkConfigModel.DingtalkApp::getDepartment)
                .map(EdsDingtalkConfigModel.Department::getDeptIds)
                .orElse(Sets.newHashSet(DEPT_ROOT_ID));
        // 结果集
        Set<Long> subIdSet = Sets.newHashSet(queryDeptIdSet);
        while (!queryDeptIdSet.isEmpty()) {
            final Long deptId = queryDeptIdSet.iterator()
                    .next();
            DingtalkDepartmentParam.ListSubDepartmentId listSubDepartmentId = DingtalkDepartmentParam.ListSubDepartmentId.builder()
                    .deptId(deptId)
                    .build();
            queryDeptIdSet.remove(deptId);
            DingtalkDepartmentModel.DepartmentSubIdResult departmentSubIdResult = dingtalkDepartmentRepo.listSubId(dingtalk,
                    listSubDepartmentId);
            if (!CollectionUtils.isEmpty(departmentSubIdResult.getResult()
                    .getDeptIdList())) {
                subIdSet.addAll(departmentSubIdResult.getResult()
                        .getDeptIdList());
                queryDeptIdSet.addAll(departmentSubIdResult.getResult()
                        .getDeptIdList());
            }
        }
        return subIdSet;
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsConfigs.Dingtalk> instance, EdsAsset edsAsset,
            DingtalkDepartmentModel.Department entity) {
        if (entity.getParentId() == null) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(createEdsAssetIndex(edsAsset, DINGTALK_DEPT_PARENT_ID, entity.getParentId()));
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Dingtalk> instance,
                                  DingtalkDepartmentModel.Department entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getDeptId())
                .nameOf(entity.getName())
                .build();
    }

}
