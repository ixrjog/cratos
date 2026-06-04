package com.baiyi.cratos.eds.aliyun.facade;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ProjectLoadBalancer;
import com.baiyi.cratos.domain.view.project.ProjectLoadBalancerVO;
import com.baiyi.cratos.eds.aliyun.model.AliyunAlb;
import com.baiyi.cratos.eds.aliyun.model.AliyunClb;
import com.baiyi.cratos.eds.aliyun.model.AliyunNlb;
import com.baiyi.cratos.eds.aliyun.repo.AliyunClbRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunNlbRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/25 18:49
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunLoadBalancerFacade {

    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final EdsAssetService edsAssetService;

    @SuppressWarnings("unchecked")
    public ProjectLoadBalancerVO.LoadBalancer getLoadBalancer(
            ProjectLoadBalancer projectLoadBalancer) throws Exception {
        EdsAsset asset = edsAssetService.getById(projectLoadBalancer.getAssetId());
        EdsInstanceProviderHolder<EdsConfigs.Aliyun, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Aliyun, ?>) edsProviderHolderFactory.createHolder(
                asset.getInstanceId(), asset.getAssetType());
        String instanceName = holder.getInstance()
                .getEdsInstance()
                .getInstanceName();
        EdsConfigs.Aliyun aliyun = holder.getInstance()
                .getConfig();
        ProjectLoadBalancerVO.LbConfig lbConfig = ProjectLoadBalancerVO.loadAs(projectLoadBalancer);
        if (EdsAssetTypeEnum.ALIYUN_CLB.name()
                .equals(asset.getAssetType())) {
            AliyunClb.Clb aliyunClb = (AliyunClb.Clb) holder.getProvider()
                    .loadAsset(asset.getOriginalModel());
            return getLoadBalancer(aliyun, instanceName, aliyunClb, lbConfig);
        }
        if (EdsAssetTypeEnum.ALIYUN_ALB.name()
                .equals(asset.getAssetType())) {
            AliyunAlb.Alb aliyunAlb = (AliyunAlb.Alb) holder.getProvider()
                    .loadAsset(asset.getOriginalModel());
            return getLoadBalancer(aliyun, instanceName, aliyunAlb, lbConfig);
        }
        if (EdsAssetTypeEnum.ALIYUN_NLB.name()
                .equals(asset.getAssetType())) {
            AliyunNlb.Nlb aliyunNlb = (AliyunNlb.Nlb) holder.getProvider()
                    .loadAsset(asset.getOriginalModel());
            return getLoadBalancer(aliyun, instanceName, aliyunNlb, lbConfig);
        }
        return ProjectLoadBalancerVO.LoadBalancer.NO_DATA;
    }

    private ProjectLoadBalancerVO.LoadBalancer getLoadBalancer(EdsConfigs.Aliyun aliyun, String instanceName,
                                                               AliyunAlb.Alb aliyunAlb,
                                                               ProjectLoadBalancerVO.LbConfig lbConfig) throws Exception {
        // TODO
        return ProjectLoadBalancerVO.LoadBalancer.NO_DATA;
    }

    private ProjectLoadBalancerVO.LoadBalancer getLoadBalancer(EdsConfigs.Aliyun aliyun, String instanceName,
                                                               AliyunNlb.Nlb aliyunNlb,
                                                               ProjectLoadBalancerVO.LbConfig lbConfig) throws Exception {
        List<ProjectLoadBalancerVO.Listener> listeners = AliyunNlbRepo.listListeners(
                        aliyunNlb.getEndpoint(), aliyun, aliyunNlb.getLoadBalancers()
                                .getLoadBalancerId()
                )
                .stream()
                .map(e -> {
                    List<ProjectLoadBalancerVO.Server> serverGroupServers = List.of();
                    if (StringUtils.hasText(e.getServerGroupId())) {
                        try {
                            serverGroupServers = AliyunNlbRepo.listServerGroupServers(
                                            aliyunNlb.getEndpoint(), aliyun, e.getServerGroupId())
                                    .stream()
                                    .map(s -> ProjectLoadBalancerVO.Server.builder()
                                            .serverId(s.getServerId())
                                            .serverType(s.getServerType())
                                            .serverIp(s.getServerIp())
                                            .port(s.getPort())
                                            .weight(s.getWeight())
                                            .serverGroupId(s.getServerGroupId())
                                            .zoneId(s.getZoneId())
                                            .build())
                                    .toList();
                        } catch (Exception ignored) {
                        }
                    }
                    return ProjectLoadBalancerVO.Listener.builder()
                            .listenerProtocol(e.getListenerProtocol())
                            .listenerPort(e.getListenerPort())
                            .startPort(e.startPort)
                            .endPort(e.endPort)
                            .listenerDescription(e.listenerDescription)
                            .serverGroupId(e.getServerGroupId())
                            .listenerStatus(e.getListenerStatus())
                            .serverGroupServers(serverGroupServers)
                            .build();
                })
                .toList();
        return ProjectLoadBalancerVO.LoadBalancer.builder()
                .instanceName(instanceName)
                .loadBalancerType(EdsAssetTypeEnum.ALIYUN_NLB.name())
                .loadBalancerId(aliyunNlb.getLoadBalancers()
                                        .getLoadBalancerId())
                .loadBalancerName(aliyunNlb.getLoadBalancers()
                                          .getLoadBalancerName())
                .dnsName(aliyunNlb.getLoadBalancers()
                                 .getDNSName())
                .regionId(aliyunNlb.getLoadBalancers()
                                  .getRegionId())
                .listeners(listeners)
                .lbConfig(lbConfig)
                .build();
    }

    private ProjectLoadBalancerVO.LoadBalancer getLoadBalancer(EdsConfigs.Aliyun aliyun, String instanceName,
                                                               AliyunClb.Clb aliyunClb,
                                                               ProjectLoadBalancerVO.LbConfig lbConfig) throws Exception {
        List<ProjectLoadBalancerVO.Listener> listeners = AliyunClbRepo.describeLoadBalancerListeners(
                        aliyunClb.getEndpoint(), aliyun, aliyunClb.getLoadBalancer()
                                .getLoadBalancerId()
                )
                .stream()
                .map(e -> {
                    List<ProjectLoadBalancerVO.Server> serverGroupServers = List.of();
                    if (StringUtils.hasText(e.getVServerGroupId())) {
                        try {
                            serverGroupServers = AliyunClbRepo.describeVServerGroupAttribute(
                                            aliyunClb.getEndpoint(), aliyun, e.getVServerGroupId())
                                    .stream()
                                    .map(s -> ProjectLoadBalancerVO.Server.builder()
                                            .serverId(s.getServerId())
                                            .serverType(s.getType())
                                            .serverIp(s.getServerIp())
                                            .port(s.getPort())
                                            .weight(s.getWeight())
                                            .serverGroupId(e.getVServerGroupId())
                                            .zoneId("--")
                                            .build())
                                    .toList();
                        } catch (Exception ignored) {
                        }
                    }
                    return ProjectLoadBalancerVO.Listener.builder()
                            .listenerProtocol(e.getListenerProtocol()
                                                      .toUpperCase())
                            .listenerPort(e.getListenerPort())
                            .listenerDescription(e.getDescription())
                            .serverGroupId(e.getVServerGroupId())
                            .listenerStatus(StringUtils.capitalize(e.getStatus()))
                            .serverGroupServers(serverGroupServers)
                            .forwardTo("on".equals(e.getHTTPListenerConfig()
                                                           .getListenerForward()) ? e.getHTTPListenerConfig()
                                    .getForwardPort() : null)
                            .build();
                })
                .toList();
        return ProjectLoadBalancerVO.LoadBalancer.builder()
                .instanceName(instanceName)
                .loadBalancerType(EdsAssetTypeEnum.ALIYUN_CLB.name())
                .loadBalancerId(aliyunClb.getLoadBalancer()
                                        .getLoadBalancerId())
                .loadBalancerName(aliyunClb.getLoadBalancer()
                                          .getLoadBalancerName())
                .dnsName(aliyunClb.getAttribute()
                                 .getAddress())
                .regionId(aliyunClb.getLoadBalancer()
                                  .getRegionId())
                .listeners(listeners)
                .lbConfig(lbConfig)
                .build();
    }

}
