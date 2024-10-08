package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 实现类上本不需要太多代码
 *
 * @Author baiyi
 * @Date 2024/1/5 10:23
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.BUSINESS_TAG)
public class BusinessTagWrapper extends BaseDataTableConverter<BusinessTagVO.BusinessTag, BusinessTag> implements IBusinessWrapper<BusinessTagVO.HasBusinessTags, BusinessTagVO.BusinessTag> {

    private final BusinessTagService businessTagService;

    @Override
    @BusinessWrapper(ofTypes = BusinessTypeEnum.TAG)
    public void wrap(BusinessTagVO.BusinessTag vo) {
        // This is a good idea
    }

    @Override
    public void businessWrap(BusinessTagVO.HasBusinessTags businessTags) {
        businessTags.setBusinessTags(businessTagService.selectByBusiness(businessTags)
                .stream()
                .map(bizTag -> {
                    BusinessTagVO.BusinessTag businessTag = this.convert(bizTag);
                    // AOP增强
                    wrapFromProxy(businessTag);
                    return businessTag;
                })
                .collect(Collectors.toList()));
    }

}