package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.param.http.robot.RobotParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RobotMapper extends Mapper<Robot> {

    int countResourcesAuthorizedByToken(@Param("token") String token, @Param("resource") String resource);

    List<Robot> queryPageByParam(RobotParam.RobotPageQuery pageQuery);

}