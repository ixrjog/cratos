package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.BusinessException;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.business.BusinessParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.facade.BusinessTagFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.SupportBusinessTagService;
import com.baiyi.cratos.service.factory.SupportBusinessTagServiceFactory;
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
    public List<String> queryBusinessTagValue(BusinessTagParam.QueryByValue queryByValue) {
        return businessTagService.queryByValue(queryByValue);
    }

    @Override
    public void saveBusinessTag(BusinessTagParam.SaveBusinessTag saveBusinessTag) {
        SupportBusinessTagService supportBusinessService = SupportBusinessTagServiceFactory.getService(saveBusinessTag.getBusinessType());
        if (supportBusinessService == null) {
            throw new BusinessException("BusinessType {} does not support business tag.", saveBusinessTag.getBusinessType());
        }
        BusinessTag businessTag = saveBusinessTag.toTarget();
        if (supportBusinessService instanceof BaseService<?, ?> baseService) {
            saveBusinessTag(baseService, businessTag);
        } else {
            throw new BusinessException("SupportBusinessTagService does not BaseService.");
        }
    }

    private void saveBusinessTag(BaseService<?, ?> baseService, BusinessTag saveBusinessTag) {
        if (baseService.getById(saveBusinessTag.getBusinessId()) == null) {
            throw new BusinessException("BusinessObject {} does not exist: businessType={}, businessId={}", saveBusinessTag.getBusinessType(), saveBusinessTag.getBusinessId());
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
