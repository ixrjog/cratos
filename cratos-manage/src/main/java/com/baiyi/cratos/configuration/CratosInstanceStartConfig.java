package com.baiyi.cratos.configuration;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.GitCommitUtils;
import com.baiyi.cratos.common.util.HostUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.enums.InstanceHealthStatus;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.CratosInstance;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.CratosInstanceService;
import com.baiyi.cratos.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 15:16
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CratosInstanceStartConfig implements CommandLineRunner {

    private final CratosInstanceService cratosInstanceService;
    private final TagService tagService;
    private final BusinessTagService businessTagService;

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${spring.application.version}")
    private String version;

    public static final InetAddress INET_ADDRESS = CratosInstanceStartConfig.getInetAddress();

    @Override
    public void run(String... args) throws Exception {
        this.register();
    }

    private static InetAddress getInetAddress() {
        try {
            return HostUtils.getInetAddress();
        } catch (UnknownHostException ignored) {
            return null;
        }
    }

    private void register() throws UnknownHostException {
        InetAddress inetAddress = HostUtils.getInetAddress();
        // 已存在
        CratosInstance instance = cratosInstanceService.getByHostIp(inetAddress.getHostAddress());
        if (instance != null) {
            instance.setStartTime(new Date());
            instance.setVersion(version);
            instance.setCommit(getCommit());
            cratosInstanceService.updateByPrimaryKey(instance);
        } else {
            registerNewInstance(inetAddress);
        }
    }

    private void registerNewInstance(InetAddress inetAddress) {
        CratosInstance instance = CratosInstance.builder()
                .hostIp(inetAddress.getHostAddress())
                .hostname(inetAddress.getHostName())
                .name(inetAddress.getCanonicalHostName())
                .status(InstanceHealthStatus.OK.name())
                .valid(true)
                .version(version)
                .commit(getCommit())
                .startTime(new Date())
                .build();
        cratosInstanceService.add(instance);
        // 打Env标签
        Tag envTag = tagService.getByTagKey(SysTagKeys.ENV);
        if (Objects.nonNull(envTag)) {
            BusinessTag businessTag = BusinessTag.builder()
                    .tagId(envTag.getId())
                    .businessType(BusinessTypeEnum.CRATOS_INSTANCE.name())
                    .businessId(instance.getId())
                    .tagValue(env)
                    .build();
            businessTagService.add(businessTag);
        }
    }

    private String getCommit() {
        return GitCommitUtils.readGitProperties();
    }

}
