package com.baiyi.cratos.facade.tag.impl;

import com.baiyi.cratos.common.exception.BusinessException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.tag.TagParam;
import com.baiyi.cratos.domain.view.tag.TagVO;
import com.baiyi.cratos.facade.tag.TagFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.TagWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 17:40
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
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
    public List<TagVO.Tag> queryTagByBusinessType(BusinessParam.QueryByBusinessType getByBusinessType) {
        List<Integer> tagIds = businessTagService.queryTagIdByBusinessType(getByBusinessType);
        if (CollectionUtils.isEmpty(tagIds)) {
            return Collections.emptyList();
        }
        List<Tag> tags = tagService.queryByIds(tagIds);
        return tags.stream()
                .map(tagWrapper::wrapToTarget)
                .toList();
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return tagService;
    }
}