package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.BindAssetsAfterImport;
import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.param.domain.DomainParam;
import com.baiyi.cratos.domain.view.domain.DomainVO;
import com.baiyi.cratos.facade.DomainFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.DomainService;
import com.baiyi.cratos.wrapper.DomainWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:17
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainFacadeImpl implements DomainFacade {

    private final DomainService domainService;

    private final BusinessTagService businessTagService;

    private final DomainWrapper domainWrapper;

    @Override
    @PageQueryByTag(ofType = BusinessTypeEnum.DOMAIN)
    public DataTable<DomainVO.Domain> queryDomainPage(DomainParam.DomainPageQuery pageQuery) {
//        if (pageQuery.isQueryByTag()) {
//            BusinessTagParam.QueryByTag queryByTag = pageQuery.getQueryByTag();
//            queryByTag.setBusinessType(BusinessTypeEnum.DOMAIN.name());
//            List<Integer> idList = businessTagService.queryBusinessIdByTag(queryByTag);
//            pageQuery.setIdList(idList);
//        } else {
//            pageQuery.setIdList(Collections.emptyList());
//        }
        DomainParam.DomainPageQueryParam param = pageQuery.toParam();
        DataTable<Domain> table = domainService.queryDomainPage(param);
        return domainWrapper.wrapToTarget(table);
    }

    @Override
    public void setDomainValidById(int id) {
        domainService.updateValidById(id);
    }

    @Override
    @BindAssetsAfterImport
    public Domain addDomain(DomainParam.AddDomain addDomain) {
        Domain domain = addDomain.toTarget();
        domainService.add(domain);
        return domain;
    }

    @Override
    public void updateDomain(DomainParam.UpdateDomain updateDomain) {
        Domain domain = domainService.getById(updateDomain.getId());
        if (domain == null) {
            return;
        }
        domain.setExpiry(updateDomain.getExpiry());
        domain.setComment(updateDomain.getComment());
        domainService.updateByPrimaryKey(domain);
    }

    @Override
    public void deleteById(int id) {
        domainService.deleteById(id);
    }

}
