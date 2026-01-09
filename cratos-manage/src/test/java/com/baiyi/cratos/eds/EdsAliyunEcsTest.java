package com.baiyi.cratos.eds;

import com.aliyun.alb20200616.models.ListAclEntriesResponseBody;
import com.aliyun.alb20200616.models.ListAclsResponseBody;
import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupAttributeResponse;
import com.aliyuncs.ecs.model.v20140526.ListTagResourcesResponse;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.aliyun.repo.AliyunAlbRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunSecurityGroupRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunTagRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
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

    @Resource
    private AliyunSecurityGroupRepo aliyunSecurityGroupRepo;

    @Test
    void test2() {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        List<ListTagResourcesResponse.TagResource> tagResources = aliyunTagRepo.listTagResources(
                "eu-central-1", aliyun,
                AliyunTagRepo.ResourceTypes.INSTANCE,
                "i-gw85vfbmx2unzj0y1h3e"
        );
        System.out.println(tagResources);
    }

    @Test
    void test3() {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        List<DescribeSecurityGroupAttributeResponse.Permission> permissions = aliyunSecurityGroupRepo.describeSecurityGroupAttribute(
                "eu-central-1", aliyun, "sg-gw88rpl9ve7xq8q8x3ag");

        for (DescribeSecurityGroupAttributeResponse.Permission permission : permissions) {
            // Accept [ICMP] 0.0.0.0/0 -> -1/-1
            String tpl = "{} [{}]  {} {} {}";
            String out = StringFormatter.arrayFormat(
                    tpl, permission.getPolicy(), permission.getIpProtocol(), permission.getSourceCidrIp(), "->",
                    permission.getSourcePortRange()
            );
            System.out.println(out);

        }
        System.out.println(permissions);
    }

    @Test
    void test4() {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        try {
            List<ListAclsResponseBody.ListAclsResponseBodyAcls> acls = AliyunAlbRepo.listAcls(
                    "alb.eu-central-1.aliyuncs.com", aliyun, "acl-shaiyy6u1aul8vas07");
            System.out.println(acls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void test5() {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        try {
            List<ListAclEntriesResponseBody.ListAclEntriesResponseBodyAclEntries> aclEntries = AliyunAlbRepo.listAclEntries(
                    "alb.eu-central-1.aliyuncs.com", aliyun, "acl-shaiyy6u1aul8vas07");
            System.out.println(aclEntries);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

