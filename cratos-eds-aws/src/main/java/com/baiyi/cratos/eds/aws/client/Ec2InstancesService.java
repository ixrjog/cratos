package com.baiyi.cratos.eds.aws.client;

import com.baiyi.cratos.eds.aws.model.InstanceModel;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午2:41
 * @Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface Ec2InstancesService {

    @GetExchange("/ec2details/api/ec2instances.json")
    Map<String, InstanceModel.EC2InstanceType> getInstances();

}