package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.domain.param.http.acme.AcmeOrderParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AcmeOrderMapper extends Mapper<AcmeOrder> {

    List<AcmeOrder> queryPageByParam(AcmeOrderParam.OrderPageQuery pageQuery);

}