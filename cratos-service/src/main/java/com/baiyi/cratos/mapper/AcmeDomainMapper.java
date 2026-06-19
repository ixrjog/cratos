package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;
import com.baiyi.cratos.domain.view.acme.AcmeDomainVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AcmeDomainMapper extends Mapper<AcmeDomain> {

    List<AcmeDomain> queryPageByParam(AcmeDomainParam.DomainPageQuery pageQuery);

    List<AcmeDomainVO.DomainGroup> queryDistinctDomain();

    List<String> queryDistinctDomains(@Param("domain") String domain);

}