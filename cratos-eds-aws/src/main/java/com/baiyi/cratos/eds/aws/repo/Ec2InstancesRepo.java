package com.baiyi.cratos.eds.aws.repo;

import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.eds.aws.model.InstanceModel;
import com.baiyi.cratos.eds.aws.service.Ec2InstancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午2:48
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class Ec2InstancesRepo {

    private final Ec2InstancesService ec2InstancesService;

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.LONG_TERM, key = "'AWS:EC2:INSTANCES'", unless = "#result == null")
    public Map<String, InstanceModel.EC2InstanceType> getInstances() {
        return ec2InstancesService.getInstances();
    }

}
