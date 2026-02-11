package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.param.http.application.ApplicationResourceBaselineParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ApplicationResourceBaselineMapper extends Mapper<ApplicationResourceBaseline> {

    List<ApplicationResourceBaseline> queryPageByParam(
            ApplicationResourceBaselineParam.ApplicationResourceBaselinePageQuery pageQuery);

}