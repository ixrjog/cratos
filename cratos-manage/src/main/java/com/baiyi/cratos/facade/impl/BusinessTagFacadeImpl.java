package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.BusinessException;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.facade.impl.base.BaseSupportBusinessFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.wrapper.BusinessTagWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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
    public BusinessTag getBusinessTag(BaseBusiness.HasBusiness hasBusiness, String tagKey) {
        Tag tag = tagService.getByTagKey(tagKey);
        if (Objects.isNull(tag)) {
            return null;
        }
        return getBusinessTag(hasBusiness, tag.getId());
    }

    private BusinessTag getBusinessTag(BaseBusiness.HasBusiness hasBusiness, int tagId) {
        BusinessTag uniqueKey = BusinessTag.builder()
                .businessType(hasBusiness.getBusinessType())
                .businessId(hasBusiness.getBusinessId())
                .tagId(tagId)
                .build();
        return businessTagService.getByUniqueKey(uniqueKey);
    }

    @Override
    public boolean containsTag(String businessType, Integer businessId, Integer tagId) {
        BusinessTag uniqueKey = BusinessTag.builder()
                .businessType(businessType)
                .businessId(businessId)
                .tagId(tagId)
                .build();
        return businessTagService.getByUniqueKey(uniqueKey) != null;
    }

    @Override
    public boolean containsTag(String businessType, Integer businessId, String tagKey) {
        Tag tag = tagService.getByTagKey(tagKey);
        if (tag == null) {
            return false;
        }
        return containsTag(businessType, businessId, tag.getId());
    }

    @Override
    public void deleteById(int id) {
        businessTagService.deleteById(id);
    }

}
