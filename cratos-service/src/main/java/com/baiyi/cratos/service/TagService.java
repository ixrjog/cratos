package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.tag.TagParam;
import com.baiyi.cratos.service.base.BaseService;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:28
 * @Version 1.0
 */
public interface TagService extends BaseService<Tag> {

    DataTable<Tag> queryPageByParam(TagParam.TagPageQuery pageQuery);

}