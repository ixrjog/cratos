package com.baiyi.cratos.eds.cloudflare;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareDns;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/8 10:13
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
public class EdsCloudFlare2Test extends BaseEdsTest<EdsConfigs.Cloudflare> {

    @Resource
    private EdsAssetService edsAssetService;

    @Resource
    private EdsProviderHolderFactory edsProviderHolderFactory;

    @Test
    void test() {
        EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudFlareDns.DnsRecord> holder = (EdsInstanceProviderHolder<EdsConfigs.Cloudflare, CloudFlareDns.DnsRecord>) edsProviderHolderFactory.createHolder(
                95, EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD.name());
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(95, EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD.name());
        PrettyTable pt = PrettyTable.fieldNames("Domain", "Host", "Type", "Record", "Proxied");
        assets.forEach(asset -> {
            CloudFlareDns.DnsRecord dnsRecord = holder.getProvider()
                    .loadAsset(asset.getOriginalModel());
            DnsRRType dnsType = DnsRRType.valueOf(dnsRecord.getType());
            if (!(DnsRRType.A.equals(dnsType) || DnsRRType.CNAME.equals(dnsType))) {
                return;
            }
            if (dnsRecord.getContent()
                    .equals("alb-6euon50aixdxqwz9z4.eu-central-1.alb.aliyuncs.com")) {
                return;
            }
            pt.addRow(asset.getName(), "--", dnsRecord.getType(), dnsRecord.getContent(), dnsRecord.getProxied());
        });
        System.out.println(pt);
    }

    @Test
    void test2() {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(95, EdsAssetTypeEnum.CLOUDFLARE_IPV4.name());
        String ips = assets.stream()
                .map(EdsAsset::getName)
                .collect(Collectors.joining(","));
        System.out.println(ips);
    }

}
