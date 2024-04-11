package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.eds.aws.model.InstanceModel;
import com.baiyi.cratos.eds.aws.repo.Ec2InstancesRepo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午2:57
 * @Version 1.0
 */
public class Ec2InstancesRepoTest extends BaseUnit {

    @Resource
    private Ec2InstancesRepo ec2InstancesRepo;

    @Test
    void test() {
        Map<String, InstanceModel.EC2InstanceType> map = ec2InstancesRepo.getInstances();
        System.out.println(map);
    }

}
