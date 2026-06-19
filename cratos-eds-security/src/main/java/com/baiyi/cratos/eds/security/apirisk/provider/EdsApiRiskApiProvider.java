package com.baiyi.cratos.eds.security.apirisk.provider;

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
import com.baiyi.cratos.eds.security.apirisk.repo.ApiRiskApiRepo;
import com.baiyi.cratos.eds.security.apirisk.result.ApiRiskApiResult;
import com.baiyi.cratos.service.TagService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/12 15:14
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.APIRISK, assetTypeOf = EdsAssetTypeEnum.APIRISK_API)
public class EdsApiRiskApiProvider extends BaseEdsAssetProvider<EdsConfigs.ApiRisk, ApiRiskApiResult.Api> {

    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    public EdsApiRiskApiProvider(EdsAssetProviderContext context, TagService tagService,
                                 BusinessTagFacade businessTagFacade) {
        super(context);
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
    }

    @Override
    protected List<ApiRiskApiResult.Api> listEntities(
            ExternalDataSourceInstance<EdsConfigs.ApiRisk> instance) throws EdsQueryEntitiesException {
        return ApiRiskApiRepo.listAllApi(instance.getConfig());
    }

    @Override
    protected EdsAsset toAsset(ExternalDataSourceInstance<EdsConfigs.ApiRisk> instance,
                               ApiRiskApiResult.Api entity) throws EdsAssetConversionException {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .assetKeyOf(entity.getApiUrl())
                .nameOf(substringFromThirdSlash(entity.getApiUrl()))
                .kindOf(entity.getApiRiskLevelName())
                .build();
    }

    @Override
    protected void processAssetTags(EdsAsset asset, ExternalDataSourceInstance<EdsConfigs.ApiRisk> instance,
                                    ApiRiskApiResult.Api entity, List<EdsAssetIndex> indices) {
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

    /**
     * 从第 n 个 '/' 开始截取字符串(包含该斜杠)。
     *
     * @param str 原始字符串
     * @param n   第几个 '/'(从 1 开始)
     * @return 第 n 个 '/' 及之后的内容;不足 n 个 '/' 或入参为空时返回 null
     */
    public static String substringFromNthSlash(String str, int n) {
        if (str == null || n <= 0) {
            return null;
        }
        int index = -1;
        for (int i = 0; i < n; i++) {
            index = str.indexOf('/', index + 1);
            if (index == -1) {
                return null;
            }
        }
        return str.substring(index);
    }

    /**
     * 从第 3 个 '/' 开始截取(含斜杠)。
     */
    public static String substringFromThirdSlash(String str) {
        return substringFromNthSlash(str, 3);
    }

}
