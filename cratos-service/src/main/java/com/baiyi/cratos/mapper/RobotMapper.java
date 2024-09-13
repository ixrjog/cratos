package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Robot;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface RobotMapper extends Mapper<Robot> {

    int countResourcesAuthorizedByToken(@Param("token") String token, @Param("resource") String resource);

}