package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.BusinessException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.tag.TagParam;
import com.baiyi.cratos.domain.view.tag.TagVO;
import com.baiyi.cratos.facade.TagFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.wrapper.TagWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/2 17:40
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class TagFacadeImpl implements TagFacade {

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    private final TagWrapper tagWrapper;

    @Override
    public void addTag(TagParam.AddTag addTag) {
        tagService.add(addTag.toTarget());
    }

    @Override
    public void updateTag(TagParam.UpdateTag updateTag) {
        tagService.updateByPrimaryKey(updateTag.toTarget());
    }

    @Override
    public DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery) {
        DataTable<Tag> table = tagService.queryPageByParam(pageQuery);
        return tagWrapper.wrapToTarget(table);
    }

    @Override
    public void deleteById(int id) {
        int count = businessTagService.selectCountByTagId(id);
        if (count > 0) {
            throw new BusinessException("Business object in use count={}.", count);
        }
        tagService.deleteById(id);
    }

    @Override
    public void setTagValidById(int id) {
        tagService.updateValidById(id);
    }

}