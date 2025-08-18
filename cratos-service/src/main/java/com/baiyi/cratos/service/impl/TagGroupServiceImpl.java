package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.business.PermissionBusinessServiceFactory;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagGroupService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.factory.SupportBusinessServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 10:35
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TAG_GROUP)
public class TagGroupServiceImpl implements TagGroupService {

    private final TagService tagService;
    private final BusinessTagService businessTagService;
    public static final String TAG_GROUP = "Group";

    @Override
    public Tag getTagGroup() {
        return tagService.getByTagKey(TAG_GROUP);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataTable<PermissionBusinessVO.PermissionBusiness> queryUserPermissionBusinessPage(
            UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery) {
        Tag tagGroup = getTagGroup();
        if (Objects.isNull(tagGroup)) {
            return DataTable.NO_DATA;
        }
        BusinessTagParam.BusinessTagPageQuery param = BusinessTagParam.BusinessTagPageQuery.builder()
                .tagGroupId(tagGroup.getId())
                .queryName(pageQuery.getQueryName())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .page(pageQuery.getPage())
                .length(pageQuery.getLength())
                .build();
        DataTable<BusinessTag> dataTable = businessTagService.queryPageByParam(param);
        return new DataTable<>(dataTable.getData()
                .stream()
                .map(e -> {
                    e.setBusinessId(tagGroup.getId());
                    return toPermissionBusiness(e);
                })
                .toList(), dataTable.getTotalNum());
    }

    @Override
    public PermissionBusinessVO.PermissionBusiness toPermissionBusiness(BusinessTag recode) {
        return PermissionBusinessVO.PermissionBusiness.builder()
                .name(recode.getTagValue())
                .displayName(recode.getTagValue())
                .businessType(getBusinessType())
                .businessId(recode.getTagId())
                .build();
    }

    @Override
    public PermissionBusinessVO.PermissionBusiness getByBusinessName(String name) {
        return PermissionBusinessVO.PermissionBusiness.builder()
                .name(name)
                .displayName(name)
                .businessType(getBusinessType())
                .businessId(name.hashCode())
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        SupportBusinessServiceFactory.register(this);
        PermissionBusinessServiceFactory.register(this);
    }
}
