package com.baiyi.cratos.service;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.TagParam;
import com.baiyi.cratos.mapper.TagMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:28
 * @Version 1.0
 */
public interface TagService extends BaseUniqueKeyService<Tag, TagMapper>, BaseValidService<Tag, TagMapper> {

    DataTable<Tag> queryPageByParam(TagParam.TagPageQuery pageQuery);

    Tag getByTagKey(String tagKey);

    default Tag getByTagKey(SysTagKeys sysTagKey) {
        return getByTagKey(sysTagKey.getKey());
    }

}