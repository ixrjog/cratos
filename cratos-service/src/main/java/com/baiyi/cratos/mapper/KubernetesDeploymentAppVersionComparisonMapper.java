package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.KubernetesDeploymentAppVersionComparison;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface KubernetesDeploymentAppVersionComparisonMapper extends Mapper<KubernetesDeploymentAppVersionComparison> {
}