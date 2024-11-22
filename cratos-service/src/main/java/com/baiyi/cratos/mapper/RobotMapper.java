package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.param.http.robot.RobotParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RobotMapper extends Mapper<Robot> {

    int countResourcesAuthorizedByToken(@Param("token") String token, @Param("resource") String resource);

    List<Robot> queryPageByParam(RobotParam.RobotPageQuery pageQuery);

}