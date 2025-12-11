package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.view.tag.TagVO;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseBusinessWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/2 18:02
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TAG)
public class TagWrapper extends BaseDataTableConverter<TagVO.Tag, Tag> implements BaseBusinessWrapper<TagVO.HasTag,TagVO.Tag> {

    private final TagService tagService;

    @Override
    public void wrap(TagVO.Tag vo) {
        // This is a good idea
    }

    @Override
    public void decorateBusiness(TagVO.HasTag biz) {
        Tag tag = tagService.getById(biz.getTagId());
        TagVO.Tag t = convert(tag);
        delegateWrap(t);
        biz.setTag(t);
    }

}