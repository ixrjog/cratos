package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerTopologyParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerTopologyVO;
import com.baiyi.cratos.eds.aws.model.AwsCloudFrontDistribution;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareDns;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.TrafficLayerTopologyFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TagService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/16 13:48
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficLayerTopologyFacadeImpl implements TrafficLayerTopologyFacade {

    private final EdsAssetIndexService indexService;
    private final EdsAssetService assetService;
    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final TagService tagService;
    private final EdsFacade edsFacade;
    private final ApplicationResourceService applicationResourceService;

    // CloudFlare DDOS and WAF service instance id
    // TODO 建议改为配置项,避免环境相关的硬编码
    private static final int CF_INSTANCE_ID = 95;
    private static final int AWS_INSTANCE_ID = 94;
    private static final String DEFAULT_NAMESPACE = "prod";
    private static final int INGRESS_INDEX_QUERY_SIZE = 500;
    private static final int DNS_RECORD_PAGE_SIZE = 100;
    // Aliyun ALB DNS name suffixes (international / mainland)
    private static final String ALB_DNS_SUFFIX_INTL = "alb.aliyuncsslbintl.com";
    private static final String ALB_DNS_SUFFIX = "alb.aliyuncs.com";
    // Bounded pool for parallel per-route enrichment (independent CDN / host-header IO queries).
    // Kept modest to stay within the DB connection pool capacity.
    private static final int ENRICH_POOL_SIZE = 8;

    /**
     * Dedicated daemon pool for route enrichment; inline-initialized so it is excluded from the Lombok constructor.
     */
    private final ExecutorService enrichExecutor = Executors.newFixedThreadPool(
            ENRICH_POOL_SIZE, runnable -> {
                Thread thread = new Thread(runnable, "topo-enrich");
                thread.setDaemon(true);
                return thread;
            }
    );

    @PreDestroy
    public void shutdownEnrichExecutor() {
        enrichExecutor.shutdown();
        try {
            if (!enrichExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                enrichExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            enrichExecutor.shutdownNow();
            Thread.currentThread()
                    .interrupt();
        }
    }

    private List<String> queryServiceName(String appName, String namespace) {
        if (!StringUtils.hasText(namespace)) {
            namespace = DEFAULT_NAMESPACE;
        }
        return applicationResourceService.queryApplicationResource(
                        appName, EdsAssetTypeEnum.KUBERNETES_SERVICE.name(), namespace)
                .stream()
                .map(ApplicationResource::getName)
                .toList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public TrafficLayerTopologyVO.Topology queryTrafficLayerTopology(
            TrafficLayerTopologyParam.QueryTrafficLayerTopology queryTrafficLayerTopology) {
        // 1. 应用下的 service
        List<String> serviceNames = queryServiceName(
                queryTrafficLayerTopology.getAppName(), queryTrafficLayerTopology.getNamespace());
        if (CollectionUtils.isEmpty(serviceNames)) {
            return TrafficLayerTopologyVO.Topology.NO_DATA;
        }
        // 2. service 关联的 ingress index
        List<EdsAssetIndex> indices = Lists.newArrayList();
        serviceNames.forEach(serviceName -> indices.addAll(
                indexService.queryIndexByIngressServiceName(
                        serviceName, EdsAssetTypeEnum.KUBERNETES_INGRESS.name(),
                        INGRESS_INDEX_QUERY_SIZE
                )));
        if (CollectionUtils.isEmpty(indices)) {
            return TrafficLayerTopologyVO.Topology.NO_DATA;
        }
        Tag tag = tagService.getByTagKey(SysTagKeys.HOST);
        EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudFlareDns.DnsRecord> cloudFlareProviderHolder = (EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudFlareDns.DnsRecord>) edsProviderHolderFactory.createHolder(
                CF_INSTANCE_ID, EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD.name());
        EdsInstanceProviderHolder<EdsConfigs.Aws, AwsCloudFrontDistribution.Distribution> awsProviderHolder = (EdsInstanceProviderHolder<EdsConfigs.Aws, AwsCloudFrontDistribution.Distribution>) edsProviderHolderFactory.createHolder(
                AWS_INSTANCE_ID, EdsAssetTypeEnum.AWS_CLOUDFRONT_DISTRIBUTION.name());
        // 请求内缓存,避免循环内对同一资产/LB 重复查询(N+1)
        Map<Integer, EdsAsset> assetCache = Maps.newHashMap();
        Map<Integer, String> lbNameCache = Maps.newHashMap();
        // key = lbName
        Map<String, TrafficLayerTopologyVO.TrafficPath> trafficPathMap = Maps.newHashMap();
        // Phase 1 (sequential): build the path/route skeleton; collect newly created routes needing enrichment.
        List<RouteEnrichTask> enrichTasks = Lists.newArrayList();
        indices.forEach(index -> {
            EdsAsset edsAsset = assetCache.computeIfAbsent(index.getAssetId(), assetService::getById);
            if (edsAsset == null) {
                return;
            }
            // Cache negative results ("") too, so assetIds without an LB hostname aren't re-queried.
            String lb = lbNameCache.computeIfAbsent(
                    index.getAssetId(), id -> {
                        String name = getIngressLBName(index);
                        return name != null ? name : "";
                    }
            );
            if (!StringUtils.hasText(lb)) {
                return;
            }
            TrafficLayerTopologyVO.Rule rule = TrafficLayerTopologyVO.Rule.parse(index.getName());
            if (rule == null) {
                return;
            }
            String namespace = edsAsset.getAssetKey()
                    .split(":")[0];
            // LB asset (name/id) is resolved lazily: only on the LB's first appearance (avoids N+1 queries).
            TrafficLayerTopologyVO.TrafficPath trafficPath = trafficPathMap.computeIfAbsent(
                    lb, key -> {
                        EdsAsset lbAsset = queryAliyunALB(lb);
                        return TrafficLayerTopologyVO.TrafficPath.builder()
                                .loadBalancer(TrafficLayerTopologyVO.LoadBalancer.builder()
                                                      .dnsName(lb)
                                                      .loadBalancerName(lbAsset != null ? lbAsset.getName() : null)
                                                      .assetId(lbAsset != null ? lbAsset.getId() : null)
                                                      .build())
                                .routeMap(Maps.newHashMap())
                                .build();
                    }
            );
            TrafficLayerTopologyVO.Route newRoute = addBaseRoute(trafficPath.getRouteMap(), lb, namespace, rule);
            if (newRoute != null) {
                enrichTasks.add(new RouteEnrichTask(trafficPath.getRouteMap(), newRoute));
            }
        });
        // Phase 2 (parallel): enrich each new route (CDN + host-header override) via independent IO queries.
        enrichRoutesInParallel(enrichTasks, cloudFlareProviderHolder, awsProviderHolder, tag);
        return TrafficLayerTopologyVO.Topology.of(queryTrafficLayerTopology.getAppName(), trafficPathMap);
    }

    private EdsAsset queryAliyunALB(String dnsName) {
        if (dnsName.endsWith(ALB_DNS_SUFFIX_INTL) || dnsName.endsWith(ALB_DNS_SUFFIX)) {
            List<EdsAsset> assets = assetService.queryByTypeAndKey(EdsAssetTypeEnum.ALIYUN_ALB.name(), dnsName);
            if (!CollectionUtils.isEmpty(assets)) {
                return assets.getFirst();
            }
        }
        return null;
    }

    /**
     * A newly created route together with the route map it belongs to, awaiting CDN/host-header enrichment.
     */
    private record RouteEnrichTask(Map<String, TrafficLayerTopologyVO.Route> routeMap,
                                   TrafficLayerTopologyVO.Route route) {
    }

    /**
     * 把一条规则合并进 routeMap(不做任何外部查询):
     * - record 已存在则追加 path,返回 null;
     * - record 新增则构建基础 Route 放入 map,并返回该 Route(待后续并行补充 CDN / 主机标头覆盖)。
     */
    private TrafficLayerTopologyVO.Route addBaseRoute(Map<String, TrafficLayerTopologyVO.Route> routeMap, String lb,
                                                      String namespace, TrafficLayerTopologyVO.Rule rule) {
        TrafficLayerTopologyVO.Route existing = routeMap.get(rule.getRecord());
        if (existing != null) {
            if (!existing.getRules()
                    .contains(rule.getPath())) {
                existing.getRules()
                        .add(rule.getPath());
            }
            return null;
        }
        TrafficLayerTopologyVO.Route route = TrafficLayerTopologyVO.Route.builder()
                .record(rule.getRecord())
                .originServer(lb)
                .namespace(namespace)
                .rules(rule.getPaths())
                .build();
        routeMap.put(route.getRecord(), route);
        return route;
    }

    /**
     * 并行补充每条新建 Route 的信息:
     * - populateCdn 只修改各自的 route 对象(对象互不相同,线程安全);
     * - queryHostHeaderOverride 仅返回额外的 route,不修改共享 map。
     * 所有外部查询并行执行,最后按顺序串行合并结果,避免对 routeMap 的并发写入。
     */
    private void enrichRoutesInParallel(List<RouteEnrichTask> tasks,
                                        EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudFlareDns.DnsRecord> cloudFlareHolder,
                                        EdsInstanceProviderHolder<EdsConfigs.Aws, AwsCloudFrontDistribution.Distribution> awsHolder,
                                        Tag tag) {
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        List<CompletableFuture<List<TrafficLayerTopologyVO.Route>>> futures = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(
                        () -> {
                            List<TrafficLayerTopologyVO.Route> extras = Lists.newArrayList();
                            try {
                                // CloudFlare: 命中则补充当前 route 的 CDN/proxied,并追加主机标头覆盖的域名
                                if (!populateCloudFlare(cloudFlareHolder, task.route())) {
                                    populateCloudFront(awsHolder, task.route());
                                }
                                extras.addAll(queryHostHeaderOverride(cloudFlareHolder, task.route(), tag));
                                // AWS CloudFront: 追加该 record 命中的 CloudFront 分配(CDN)
                                extras.addAll(queryCloudFrontCDN(awsHolder, task.route(), tag));
                            } catch (Exception e) {
                                log.warn(
                                        "Enrich route failed: record={}", task.route()
                                                .getRecord(), e
                                );
                            }
                            return extras;
                        }, enrichExecutor
                ))
                .toList();
        // 串行合并:对每个 routeMap 单线程写入,保持 putIfAbsent 语义。
        for (int i = 0; i < tasks.size(); i++) {
            Map<String, TrafficLayerTopologyVO.Route> routeMap = tasks.get(i)
                    .routeMap();
            futures.get(i)
                    .join()
                    .forEach(r -> routeMap.putIfAbsent(r.getRecord(), r));
        }
    }

    private boolean populateCloudFlare(
            EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudFlareDns.DnsRecord> edsInstanceProviderHolder,
            TrafficLayerTopologyVO.Route route) {
        route.setProxied(false);
        List<EdsAsset> dnsRecordAssets = assetService.queryInstanceAssetByTypeAndName(
                CF_INSTANCE_ID, EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD.name(), route.getRecord(), false);
        if (CollectionUtils.isEmpty(dnsRecordAssets)) {
            return false;
        }
        CloudFlareDns.DnsRecord dnsRecord = edsInstanceProviderHolder.getProvider()
                .loadAsset(dnsRecordAssets.getFirst()
                                   .getOriginalModel());
        // 源站匹配时才认为命中该 CDN 记录
        if (Objects.equals(dnsRecord.getContent(), route.getOriginServer())) {
            route.setProxied(dnsRecord.getProxied());
            route.setCdn(dnsRecord.getName() + ".cdn.cloudflare.net");
            return true;
        }
        return false;
    }

    private void populateCloudFront(
            EdsInstanceProviderHolder<EdsConfigs.Aws, AwsCloudFrontDistribution.Distribution> awsProviderHolder,
            TrafficLayerTopologyVO.Route route) {
        route.setProxied(false);
        Tag tag = tagService.getByTagKey(SysTagKeys.HOST_ALIAS);
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .tagValue(route.getRecord())
                .build();
        EdsInstanceParam.AssetPageQuery assetPageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .assetType(EdsAssetTypeEnum.AWS_CLOUDFRONT_DISTRIBUTION.name())
                .instanceId(AWS_INSTANCE_ID)
                .queryByTag(queryByTag)
                .page(1)
                .length(DNS_RECORD_PAGE_SIZE)
                .build();
        DataTable<EdsAssetVO.Asset> dataTable = edsFacade.queryEdsInstanceAssetPage(assetPageQuery);
        if (CollectionUtils.isEmpty(dataTable.getData())) {
            return;
        }
        String cdn = dataTable.getData()
                .stream()
                .map(asset -> awsProviderHolder.getProvider()
                        .loadAsset(asset.getOriginalModel()))
                .filter(e -> {
                    String origin = e.getConfig()
                            .getOrigins()
                            .getFirst()
                            .getDomainName();
                    return Objects.equals(origin, route.getOriginServer());
                })
                .map(e -> e.getDistribution()
                        .getDomainName())
                .collect(Collectors.joining(","));
        if (StringUtils.hasText(cdn)) {
            route.setProxied(true);
            route.setCdn(cdn);
        }
    }

    /**
     * 查询命中该 record 的 AWS CloudFront 分配(按 HOST 标签匹配),为其每个别名(alias)追加一条 CDN route。
     * 语义与 {@link #queryHostHeaderOverride} 类似:返回额外的 route,不修改共享 map。
     */
    private List<TrafficLayerTopologyVO.Route> queryCloudFrontCDN(
            EdsInstanceProviderHolder<EdsConfigs.Aws, AwsCloudFrontDistribution.Distribution> awsProviderHolder,
            TrafficLayerTopologyVO.Route route, Tag tag) {
        if (!StringUtils.hasText(route.getRecord())) {
            return List.of();
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .tagValue(route.getRecord())
                .build();
        EdsInstanceParam.AssetPageQuery assetPageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .assetType(EdsAssetTypeEnum.AWS_CLOUDFRONT_DISTRIBUTION.name())
                .instanceId(AWS_INSTANCE_ID)
                .queryByTag(queryByTag)
                .page(1)
                .length(DNS_RECORD_PAGE_SIZE)
                .build();
        DataTable<EdsAssetVO.Asset> dataTable = edsFacade.queryEdsInstanceAssetPage(assetPageQuery);
        if (CollectionUtils.isEmpty(dataTable.getData())) {
            return List.of();
        }
        List<TrafficLayerTopologyVO.Route> routes = Lists.newArrayList();
        dataTable.getData()
                .forEach(asset -> {
                    AwsCloudFrontDistribution.Distribution distribution = awsProviderHolder.getProvider()
                            .loadAsset(asset.getOriginalModel());
                    if (distribution == null || CollectionUtils.isEmpty(distribution.getAliases())) {
                        return;
                    }
                    // 为该分配的每个别名各追加一条 CDN route
                    distribution.getAliases()
                            .forEach(alias -> routes.add(TrafficLayerTopologyVO.Route.builder()
                                                                 .record(alias)
                                                                 .proxied(true)
                                                                 .cdn(asset.getName())
                                                                 .host(route.getRecord())
                                                                 .namespace(route.getNamespace())
                                                                 .originServer(route.getOriginServer())
                                                                 .rules(Lists.newArrayList("/"))
                                                                 .build()));
                });
        return routes;
    }

    /**
     * 增加主机标头覆盖的域名
     *
     * @param edsInstanceProviderHolder
     * @param route
     * @param tag
     * @return
     */
    private List<TrafficLayerTopologyVO.Route> queryHostHeaderOverride(
            EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudFlareDns.DnsRecord> edsInstanceProviderHolder,
            TrafficLayerTopologyVO.Route route, Tag tag) {
        if (!StringUtils.hasText(route.getRecord())) {
            return List.of();
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .tagValue(route.getRecord())
                .build();
        EdsInstanceParam.AssetPageQuery assetPageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .assetType(EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD.name())
                .instanceId(CF_INSTANCE_ID)
                .queryByTag(queryByTag)
                .page(1)
                .length(DNS_RECORD_PAGE_SIZE)
                .build();
        DataTable<EdsAssetVO.Asset> dataTable = edsFacade.queryEdsInstanceAssetPage(assetPageQuery);
        if (CollectionUtils.isEmpty(dataTable.getData())) {
            return List.of();
        }
        List<TrafficLayerTopologyVO.Route> routes = Lists.newArrayList();
        for (EdsAssetVO.Asset asset : dataTable.getData()) {
            CloudFlareDns.DnsRecord dnsRecord = edsInstanceProviderHolder.getProvider()
                    .loadAsset(asset.getOriginalModel());
            if (Objects.equals(dnsRecord.getContent(), route.getOriginServer())) {
                TrafficLayerTopologyVO.Route r = TrafficLayerTopologyVO.Route.builder()
                        .record(dnsRecord.getName())
                        .proxied(dnsRecord.getProxied())
                        .cdn(dnsRecord.getName() + ".cdn.cloudflare.net")
                        .host(route.getRecord())
                        .namespace(route.getNamespace())
                        .originServer(route.getOriginServer())
                        .rules(Lists.newArrayList("/"))
                        .build();
                routes.add(r);
            }
        }
        return routes;
    }

    private String getIngressLBName(EdsAssetIndex edsAssetIndex) {
        EdsAssetIndex ingressLBUniqueKey = EdsAssetIndex.builder()
                .instanceId(edsAssetIndex.getInstanceId())
                .assetId(edsAssetIndex.getAssetId())
                .name(KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME)
                .build();
        EdsAssetIndex ingressLB = indexService.getByUniqueKey(ingressLBUniqueKey);
        return ingressLB != null ? ingressLB.getValue() : null;
    }

}
