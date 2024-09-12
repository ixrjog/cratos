package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.param.domain.DomainParam;
import com.baiyi.cratos.domain.view.domain.DomainVO;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:17
 * @Version 1.0
 */
public interface DomainFacade extends HasSetValid {

    Domain addDomain(DomainParam.AddDomain addDomain);

    void updateDomain(DomainParam.UpdateDomain updateDomain);

    DataTable<DomainVO.Domain> queryDomainPage(DomainParam.DomainPageQuery pageQuery);

    void deleteById(int id);

}
