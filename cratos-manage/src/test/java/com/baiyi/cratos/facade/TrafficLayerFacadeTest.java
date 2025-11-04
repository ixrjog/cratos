package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.facade.proxy.TrafficLayerProxy;
import com.baiyi.cratos.service.TrafficLayerDomainService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author baiyi
 * @Date 2024/3/29 16:04
 * @Version 1.0
 */
public class TrafficLayerFacadeTest extends BaseUnit {

    @Resource
    private TrafficLayerFacade trafficLayerFacade;

    @Resource
    private TrafficLayerDomainService trafficLayerDomainService;

    @Resource
    private TrafficLayerProxy trafficLayerProxy;

    @Test
    void test() {
        TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails = TrafficLayerRecordParam.QueryRecordDetails.builder()
                .domainId(1)
                .envName("dev")
                .build();
        TrafficLayerRecordVO.RecordDetails recordDetails = trafficLayerFacade.queryRecordDetails(queryRecordDetails);
        System.out.println(recordDetails);
    }


    @Test
    void test2() {
        TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails = TrafficLayerRecordParam.QueryRecordDetails.builder()
                .domainId(1)
                .envName("daily")
                .build();
        // String table = trafficLayerFacade.queryRecordDetailsStringTable(queryRecordDetails);
        //  System.out.println(table);
    }

    /**
     * 从完整URL或域名中提取注册域名
     *
     * @param url 完整的URL或域名，如www.flexicash.app或https://www.flexicash.app/path
     * @return 注册域名，如flexicash.app
     */
    public static String extractRegisteredDomain(String url) {
        try {
            // 确保URL格式正确，如果没有协议部分，添加一个临时的
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            // 解析URL获取主机名
            URI uri = new URI(url);
            String host = uri.getHost();

            if (host == null) {
                return url; // 无法解析时返回原始输入
            }

            // 使用正则表达式匹配注册域名
            // 这个正则表达式匹配最后两个部分（二级域名和顶级域名）
            Pattern pattern = Pattern.compile("([\\w-]+\\.[a-z]{2,})$");
            Matcher matcher = pattern.matcher(host);

            if (matcher.find()) {
                return matcher.group(1);
            }

            // 处理更复杂的情况，如co.uk等
            pattern = Pattern.compile("([\\w-]+\\.[a-z]{2,3}\\.[a-z]{2})$");
            matcher = pattern.matcher(host);

            if (matcher.find()) {
                return matcher.group(1);
            }

            // 如果无法匹配，返回原始主机名
            return host;

        } catch (URISyntaxException e) {
            // 处理URL解析异常
            return url;
        }
    }

