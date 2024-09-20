package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.BusinessException;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.business.BusinessParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.facade.BusinessTagFacade;
import com.baiyi.cratos.facade.impl.base.BaseSupportBusinessFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.base.BaseService;
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
public class BusinessTagFacadeImpl extends BaseSupportBusinessFacade<BusinessTag> implements BusinessTagFacade {

    private final BusinessTagService businessTagService;
    private final BusinessTagWrapper businessTagWrapper;
    private final TagService tagService;

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
    public List<String> queryBusinessTagValue(BusinessTagParam.QueryByTag queryByValue) {
        return businessTagService.queryByValue(queryByValue);
    }

    @Override
    public void saveBusinessTag(BusinessTagParam.SaveBusinessTag saveBusinessTag) {
        BusinessTag businessTag = saveBusinessTag.toTarget();
        BaseService<?, ?> baseService = getBusinessService(businessTag);
        saveBusinessTag(baseService, businessTag);
    }

    private void saveBusinessTag(BaseService<?, ?> baseService, BusinessTag saveBusinessTag) {
        if (baseService.getById(saveBusinessTag.getBusinessId()) == null) {
            throw new BusinessException("BusinessObject {} does not exist: businessType={}, businessId={}",
                    saveBusinessTag.getBusinessType(), saveBusinessTag.getBusinessId());
        }
        if (saveBusinessTag.getId() != null) {
            businessTagService.updateByPrimaryKey(saveBusinessTag);
        } else {
            BusinessTag existBusinessTag = businessTagService.getByUniqueKey(saveBusinessTag);
            if (existBusinessTag == null) {
                businessTagService.add(saveBusinessTag);
            } else {
                existBusinessTag.setTagValue(saveBusinessTag.getTagValue());
                businessTagService.updateByPrimaryKey(existBusinessTag);
            }
        }
    }

    @Override
    public void deleteById(int id) {
        businessTagService.deleteById(id);
    }

}
