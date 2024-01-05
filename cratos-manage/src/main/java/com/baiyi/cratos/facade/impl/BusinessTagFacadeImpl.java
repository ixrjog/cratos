package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.facade.BusinessTagFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.wrapper.BusinessTagWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/1/5 17:57
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessTagFacadeImpl implements BusinessTagFacade {

    private final BusinessTagService businessTagService;

    private final BusinessTagWrapper businessTagWrapper;

    @Override
    public List<BusinessTagVO.BusinessTag> getBusinessTagByBusiness(BusinessTagParam.GetByBusiness getByBusiness) {
        List<BusinessTag> tags = businessTagService.selectByBusiness(getByBusiness);
        return tags.stream().map(t -> {
            BusinessTagVO.BusinessTag tag = businessTagWrapper.convert(t);
            businessTagWrapper.wrap(tag);
            return tag;
        }).collect(Collectors.toList());
    }

}
