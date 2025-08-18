package com.baiyi.cratos.facade.tag.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.UserPermissionBusinessFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.tag.TagGroupParam;
import com.baiyi.cratos.domain.query.EdsAssetQuery;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.facade.tag.TagGroupFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/2 14:54
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TagGroupFacadeImpl implements TagGroupFacade {

    private final TagService tagService;
    private final BusinessTagService businessTagService;
    private final EdsAssetService edsAssetService;
    private final EdsAssetWrapper edsAssetWrapper;
    private final UserPermissionBusinessFacade userPermissionBusinessFacade;

    @Override
    public OptionsVO.Options getGroupOptions(TagGroupParam.GetGroupOptions getGroupOptions) {
        Tag tag = getGroupTag();
        if (Objects.isNull(tag)) {
            return OptionsVO.NO_OPTIONS_AVAILABLE;
        }
        BusinessTagParam.QueryByTag query = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .queryTagValue(getGroupOptions.getQueryName())
                .build();
        List<String> tagGroupNames = businessTagService.queryByValue(query);
        return OptionsVO.toOptions(tagGroupNames);
    }

    @Override
    public DataTable<EdsAssetVO.Asset> queryGroupAssetPage(TagGroupParam.GroupAssetPageQuery pageQuery) {
        Tag tag = getGroupTag();
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .tagValue(pageQuery.getTagGroup())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .build();
        List<Integer> businessIds = businessTagService.queryBusinessIdByTag(queryByTag);
        EdsInstanceParam.AssetPageQueryParam param = EdsInstanceParam.AssetPageQueryParam.builder()
                .page(pageQuery.getPage())
                .length(pageQuery.getLength())
                .queryName(pageQuery.getQueryName())
                .idList(businessIds)
                .build();
        DataTable<EdsAsset> dataTable = edsAssetService.queryEdsInstanceAssetPage(param);
        return edsAssetWrapper.wrapToTarget(dataTable);
    }

    @Override
    @SetSessionUserToParam(desc = "set query username")
    public DataTable<EdsAssetVO.Asset> queryMyGroupAssetPage(TagGroupParam.MyGroupAssetPageQuery pageQuery) {
        EdsAssetQuery.UserPermissionPageQueryParam queryParam = EdsAssetQuery.UserPermissionPageQueryParam.builder()
                .username(pageQuery.getUsername())
                .page(pageQuery.getPage())
                .length(pageQuery.getLength())
                .queryName(pageQuery.getQueryName())
                .queryGroupName(pageQuery.getTagGroup())
                .build();
        DataTable<EdsAsset> dataTable = userPermissionBusinessFacade.queryUserPermissionAssets(queryParam);
        return edsAssetWrapper.wrapToTarget(dataTable);
    }

    private Tag getGroupTag() {
        return tagService.getByTagKey(SysTagKeys.GROUP.getKey());
    }

}
