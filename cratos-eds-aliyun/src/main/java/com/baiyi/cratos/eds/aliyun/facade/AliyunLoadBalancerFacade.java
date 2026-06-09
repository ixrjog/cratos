package com.baiyi.cratos.eds.aliyun.facade;

import com.aliyun.alb20200616.models.GetListenerAttributeResponseBody;
import com.aliyun.alb20200616.models.ListAclEntriesResponseBody;
import com.aliyun.alb20200616.models.ListListenersResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ProjectLoadBalancer;
import com.baiyi.cratos.domain.view.project.ProjectLoadBalancerVO;
import com.baiyi.cratos.eds.aliyun.model.AliyunAlb;
import com.baiyi.cratos.eds.aliyun.model.AliyunClb;
import com.baiyi.cratos.eds.aliyun.model.AliyunNlb;
import com.baiyi.cratos.eds.aliyun.repo.AliyunAlbRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunClbRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunNlbRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/25 18:49
 * &#064;Version 1.0
 */
@Slf4j
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
        Map<String, List<ProjectLoadBalancerVO.Rule>> ruleMap = getRuleMap(aliyun, aliyunAlb);
        List<ProjectLoadBalancerVO.Listener> listeners = AliyunAlbRepo.listListeners(
                        aliyunAlb.getEndpoint(), aliyun, aliyunAlb.getLoadBalancers()
                                .getLoadBalancerId()
                )
                .stream()
                .map(e -> {
                    List<ProjectLoadBalancerVO.Rule> ruleList = List.of();
                    if (ruleMap.containsKey(e.getListenerId())) {
                        ruleList = ruleMap.get(e.getListenerId());
                    }
                    return ProjectLoadBalancerVO.Listener.builder()
                            .listenerProtocol(e.getListenerProtocol())
                            .listenerPort(e.getListenerPort())
                            .listenerDescription(e.getListenerDescription())
                            .listenerStatus(e.getListenerStatus())
                            .serverGroupServers(List.of())
                            .aclList(listListeners(e, aliyun, aliyunAlb))
                            .ruleList(ruleList)
                            .build();
                })
                .toList();
        return ProjectLoadBalancerVO.LoadBalancer.builder()
                .instanceName(instanceName)
                .loadBalancerType(EdsAssetTypeEnum.ALIYUN_ALB.name())
                .loadBalancerId(aliyunAlb.getLoadBalancers()
                                        .getLoadBalancerId())
                .loadBalancerName(aliyunAlb.getLoadBalancers()
                                          .getLoadBalancerName())
                .dnsName(aliyunAlb.getLoadBalancers()
                                 .getDNSName())
                .regionId(aliyunAlb.getRegionId())
                .listeners(listeners)
                .lbConfig(lbConfig)
                .build();
    }

    private Map<String, List<ProjectLoadBalancerVO.Rule>> getRuleMap(EdsConfigs.Aliyun aliyun,
                                                                     AliyunAlb.Alb aliyunAlb) {
        try {
            List<ProjectLoadBalancerVO.Rule> rules = AliyunAlbRepo.listRules(
                            aliyunAlb.getEndpoint(), aliyun, aliyunAlb.getLoadBalancers()
                                    .getLoadBalancerId()
                    )
                    .stream()
                    .map(r -> {
                        List<ProjectLoadBalancerVO.RuleCondition> ruleConditions = r.getRuleConditions()
                                .stream()
                                .map(rc -> ProjectLoadBalancerVO.RuleCondition.builder()
                                        .hostConfig(ProjectLoadBalancerVO.RuleConfig.of(rc.getHostConfig().values))
                                        .pathConfig(ProjectLoadBalancerVO.RuleConfig.of(rc.getPathConfig().values))
                                        .sourceIpConfig(
                                                ProjectLoadBalancerVO.RuleConfig.of(rc.getSourceIpConfig().values))
                                        .type(rc.getType())
                                        .build())
                                .toList();
                        return ProjectLoadBalancerVO.Rule.builder()
                                .ruleId(r.getRuleId())
                                .ruleName(r.getRuleName())
                                .ruleStatus(r.getRuleStatus())
                                .listenerId(r.getListenerId())
                                .loadBalancerId(r.getLoadBalancerId())
                                .ruleConditions(ruleConditions)
                                .build();
                    })
                    .toList();
            return rules.stream()
                    .collect(Collectors.groupingBy(ProjectLoadBalancerVO.Rule::getListenerId));
        } catch (Exception e) {
            return Map.of();
        }
    }

    private List<ProjectLoadBalancerVO.Acl> listListeners(
            ListListenersResponseBody.ListListenersResponseBodyListeners listeners, EdsConfigs.Aliyun aliyun,
            AliyunAlb.Alb aliyunAlb) {
        List<ProjectLoadBalancerVO.Acl> aclList = Lists.newArrayList();
        try {
            GetListenerAttributeResponseBody listenerAttribute = AliyunAlbRepo.getListenerAttribute(
                    aliyunAlb.getEndpoint(), aliyun, listeners.getListenerId());
            String aclType = Optional.ofNullable(listenerAttribute)
                    .map(GetListenerAttributeResponseBody::getAclConfig)
                    .map(GetListenerAttributeResponseBody.GetListenerAttributeResponseBodyAclConfig::getAclType)
                    .orElse("--");
            List<GetListenerAttributeResponseBody.GetListenerAttributeResponseBodyAclConfigAclRelations> aclRelations = Optional.ofNullable(
                            listenerAttribute)
                    .map(GetListenerAttributeResponseBody::getAclConfig)
                    .map(GetListenerAttributeResponseBody.GetListenerAttributeResponseBodyAclConfig::getAclRelations)
                    .orElse(List.of());
            for (GetListenerAttributeResponseBody.GetListenerAttributeResponseBodyAclConfigAclRelations aclRelation : aclRelations) {
                List<ListAclEntriesResponseBody.ListAclEntriesResponseBodyAclEntries> aclEntries = AliyunAlbRepo.listAclEntries(
                        aliyunAlb.getEndpoint(), aliyun, aclRelation.getAclId());
                if (!CollectionUtils.isEmpty(aclEntries)) {
                    ProjectLoadBalancerVO.Acl acl = ProjectLoadBalancerVO.Acl.builder()
                            .aclType(aclType)
                            .aclId(aclRelation.getAclId())
                            .aclEntries(aclEntries.stream()
                                                .map(entry -> ProjectLoadBalancerVO.AclEntry.builder()
                                                        .entry(entry.getEntry())
                                                        .status(entry.getStatus())
                                                        .description(entry.getDescription())
                                                        .build())
                                                .toList())
                            .build();
                    aclList.add(acl);
                }
            }
        } catch (Exception ex) {
            log.warn(
                    "Failed to get listener attribute for listenerId={}: {}", listeners.getListenerId(),
                    ex.getMessage()
            );
        }
        return aclList;
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
