package com.baiyi.cratos.eds.security.apirisk.provider;

import com.baiyi.cratos.common.enums.SecurityLevel;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.util.DomainUtils;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.security.apirisk.repo.ApiRiskWeaknessRepo;
import com.baiyi.cratos.eds.security.apirisk.result.ApiRiskWeaknessResult;
import com.baiyi.cratos.service.TagService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/12 17:21
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.APIRISK, assetTypeOf = EdsAssetTypeEnum.APIRISK_WEAKNESS)
public class EdsApiriskWeaknessProvider extends BaseEdsAssetProvider<EdsConfigs.ApiRisk, ApiRiskWeaknessResult.Weakness> {

    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    public EdsApiriskWeaknessProvider(EdsAssetProviderContext context, TagService tagService,
                                      BusinessTagFacade businessTagFacade) {
        super(context);
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
    }

    @Override
    protected List<ApiRiskWeaknessResult.Weakness> listEntities(
            ExternalDataSourceInstance<EdsConfigs.ApiRisk> instance) throws EdsQueryEntitiesException {
        return ApiRiskWeaknessRepo.listAllWeakness(instance.getConfig());
    }

    @Override
    protected EdsAsset toAsset(ExternalDataSourceInstance<EdsConfigs.ApiRisk> instance,
                               ApiRiskWeaknessResult.Weakness entity) throws EdsAssetConversionException {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .assetKeyOf(entity.getOperationId())
                .kindOf(entity.getTypeName())
                .descriptionOf(entity.getApiUrl())
                .build();
    }

    @Override
    protected void processAssetTags(EdsAsset asset, ExternalDataSourceInstance<EdsConfigs.ApiRisk> instance,
                                    ApiRiskWeaknessResult.Weakness entity, List<EdsAssetIndex> indices) {
        SecurityLevel level = evaluateSecurityLevel(entity);
        BusinessTagParam.SaveBusinessTag securityLevelBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                .tagId(tagService.getByTagKey(SysTagKeys.SECURITY_LEVEL.getKey())
                               .getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(asset.getId())
                .tagValue(level.name())
                .build();
        businessTagFacade.saveBusinessTag(securityLevelBusinessTag);
        BusinessTagParam.SaveBusinessTag stateBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                .tagId(tagService.getByTagKey(SysTagKeys.STATE.getKey())
                               .getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(asset.getId())
                .tagValue(entity.getStateName())
                .build();
        businessTagFacade.saveBusinessTag(stateBusinessTag);
        if (DomainUtils.isValidDomainName(entity.getHost())) {
            BusinessTagParam.SaveBusinessTag hostBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                    .tagId(tagService.getByTagKey(SysTagKeys.HOST.getKey())
                                   .getId())
                    .businessType(BusinessTypeEnum.EDS_ASSET.name())
                    .businessId(asset.getId())
                    .tagValue(entity.getHost())
                    .build();
            businessTagFacade.saveBusinessTag(hostBusinessTag);
        }
    }

    private SecurityLevel evaluateSecurityLevel(ApiRiskWeaknessResult.Weakness entity) {
        if ("高危".equals(entity.getLevelName())) {
            return SecurityLevel.HIGH;
        }
        if ("中危".equals(entity.getLevelName())) {
            return SecurityLevel.MEDIUM;
        }
        if ("低危".equals(entity.getLevelName())) {
            return SecurityLevel.LOW;
        }
        return SecurityLevel.UNKNOWN;
    }

}