package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.SysTag;
import com.baiyi.cratos.domain.view.tag.TagVO;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.baiyi.cratos.wrapper.base.IDataTableConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/2 18:02
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TagWrapper implements IDataTableConverter<TagVO.Tag, SysTag>, IBaseWrapper<TagVO.Tag> {

    @Override
    public void wrap(TagVO.Tag tag) {
        // tag.setBusinessTypeEnum(BusinessTypeEnum.getByType(tag.getBusinessType()));
        // tag.setQuantityUsed(businessTagService.countByTagId(tag.getId()));
    }

}