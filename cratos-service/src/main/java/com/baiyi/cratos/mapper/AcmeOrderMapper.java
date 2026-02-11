package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.AcmeOrder;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AcmeOrderMapper extends Mapper<AcmeOrder> {
}