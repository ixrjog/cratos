package com.baiyi.cratos.eds;

import com.aliyuncs.ecs.model.v20140526.ListTagResourcesResponse;
import com.baiyi.cratos.eds.aliyun.repo.AliyunTagRepo;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/18 16:18
 * &#064;Version 1.0
 */
public class EdsAliyunEcsTest extends BaseEdsTest<EdsConfigs.Aliyun> {

    @Resource
    private AliyunTagRepo aliyunTagRepo;

    @Test
    void test2() {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        List<ListTagResourcesResponse.TagResource> tagResources = aliyunTagRepo.listTagResources("eu-central-1",
                aliyun, AliyunTagRepo.ResourceTypes.INSTANCE, "i-gw85vfbmx2unzj0y1h3e");

        System.out.println(tagResources);
    }

}

