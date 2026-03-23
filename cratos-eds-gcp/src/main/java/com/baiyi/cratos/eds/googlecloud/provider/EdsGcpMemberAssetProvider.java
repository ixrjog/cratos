package com.baiyi.cratos.eds.googlecloud.provider;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.googlecloud.model.GcpMemberModel;
import com.baiyi.cratos.eds.googlecloud.repo.GcpProjectRepo;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.GCP_MEMBER_ROLES;

/**
 * @Author 修远
 * @Date 2024/7/30 上午11:16
 * @Since 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GCP, assetTypeOf = EdsAssetTypeEnum.GCP_MEMBER)
public class EdsGcpMemberAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Gcp, GcpMemberModel.Member> {

    private final GcpProjectRepo gcpProjectRepo;
    private final BusinessTagFacade businessTagFacade;
    private final UserService userService;
    private final BusinessTagService businessTagService;
    private final TagService tagService;

    public EdsGcpMemberAssetProvider(EdsAssetProviderContext context, GcpProjectRepo googleCloudProjectRepo,
                                     BusinessTagFacade businessTagFacade, UserService userService,
                                     BusinessTagService businessTagService, TagService tagService) {
        super(context);
        this.gcpProjectRepo = googleCloudProjectRepo;
        this.businessTagFacade = businessTagFacade;
        this.userService = userService;
        this.businessTagService = businessTagService;
        this.tagService = tagService;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Gcp> instance,
                                         GcpMemberModel.Member entity) throws EdsAssetConversionException {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getName())
                .assetKeyOf(entity.getName())
                .nameOf(entity.getName())
                .kindOf(entity.getType())
                .descriptionOf(entity.getType())
                .build();
    }

    @Override
    protected List<GcpMemberModel.Member> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Gcp> instance) throws EdsQueryEntitiesException {
        try {
            return gcpProjectRepo.listMembers(instance.getConfig())
                    .entrySet()
                    .stream()
                    .map(e -> GcpMemberModel.toMember(e.getKey(), e.getValue()))
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected List<EdsAssetIndex> buildIndexes(ExternalDataSourceInstance<EdsConfigs.Gcp> instance, EdsAsset edsAsset,
                                               GcpMemberModel.Member entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        // "roles/"
        String roles = Joiner.on(INDEX_VALUE_DIVISION_SYMBOL)
                .join(entity.getRoles()
                              .stream()
                              .map(role -> StringUtils.substring(role, 6))
                              .sorted()
                              .collect(Collectors.toList()));
        indices.add(createEdsAssetIndex(edsAsset, GCP_MEMBER_ROLES, roles));
        indices.add(createEdsAssetIndex(edsAsset, CLOUD_ACCOUNT_USERNAME, entity.getName()));
        return indices;
    }

    @Override
    protected boolean isAssetUnchanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

    @Override
    protected void processAssetTags(EdsAsset asset, ExternalDataSourceInstance<EdsConfigs.Gcp> instance,
                                    GcpMemberModel.Member entity, List<EdsAssetIndex> indices) {
        if (!"user".equals(entity.getType()) || !ValidationUtils.isEmail(entity.getName())) {
            return;
        }
        if (businessTagFacade.containsTag(
                BusinessTypeEnum.EDS_ASSET.name(), asset.getId(), SysTagKeys.USERNAME.getKey())) {
            return;
        }
        User user = resolveUser(instance, entity.getName());
        if (user == null) {
            return;
        }
        Tag tag = tagService.getByTagKey(SysTagKeys.USERNAME.getKey());
        if (tag == null) {
            return;
        }
        BusinessTag businessTag = BusinessTag.builder()
                .tagId(tag.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(asset.getId())
                .tagValue(user.getUsername())
                .build();
        businessTagService.add(businessTag);
    }

    /**
     * 解析用户: 优先用配置映射，其次按邮箱精确匹配（仅匹配到唯一用户时生效）
     */
    private User resolveUser(ExternalDataSourceInstance<EdsConfigs.Gcp> instance, String email) {
        Map<String, String> userMapping = Optional.of(instance.getConfig())
                .map(EdsConfigs.Gcp::getUser)
                .map(EdsGcpConfigModel.User::getMapping)
                .orElse(Map.of());
        if (userMapping.containsKey(email)) {
            return userService.getByUsername(userMapping.get(email));
        }
        List<User> users = userService.queryByEmail(email);
        return (users != null && users.size() == 1) ? users.getFirst() : null;
    }

}
