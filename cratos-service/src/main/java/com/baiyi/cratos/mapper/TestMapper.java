package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Test;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface TestMapper extends Mapper<Test> {
}