    @Test
    void test3() {
        List<TrafficLayerDomain> list = trafficLayerDomainService.selectAll();
        for (TrafficLayerDomain trafficLayerDomain : list) {
            try {
                String rd = extractRegisteredDomain(trafficLayerDomain.getDomain());
                System.out.println("domain: " + trafficLayerDomain.getDomain() + ", registered domain: " + rd);
                trafficLayerDomain.setRegisteredDomain(rd);
                trafficLayerDomainService.updateByPrimaryKey(trafficLayerDomain);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final String X = """
            h5.palmpay.app	尼日钱包	歆泉	叶飞
            ng-api.fleximfi.com	尼日flexibank	盛林	叶飞
            ad.palmmerchant.com	广告	皮映旭	段新舟
            ng-partner.palmpay.app	小微商户app	周镔涛	张文斌
            admin.palmmerchant.com	KILI运营平台	冯冠军	邹健
            h5.palmmerchant.com		周小将	叶飞
            business.palmpay.com	线上收单	贾文龙	叶飞
            oms.palmpay-inc.com		歆泉	叶飞
            www.palmpay.com	市场部	王浩宇	叶飞
            business.one-loop.com	线上收单	贾文龙	叶飞
            analysis.palmmerchant.com	埋点服务	皮映旭	段新舟
            analytics-prod.palmpay.com			段新舟
            admin.one-loop.com	线上收单	贾文龙	叶飞
            rate.one-loop.com	线上收单	贾文龙	叶飞
            www.flexicash.app		潘藩	张焕敏
            h5.rockitlenders.co		潘藩	张焕敏
            iservice.palmpay-inc.com		林歆泉	叶飞
            checkout.palmpay.com	线上收单	贾文龙	叶飞
            ao-m.palmpay.app			晨睿
            arkapi-prod.fleximfb.com			晨睿
            crm-adapter-prod.palmpay-inc.com			晨睿
            accept.palmpay-inc.com			杨秋华
            accept-prod.palmpay-inc.com			杨秋华
            data-collect-prod.palmpay-inc.com			杨秋华
            fswitch-distribute-prod.palmpay-inc.com	金融网络平台	李建军	郑高超
            ng-channel-callback-prod.palmpay-inc.com		顾丰荣	佳伟
            callback-prod.chuanyinet.com	运营平台基础	冯冠军	邹健
            fswitch-order-prod.palmpay-inc.com			佳伟
            gh-m.palmpay.app			晨睿
            gh-pos-prod.palmmerchant.com	加纳 POS 收单	袁志鑫	张文斌
            mgw-core-aws-inner-prod.palmpay-inc.com			晨睿
            ke-m.palmpay.app			晨睿
            manage-gw.palmpay-inc.com			晨睿
            api-prod.palmmerchant.com			邹健
            merchant-gateway-frankfurt-prod.palmpay-inc.com			邹健
            mgw-core-aliyun-pt.palmpay-inc.com			晨睿
            mgw-core-london-prod.palmpay-inc.com			晨睿
            mgw-core-aws-prod.palmpay-inc.com			晨睿
            mgw-core-aws-prod.techsharehk.com			晨睿
            rpc-frankfurt-prod.palmpay-inc.com			晨睿
            nebula-prod.palmpay-inc.com			晨睿
            ng-channel-access-prod.palmpay-inc.com			佳伟
            ng-m.palmpay.app	尼日钱包	盛林	
            ng-m-b.palmpay.app			佳伟
            ng-pos.palmmerchant.com	尼日 POS 收单	袁志鑫	张文斌
            ng-qrios-prod.palmpay-inc.com			佳伟
            open-gw-prod.palmpay-inc.com	开放网关	张国	张文斌
            platform.fleximfb.com			张焕敏
            scheduler-prod.transspay.net			王义林
            scheduler.palmmerchant.com			王义林
            prod-scheduler.palmmerchant.com			王义林
            tmock-prod.palmpay-inc.com			
            tz-api.sasacash.app			晨睿
            tz-api.sharpmfi.co			晨睿
            tz-infobip-prod.palmpay-inc.com			佳伟
            tz-m.palmpay.app			晨睿
            tz-pos-prod.palmmerchant.com	坦桑 POS 收单	袁志鑫	张文斌
            ug-m.palmpay.app			雷权
            ussd.palmpay.com		雷权	段新舟
            money-in-prod.palmpay-inc.com			佳伟
            ng-kuda-prod.palmpay-inc.com		顾丰荣	佳伟
            ng-etranzact-prod.palmpay-inc.com		顾丰荣	佳伟
            ng-access-prod.palmpay-inc.com			佳伟
            ng-zenith-prod.palmpay-inc.com		顾丰荣	佳伟
            ng-mfs-prod.palmpay-inc.com			佳伟
            ng-habaripay-prod.palmpay-inc.com		顾丰荣	佳伟
            ng-hydrogen-prod.palmpay-inc.com		顾丰荣	佳伟
            tz-fasthub-prod.palmpay-inc.com		顾丰荣	佳伟
            ng-interswitch-prod.palmpay-inc.com		顾丰荣	佳伟
            gh-appsmobile-prod.palmpay-inc.com			佳伟
            tz-tigopesa-prod.palmpay-inc.com		顾丰荣	佳伟
            tz-airtel-prod.palmpay-inc.com		顾丰荣	佳伟
            tz-selcom-prod.palmpay-inc.com		顾丰荣	佳伟
            tz-halotel-prod.palmpay-inc.com			佳伟
            tz-halopesa-prod.palmpay-inc.com			佳伟
            gh-gtb-transfer-prod.palmpay-inc.com			佳伟
            nip-nibss.palmpay.com			佳伟
            pay-route-prod.palmpay-inc.com	Channel Gateway	徐嘉琪	佳伟
            pay-route-proxy-prod.palmpay-inc.com	Channel Gateway	徐嘉琪	佳伟
            nibss-prod.palmpay-inc.com		顾丰荣	佳伟
            paynet-switch-center-prod.palmpay-inc.com			佳伟
            ng-mtnbucket-channel-sit.palmpay-inc.com	Channel Gateway	陈勇 Ethan	佳伟
            mgw-core-aliyun-sit.palmpay-inc.com			晨睿
            mgw-core-aliyun-inner-sit.palmpay-inc.com			晨睿
            channel-doc-daily.palmpay-inc.com			佳伟
            tz-mpesa-prod.palmpay-inc.com			佳伟
            ng-common-callback-prod.palmpay-inc.com	Channel Gateway	陈勇 Ethan	佳伟
            api-docs-daily.palmpay-inc.com			
            athena-gio-daily.palmpay.com			
            financial-testplatform-daily.palmpay-inc.com			
            portal.palmmerchant.com	老商户平台	贾文龙	叶飞
            qa-basic-service-frankfurt-daily.palmpay-inc.com			
            test-ad.transspay.net			
            urule-web.palmpay-inc.com			
            isp-mng-dev.palmpay-inc.com			
            ng-pa-app-dev.transspay.net	小微app测试	周镔涛	张文斌
            ng-pa-apptest.transspay.net	小微app测试	周镔涛	张文斌
            analysis-flexi.palmmerchant.com			段新舟
            admin.fleximfb.com		周小将	叶飞
            h5.fleximfb.com		周小将	叶飞
            nile.palmpay.com		周小将	叶飞
            h5.flexi-bank.com		歆泉	叶飞
            oms.flexi-bank.com	运营平台OMS-FlexiBank主体对外合规使用	冯冠军	邹健
            admin.flexi-bank.com	运营平台KILI-FlexiBank主体对外合规使用	冯冠军	邹健
            ng-app.flexi-bank.com			晨睿
            callback-a-prod.palmpay.com			
            www.flexi-bank.com	flexi-bank 官网	王浩宇	叶飞
            callback-b-prod.palmpay.com			
            open-api.easeid.ai	线上收单	张国	张文斌
            admin.bloomsmfb.com	运营平台blooms监管平台	冯冠军	邹健
            basecamp.palmpay-inc.com			郑高超
            basecamp-api.palmpay-inc.com			郑高超
            admin.palmpay-inc.com		林歆泉	叶飞
            richapi-prod.fleximfb.com			晨睿
            info.flexi-bank.com			段新舟
            tongdun-mgt.flexi-bank.com		杨金鑫	晨睿
            risk-finger.flexi-bank.com		杨金鑫	晨睿
            accept.flexi-bank.com			杨秋华
            business.jupiter-payment.com	线上收单	徐祯阳	张文斌
            checkout.jupiter-payment.com	线上收单	徐祯阳	张文斌
            admin.jupiter-payment.com	线上收单	徐祯阳	张文斌
            apply-h5.palmpaybd.com		黄玲凤	张焕敏
            www.easeid.ai	线上收单	张国	张文斌
            ng-pos-prod.jupiter-payment.com	尼日 POS 收单	袁志鑫	张文斌
            ptsp.jupiter-payment.com			
            ark.fleximfb.com		潘藩	张焕敏
            urule.palmmerchant.com		黄建强	王义林
            qa-data-center-prod.palmpay-inc.com	Devops	陈树峰	陈树峰
            devops.palmpay-inc.com	Devops	陈树峰	陈树峰
            channel.palmpay-inc.com			佳伟
            fswitch-distribute-proxy-prod.palmpay-inc.com	金融网络平台	李建军	郑高超
            bd-bkash-prod.palmpay-inc.com		丰容	佳伟
            multi-huaweisms-prod.palmpay-inc.com		丰容	佳伟
            multi-itniotech-prod.palmpay-inc.com		丰容	佳伟
            ng-easypay-prod.palmpay-inc.com		顾丰荣	佳伟
            ng-ekedc-prod.palmpay-inc.com			佳伟
            ng-fairmoney-prod.palmpay-inc.com		顾丰荣	佳伟
            ng-moniepoint-prod.palmpay-inc.com		顾丰荣	佳伟
            aliyun-frankfurt-ack-channel-prod-ng-oneloop-channel-prod.palmpay-inc.com	跨云oneloop-channel服务	黄国胜	顾丰荣
            ng-transfer-coralpay-prod.palmpay-inc.com		顾丰荣	佳伟
            ng-up-prod.palmpay-inc.com		顾丰荣	佳伟
            qa-basic-service-channel-prod.palmpay-inc.com	Devops	陈树峰	陈树峰
            scheduler-channel-prod.transspay.net		李思明	王义林
            aliyun-frankfurt-ack-channel-prod-channel-center-prod.palmpay-inc.com	金融网络平台	夏吉晨	郑高超
            aliyun-frankfurt-ack-channel-prod-channel-item-center-prod.palmpay-inc.com	金融网络平台	夏吉晨	郑高超
            aliyun-frankfurt-ack-channel-prod-channel-sms-center-prod.palmpay-inc.com	金融网络平台	夏吉晨	郑高超
            aliyun-frankfurt-ack-channel-prod-ghana-prod.palmpay-inc.com	Channel Gateway	顾丰荣 Aiden	佳伟
            aliyun-frankfurt-ack-channel-prod-ke-channel-prod.palmpay-inc.com	Channel Gateway	潘一泓Ian	佳伟
            aliyun-frankfurt-ack-channel-prod-ng-channel-prod.palmpay-inc.com	Channel Gateway	陈勇 Ethan	佳伟
            aliyun-frankfurt-ack-channel-prod-ng-issuer-isw-channel-prod.palmpay-inc.com	Channel Gateway	陈勇 Ethan	佳伟
            aliyun-frankfurt-ack-channel-prod-posp-outway-prod.palmpay-inc.com	Channel Gateway	陈勇 Ethan	佳伟
            aliyun-frankfurt-ack-frankfurt-prod-account-service-prod.palmpay-inc.com	账务清算域	chenrui 赖晨睿	chenrui 赖晨睿
            aliyun-frankfurt-ack-frankfurt-prod-basic-data-prod.palmpay-inc.com	架构域	麦莽 黄建强	义林
            aliyun-frankfurt-ack-frankfurt-prod-basic-uid-service-prod.palmpay-inc.com	支付平台域	孟文利 Martin	杨洋
            chat-service-prod.palmpay-inc.com		陈瑞骞	晨睿
            webmerchant-api.palmpay.com			晨睿
            aliyun-frankfurt-ack-frankfurt-prod-validator-prod.palmpay-inc.com	基础平台域	Jellal 陈靖	chenrui 赖晨睿
            tz-vodacom-prod.palmpay-inc.com			佳伟
            tz-tigozantel-prod.palmpay-inc.com			佳伟
            aliyun-frankfurt-ack-frankfurt-prod-tz-channel-prod.palmpay-inc.com	Channel Gateway		佳伟
            tz-airtel-ussd-prod.palmpay-inc.com			佳伟
            aliyun-frankfurt-ack-frankfurt-prod-trade-prod.palmpay-inc.com	支付平台域	廖远佩	杨洋
            www.bloomsmfb.com		潘藩	张焕敏
            openapi.palmmerchant.com	线上收单	徐祯阳	张文斌
            postpay.palmmerchant.com			张焕敏
            palmpay-callback.palmpay.app		顾丰荣	佳伟
            aliyun-frankfurt-ack-frankfurt-prod-tagv2-service-prod.palmpay-inc.com	支付平台域	holiday 冯皓祺	杨洋
            aliyun-frankfurt-ack-frankfurt-prod-oneloop-gw-service-prod.palmpay-inc.com	钱包域	皮映旭 phil Pi	新舟
            aliyun-frankfurt-ack-frankfurt-prod-data-carrier-c-prod.palmpay-inc.com	大数据域	Halen 张金弟	秋华
            aliyun-frankfurt-ack-frankfurt-prod-member-center-prod.palmpay-inc.com	基础平台域	Jellal 陈靖	chenrui 赖晨睿
            aliyun-frankfurt-ack-frankfurt-prod-mail-prod.palmpay-inc.com	钱包域	林仲海 John	新舟
            aliyun-frankfurt-ack-frankfurt-prod-query-prod.palmpay-inc.com	理财域	邹健JianZou	邹健JianZou
            aliyun-frankfurt-ack-frankfurt-prod-settlement-prod.palmpay-inc.com	账务清算域	徐锋 Jack	chenrui 赖晨睿
            aliyun-frankfurt-ack-frankfurt-prod-posp-prod.palmpay-inc.com	跨云调用 posp 域名	袁志鑫	张文斌
            aliyun-frankfurt-ack-frankfurt-prod-posp-admin-prod.palmpay-inc.com	跨云调用 posp-admin 域名	袁志鑫	张文斌
            aliyun-frankfurt-ack-frankfurt-prod-mgw-core-prod.palmpay-inc.com			
            www.palmpaybd.com		潘藩	张焕敏
            app-update.palmpay-inc.com	React Native 热更新服务	叶飞	
            apply-h5.sharpmfi.co	【多国手机分期】坦桑手机分销H5		
            apply-h5.palmpay.app			
            info.palmpay.com	尼日短地址服务	林仲海	段新舟
            link.palmpaybd.com	孟加拉短地址服务	林仲海	段新舟
            partner-api-prod.palmpay.app	【多国手机分期】尼日手机分销App	潘鑫	张文斌
            userguide.palmpay.com			
            liveness.easeid.ai	线上收单	贾文龙	叶飞
            ui-mobile.palmpay-inc.com	组件库	贾文龙	叶飞
            sre-basecamp.palmpay-inc.com			高超
            tz-free-api.sharpmfi.co			
            partner-api-prod.palmpaybd.com	【多国手机分期】孟加拉手机分销App	孙浩	张文斌
            richapi-prod.palmpaybd.com	Rich app		
            api-prod.palmpaybd.com	孟加拉还款app		
            supportcenter.palmpay.com	理财	周小将	周小将
            docs.palmpay.com	线上收单	徐祯阳	张文斌
            docs.easeid.ai	线上收单	张国	张文斌
            ptsp.jupiter-payment.com	线上收单	袁志鑫	张文斌
            portal.easeid.ai	线上收单	张国	张文斌
            mgw-core-prod.oneloop-inc.com	线上收单	黄国胜	张文斌
            partner-api-prod.sharpmfi.co	【多国手机分期】坦桑手机分销App	孙浩	张文斌
            frankfurt-distribute-prod.palmpay-inc.com	金融网络平台	李建军	郑高超
            oneloop-distribute-prod.oneloop-inc.com	金融网络平台	李建军	郑高超
            finance-switch-channel-prod.palmpay-inc.com	金融网络平台	李建军	郑高超
            """;

    @Test
    void test4() {
        System.out.println("解析 Excel 数据：");
        System.out.println("字段：Domain | Business(研发填写) | Owner | TL");
        System.out.println("=======================");

        String[] lines = X.split("\n");
        for (String line : lines) {
            if (line.trim()
                    .isEmpty()) continue;

            String[] fields = line.split("\t");
            String domain = fields.length > 0 ? fields[0].trim() : "";
            String business = fields.length > 1 ? fields[1].trim() : "";
            String owner = fields.length > 2 ? fields[2].trim() : "";
            String tl = fields.length > 3 ? fields[3].trim() : "";

            System.out.printf("域名: %-50s | 业务: %-20s | 负责人: %-10s | TL: %-10s%n", domain, business, owner, tl);
        }

        System.out.println("\n总计域名数量: " + lines.length);
    }

}
