package com.baiyi.cratos.wrapper.project;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ProjectLoadBalancer;
import com.baiyi.cratos.domain.view.project.ProjectVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/2 15:30
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectLoadBalancerWrapper extends BaseDataTableConverter<ProjectVO.LoadBalancer, ProjectLoadBalancer> implements BaseWrapper<ProjectVO.LoadBalancer> {

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.EDS_INSTANCE})
    public void wrap(ProjectVO.LoadBalancer vo) {
    }

}