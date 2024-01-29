package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.business.BusinessParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.facade.BusinessTagFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.base.BaseBusinessService;
import com.baiyi.cratos.service.factory.BusinessServiceFactory;
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
    public List<BusinessTagVO.BusinessTag> getBusinessTagByBusiness(BusinessParam.GetByBusiness getByBusiness) {
        List<BusinessTag> tags = businessTagService.selectByBusiness(getByBusiness);
        return tags.stream()
                .map(t -> {
                    BusinessTagVO.BusinessTag tag = businessTagWrapper.convert(t);
                    businessTagWrapper.wrap(tag);
                    return tag;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessTagVO.BusinessTag> queryBusinessTagByValue(BusinessTagParam.QueryByValue queryByValue) {
        List<BusinessTag> tags = businessTagService.queryBusinessTagByValue(queryByValue);
        return tags.stream()
                .map(t -> {
                    BusinessTagVO.BusinessTag tag = businessTagWrapper.convert(t);
                    businessTagWrapper.wrap(tag);
                    return tag;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addBusinessTag(BusinessTagParam.AddBusinessTag addBusinessTag) {
        BusinessTag businessTag = addBusinessTag.toTarget();
        BaseBusinessService<?> baseBusinessService = BusinessServiceFactory.getService(addBusinessTag.getBusinessType());
        if (businessTagService.getByUniqueKey(businessTag) == null) {
            businessTagService.add(businessTag);
        }
    }

    @Override
    public void updateBusinessTag(BusinessTagParam.UpdateBusinessTag updateBusinessTag) {
        BusinessTag businessTag = updateBusinessTag.toTarget();
        if (businessTagService.getByUniqueKey(businessTag) != null) {
            businessTagService.updateByPrimaryKey(businessTag);
        }
    }

    @Override
    public void deleteById(int id) {
        businessTagService.deleteById(id);
    }

}
