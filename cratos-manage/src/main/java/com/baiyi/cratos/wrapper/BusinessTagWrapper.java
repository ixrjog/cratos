package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.base.IDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/1/5 10:23
 * @Version 1.0
 */
@Slf4j
@Component
// 懒加载防止循环依赖
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class BusinessTagWrapper implements IDataTableConverter<BusinessTagVO.BusinessTag, BusinessTag>, IBusinessWrapper<BusinessTagVO.IBusinessTags, BusinessTagVO.BusinessTag> {

    private final BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.BUSINESS_TAG;

    private final BusinessTagWrapper businessTagWrapper;

    private final BusinessTagService businessTagService;

    @Override
    public Class<BusinessTagVO.BusinessTag> getTargetClazz() {
        return BusinessTagVO.BusinessTag.class;
    }

    @Override
    public IBaseWrapper<BusinessTagVO.BusinessTag> getBean() {
        return businessTagWrapper;
    }

    @Override
    public String getBusinessType() {
        return businessTypeEnum.name();
    }

    @Override
    @BusinessWrapper(businessEnums = {BusinessTypeEnum.TAG})
    public void wrap(BusinessTagVO.BusinessTag businessTag) {
        // TODO
    }

    @Override
    public void businessWrap(BusinessTagVO.IBusinessTags businessTags) {
        businessTags.setBusinessTags(businessTagService.selectByBusiness(businessTags).stream().map(bizTag -> {
            BusinessTagVO.BusinessTag businessTag = this.convert(bizTag);
            // AOP增强
            businessTagWrapper.wrap(businessTag);
            return businessTag;
        }).collect(Collectors.toList()));
    }

}