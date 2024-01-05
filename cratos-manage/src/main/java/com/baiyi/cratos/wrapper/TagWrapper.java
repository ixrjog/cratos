package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.view.tag.TagVO;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.base.IDataTableConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/2 18:02
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class TagWrapper implements IDataTableConverter<TagVO.Tag, Tag>, IBusinessWrapper<TagVO.ITag,TagVO.Tag> {

    private final BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.TAG;

    private final TagWrapper tagWrapper;

    private final TagService tagService;

    @Override
    public void wrap(TagVO.Tag tag) {
        // TODO
    }

    @Override
    public Class<TagVO.Tag> getTargetClazz() {
        return TagVO.Tag.class;
    }

    @Override
    public IBaseWrapper<TagVO.Tag> getBean() {
        return tagWrapper;
    }

    @Override
    public String getBusinessType() {
        return businessTypeEnum.name();
    }

    @Override
    public void businessWrap(TagVO.ITag iTag) {
        Tag tag = tagService.getById(iTag.getTagId());
        TagVO.Tag t = tagWrapper.convert(tag);
        tagWrapper.wrap(t);
        iTag.setTag(t);
    }

}