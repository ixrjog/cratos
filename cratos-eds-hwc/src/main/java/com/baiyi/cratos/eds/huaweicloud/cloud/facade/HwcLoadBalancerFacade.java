package com.baiyi.cratos.eds.huaweicloud.cloud.facade;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ProjectLoadBalancer;
import com.baiyi.cratos.domain.view.project.ProjectLoadBalancerVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.huaweicloud.cloud.model.HwcElb;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcElbRepo;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/1 18:17
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class HwcLoadBalancerFacade {

    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final EdsAssetService edsAssetService;

    @SuppressWarnings("unchecked")
    public ProjectLoadBalancerVO.LoadBalancer getLoadBalancer(
            ProjectLoadBalancer projectLoadBalancer) throws Exception {
        EdsAsset asset = edsAssetService.getById(projectLoadBalancer.getAssetId());
        EdsInstanceProviderHolder<EdsConfigs.Hwc, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Hwc, ?>) edsProviderHolderFactory.createHolder(
                asset.getInstanceId(), asset.getAssetType());
        String instanceName = holder.getInstance()
                .getEdsInstance()
                .getInstanceName();
        EdsConfigs.Hwc hwc = holder.getInstance()
                .getConfig();
        ProjectLoadBalancerVO.LbConfig lbConfig = ProjectLoadBalancerVO.loadAs(projectLoadBalancer);
        if (EdsAssetTypeEnum.HUAWEICLOUD_ELB.name()
                .equals(asset.getAssetType())) {
            HwcElb.LoadBalancer elb = (HwcElb.LoadBalancer) holder.getProvider()
                    .loadAsset(asset.getOriginalModel());
            return getLoadBalancer(hwc, instanceName, elb, lbConfig);
        }
        return ProjectLoadBalancerVO.LoadBalancer.NO_DATA;
    }

    private ProjectLoadBalancerVO.LoadBalancer getLoadBalancer(EdsConfigs.Hwc hwc, String instanceName,
                                                               HwcElb.LoadBalancer elb,
                                                               ProjectLoadBalancerVO.LbConfig lbConfig) throws Exception {
        List<ProjectLoadBalancerVO.Listener> listeners = HwcElbRepo.listListeners(elb.getRegionId(), hwc, elb.getId())
                .stream()
                .map(e -> {
                    List<ProjectLoadBalancerVO.Server> serverGroupServers = HwcElbRepo.listMembers(
                                    elb.getRegionId(), hwc, e.getDefaultPoolId())
                            .stream()
                            .map(s -> ProjectLoadBalancerVO.Server.builder()
                                    .serverId(s.getId())
                                    .serverType(s.getMemberType())
                                    .serverIp(s.getAddress())
                                    .port(s.getProtocolPort())
                                    .weight(s.getWeight())
                                    .serverGroupId(e.getDefaultPoolId())
                                    .zoneId("--")
                                    .build())
                            .toList();
                    return ProjectLoadBalancerVO.Listener.builder()
                            .listenerProtocol(e.getProtocol())
                            .listenerPort(e.getProtocolPort())
                            .listenerDescription(e.getDescription())
                            .serverGroupId(e.getDefaultPoolId())
                            .listenerStatus(e.getAdminStateUp() ? "Running" : "Stopped")
                            .serverGroupServers(serverGroupServers)
                            .build();
                })
                .toList();
        String dnsName = !CollectionUtils.isEmpty(elb.getPublicips()) ? elb.getPublicips()
                .stream()
                .map(HwcElb.PublicIpInfo::getPublicipAddress)
                .collect(Collectors.joining(",")) : elb.getVipAddress();
        return ProjectLoadBalancerVO.LoadBalancer.builder()
                .instanceName(instanceName)
                .loadBalancerType(EdsAssetTypeEnum.ALIYUN_NLB.name())
                .loadBalancerId(elb.getId())
                .loadBalancerName(elb.getName())
                .dnsName(dnsName)
                .regionId(elb.getRegionId())
                .listeners(listeners)
                .lbConfig(lbConfig)
                .build();
    }

}
