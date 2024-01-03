package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.BeanCopierUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SysTag;
import com.baiyi.cratos.domain.param.tag.TagParam;
import com.baiyi.cratos.domain.view.tag.TagVO;
import com.baiyi.cratos.facade.TagFacade;
import com.baiyi.cratos.service.SysTagService;
import com.baiyi.cratos.wrapper.TagWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/2 17:40
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class TagFacadeImpl implements TagFacade {

    private final SysTagService tagService;

    private final TagWrapper tagWrapper;

    @Override
    public void addTag(TagParam.AddTag addTag) {
        tagService.add(BeanCopierUtil.copyProperties(addTag, SysTag.class));
    }

    @Override
    public void updateTag(TagParam.UpdateTag updateTag) {
        tagService.updateByPrimaryKey(BeanCopierUtil.copyProperties(updateTag, SysTag.class));
    }

    @Override
    public DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery) {
        DataTable<SysTag> table = tagService.queryPageByParam(pageQuery);
        return tagWrapper.wrap(table, TagVO.Tag.class);
    }

}