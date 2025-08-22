package com.baiyi.cratos.ssh.core.builder;

import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.core.model.PodAssetModel;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodStatus;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/24 上午10:20
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshSessionInstanceBuilder {

    private static final String DEFAULT_LOGIN_USER = "SYSTEM";

    public static SshSessionInstance build(String sessionId, HostSystem hostSystem,
                                           SshSessionInstanceTypeEnum sshSessionInstanceTypeEnum, String auditPath) {
        return SshSessionInstance.builder()
                .sessionId(sessionId)
                .instanceId(hostSystem.getInstanceId())
                .loginUser(hostSystem.getLoginUsername())
                .destIp(hostSystem.getHost())
                .outputSize(0L)
                .startTime(new Date())
                .instanceType(sshSessionInstanceTypeEnum.name())
                .instanceClosed(false)
                .auditPath(auditPath)
                .build();
    }

    public static SshSessionInstance build(String sessionId, ApplicationKubernetesParam.PodRequest pod,
                                           SshSessionInstanceTypeEnum sshSessionInstanceTypeEnum, String auditPath) {
        return SshSessionInstance.builder()
                .sessionId(sessionId)
                .instanceId(pod.getInstanceId())
                .loginUser("-")
                .destIp("-")
                .outputSize(0L)
                .startTime(new Date())
                .instanceType(sshSessionInstanceTypeEnum.name())
                .instanceClosed(false)
                .auditPath(auditPath)
                .build();
    }

    public static SshSessionInstance build(String sessionId, PodAssetModel podAssetModel,
                                           SshSessionInstanceTypeEnum sshSessionInstanceTypeEnum, String auditPath) {
        final String podIP = Optional.of(podAssetModel)
                .map(PodAssetModel::getPod)
                .map(Pod::getStatus)
                .map(PodStatus::getPodIP)
                .orElse("-");
        return SshSessionInstance.builder()
                .sessionId(sessionId)
                .instanceId(podAssetModel.getInstanceId())
                .loginUser("-")
                .destIp(podIP)
                .outputSize(0L)
                .startTime(new Date())
                .instanceType(sshSessionInstanceTypeEnum.name())
                .instanceClosed(false)
                .auditPath(auditPath)
                .build();
    }

}
