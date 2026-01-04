package com.baiyi.cratos.eds;

import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareDns;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareCertRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareDnsRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareZoneRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.*;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:57
 * @Version 1.0
 */
public class EdsCloudflareTest extends BaseEdsTest<EdsConfigs.Cloudflare> {

    @Autowired
    private EdsAssetService edsAssetService;

    @Resource
    private EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    @Resource
    private TrafficLayerDomainRecordService trafficLayerDomainRecordService;

    @Resource
    private TrafficLayerDomainService trafficLayerDomainService;

    @Resource
    private TrafficRouteService trafficRouteService;

    @Resource
    private TrafficRecordTargetService trafficRecordTargetService;

    @Test
    void zoneTest() {
        EdsConfigs.Cloudflare cf = getConfig(5);
        List<CloudflareZone.Zone> rt = CloudflareZoneRepo.listZones(cf);
        System.out.println(rt);
    }

    @Test
    void certTest() {
        EdsConfigs.Cloudflare cf = getConfig(5);
        List<CloudflareCert.Result> rt = CloudflareCertRepo.listCertificatePacks(
                cf, "5243357f773b873952f7f99090841934");
        System.out.println(rt);
    }

    @Test
    void certTest2() {
        EdsConfigs.Cloudflare cf = getConfig(5);
        List<CloudflareDns.DnsRecord> dnsRecords = CloudflareDnsRepo.listDnsRecords(
                cf, "747ddd04b8081fc13b31ed2b112d9ea6");
        System.out.println(dnsRecords);
    }

    // Traffic Route

    @SuppressWarnings("unchecked")
    @Test
    void test3() {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(95, EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD.name());
        EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudflareDns.DnsRecord> holder = (EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudflareDns.DnsRecord>) edsInstanceProviderHolderBuilder.newHolder(
                95, EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD.name());
        for (EdsAsset asset : assets) {
            CloudflareDns.DnsRecord dnsRecord = holder.getProvider()
                    .assetLoadAs(asset.getOriginalModel());
            if (!dnsRecord.getType()
                    .equals("CNAME")) {
                System.out.println("非CNAME解析:" + dnsRecord.getName());
                continue;
            }
            if (!dnsRecord.getProxied()) {
                System.out.println("未开启CDN代理:" + dnsRecord.getName());
                continue;
            }
            List<TrafficLayerDomainRecord> trafficLayerDomainRecords = trafficLayerDomainRecordService.queryByRecordName(
                    dnsRecord.getName());
            if (CollectionUtils.isEmpty(trafficLayerDomainRecords)) {
                System.out.println("未录入:" + dnsRecord.getName());
                continue;
            }
            if (trafficLayerDomainRecords.size() > 1) {
                System.out.println("记录数超过1:" + dnsRecord.getName());
                continue;
            }

            TrafficLayerDomainRecord trafficLayerDomainRecord = trafficLayerDomainRecords.getFirst();
            TrafficLayerDomain trafficLayerDomain = trafficLayerDomainService.getById(
                    trafficLayerDomainRecord.getDomainId());
            TrafficRoute trafficRoute = trafficRouteService.getByDomainRecord(trafficLayerDomainRecord.getRecordName());
            if (trafficRoute != null) {
                System.out.println("TrafficRoute配置已存在:" + trafficRoute.getDomainRecord());
                continue;
            }
            String domain = trafficLayerDomain.getRegisteredDomain();
            Integer dnsResolverInstanceId = 0;
            String zoneId = "";



            List<EdsAsset> as1 = edsAssetService.queryInstanceAssetByTypeAndName(
                    94, EdsAssetTypeEnum.AWS_HOSTED_ZONE.name(), domain + ".", false);
            if (!CollectionUtils.isEmpty(as1)) {
                dnsResolverInstanceId = 94;

                zoneId = as1.getFirst().getAssetKey();

            } else {
                List<EdsAsset> as2 = edsAssetService.queryInstanceAssetByTypeAndName(
                        93, EdsAssetTypeEnum.ALIYUN_DOMAIN.name(), domain, false);
                if (!CollectionUtils.isEmpty(as2)) {
                    dnsResolverInstanceId  = 93;
                }
            }
            if (dnsResolverInstanceId == 0) {
                System.out.println("未找到DNS:" + domain);
                continue;
            }
            trafficRoute = TrafficRoute.builder()
                    .domainId(trafficLayerDomainRecord.getDomainId())
                    .domainRecordId(trafficLayerDomainRecord.getId())
                    .domain(domain)
                    .domainRecord(trafficLayerDomainRecord.getRecordName())
                    .dnsResolverInstanceId(dnsResolverInstanceId)
                    .recordType("CNAME")
                    .zoneId(zoneId)
                    .valid(true)
                    .build();
            trafficRouteService.add(trafficRoute);
            TrafficRecordTarget target1 = TrafficRecordTarget.builder()
                    .trafficRouteId(trafficRoute.getId())
                    .recordType(dnsRecord.getType())
                    .resourceRecord(dnsRecord.getName())
                    .recordValue(dnsRecord.getName() + ".cdn.cloudflare.net")
                    .targetType("CLOUDFLARE")
                    .origin(false)
                    .ttl(600L)
                    .valid(true)
                    .build();

            trafficRecordTargetService.add(target1);

            TrafficRecordTarget target2 = TrafficRecordTarget.builder()
                    .trafficRouteId(trafficRoute.getId())
                    .recordType(dnsRecord.getType())
                    .resourceRecord(dnsRecord.getName())
                    .recordValue(dnsRecord.getContent())
                    .targetType("ALIYUN_ALB")
                    .origin(true)
                    .ttl(600L)
                    .valid(true)
                    .build();

            trafficRecordTargetService.add(target2);


        }
    }

    private Integer queryDnsInstanceId(String domain) {
        List<EdsAsset> as1 = edsAssetService.queryInstanceAssetByTypeAndName(
                94, EdsAssetTypeEnum.AWS_HOSTED_ZONE.name(), domain + ".", false);
        if (!CollectionUtils.isEmpty(as1)) {
            return 94;
        }
        List<EdsAsset> as2 = edsAssetService.queryInstanceAssetByTypeAndName(
                93, EdsAssetTypeEnum.ALIYUN_DOMAIN.name(), domain, false);
        if (!CollectionUtils.isEmpty(as2)) {
            return 93;
        }
        return 0;
    }

}
