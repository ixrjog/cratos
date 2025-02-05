package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagGroupService;
import com.baiyi.cratos.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 10:35
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TAG_GROUP)
public class TagGroupServiceImpl implements TagGroupService {

    private TagService tagService;
    private BusinessTagService businessTagService;
    public static final String TAG_GROUP = "TAG_GROUP";

    @Override
    public DataTable<PermissionBusinessVO.PermissionBusiness> queryUserPermissionBusinessPage(
            UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery) {
        Tag tagGroup = tagService.getByTagKey(TAG_GROUP);
        if(tagGroup == null){
            return DataTable.NO_DATA;
        }



        return null;
    }

    @Override
    public PermissionBusinessVO.PermissionBusiness toPermissionBusiness(Tag recode) {
        return null;
    }

}
