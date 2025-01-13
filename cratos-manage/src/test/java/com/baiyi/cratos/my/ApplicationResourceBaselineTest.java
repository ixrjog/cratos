package com.baiyi.cratos.my;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.service.ApplicationResourceBaselineService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.BusinessTagService;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 10:09
 * &#064;Version 1.0
 */
public class ApplicationResourceBaselineTest extends BaseUnit {

    @Resource
    private ApplicationService applicationService;
    @Resource
    private ApplicationResourceBaselineService baselineService;
    @Resource
    private BusinessTagService businessTagService;

    private static final int TAG_FRAMEWORK_ID = 27;

    String appFrameworks = """
            oneloop-channel	PPJv2
            oneloop-account	PPJv2
            oneloop-product	PPJv2
            mgw-core-aliyun	PPJvSpringboot2
            ng-palmpayinnertransfer-channel	PPJvSpringboot2
            finance-switch-channel	PPJvSpringboot2
            scheduler-channel	PPJv1
            ng-oneloopwebsite-channel	PPJvSpringboot2
            ng-palmpay-blooms-channel	PPJvSpringboot2
            multi-huaweisms-channel	PPJvSpringboot2
            ng-blooms-server-channel	PPJvSpringboot2
            ng-nibss-blooms-channel	PPJvSpringboot2
            ng-opay-channel	PPJvSpringboot2
            ng-transfer-coralpay-channel	PPJvSpringboot2
            bd-bkash-channel	PPJvSpringboot2
            ng-paripesa-channel	PPJvSpringboot2
            ke-choicebank-channel	PPJvSpringboot2
            ng-pos-kimono-channel	PPJvSpringboot2
            ng-pos-nibss-channel	PPJvSpringboot2
            bd-ssl-channel	PPJvSpringboot2
            bd-porichoy-channel	PPJvSpringboot2
            bd-nagad-channel	PPJvSpringboot2
            ng-mobifin-channel	PPJvSpringboot2
            ng-moniepoint-channel	PPJvSpringboot2
            ng-easywin-channel	PPJvSpringboot2
            ng-moment-channel	PPJvSpringboot2
            ng-card-gtb-channel	PPJvSpringboot2
            ng-naijabet-channel	PPJvSpringboot2
            pos-alert-webhook	PPJvSpringboot2
            ng-betbuzz-channel	PPJvSpringboot2
            ng-demmyglobal-channel	PPJvSpringboot2
            ng-betway-channel	PPJvSpringboot2
            ng-card-paystack-channel	PPJvSpringboot2
            ng-card-uba-channel	PPJvSpringboot2
            ng-bangbet-channel	PPJvSpringboot2
            ng-ilot-channel	PPJvSpringboot2
            ng-nibss-flexibank-channel	PPJvSpringboot2
            ng-flexi-server-channel	PPJvSpringboot2
            ng-palmpay-server-channel	PPJvSpringboot2
            ng-wgb-direct-channel	PPJvSpringboot2
            ng-firs-channel	PPJvSpringboot2
            tz-pos-ni-nbc-channel	PPJvSpringboot2
            ng-ikedc-channel	PPJvSpringboot2
            ng-easypay-channel	PPJvSpringboot2
            multi-itniotech-channel	PPJvSpringboot2
            ng-cgate-channel	PPJvSpringboot2
            ng-phedc-channel	PPJvSpringboot2
            ng-oneloop-channel	PPJvSpringboot2
            ng-betgr8-channel	PPJvSpringboot2
            ng-surebet247-channel	PPJvSpringboot2
            ng-nibsskyc-channel	PPJvSpringboot2
            gh-gtb-transfer-channel	PPJvSpringboot2
            ng-vertofx-channel	PPJvSpringboot2
            tz-halopesa-channel	PPJvSpringboot2
            ng-betbaba-channel	PPJvSpringboot2
            ng-common-callback	PPJvSpringboot2
            ng-betano-channel	PPJvSpringboot2
            ng-betbonanza-channel	PPJvSpringboot2
            tz-mpesa-channel	PPJvSpringboot2
            ng-new-ekedc-channel	PPJvSpringboot2
            ng-axa-channel	PPJvSpringboot2
            ng-glo-channel	PPJvSpringboot2
            ng-accessbet-channel	PPJvSpringboot2
            ke-creditinfo-channel	PPJvSpringboot2
            ng-smile-channel	PPJvSpringboot2
            ng-kedc-channel	PPJvSpringboot2
            ng-betwinner-channel	PPJvSpringboot2
            ng-monokyc-channel	PPJvSpringboot2
            ng-blusalt-channel	PPJvSpringboot2
            ng-credequity-channel	PPJvSpringboot2
            ng-nibss-channel	PPJvSpringboot2
            ng-jedc-channel	PPJvSpringboot2
            tz-selcom-channel	PPJvSpringboot2
            ng-globucketdata-channel	PPJvSpringboot2
            ng-sporty-channel	PPJvSpringboot2
            ng-pos-polaris-channel	PPJvSpringboot2
            ng-hydrogen-channel	PPJvSpringboot2
            ng-interswitch-channel	PPJvSpringboot2
            tz-tigopesa-channel	PPJvSpringboot2
            tz-creditinfo-channel	PPJvSpringboot2
            ng-new-onexbet-channel	PPJvSpringboot2
            ng-betnaija-channel	PPJvSpringboot2
            ng-sterling-channel	PPJvSpringboot2
            tz-airtel-channel	PPJvSpringboot2
            tz-pos-uba-itex-channel	PPJvSpringboot2
            ng-betking-channel	PPJvSpringboot2
            ng-ninemobile-channel	PPJvSpringboot2
            ng-pos-upsl-channel	PPJvSpringboot2
            ng-msport-channel	PPJvSpringboot2
            ng-fcmb-channel	PPJvSpringboot2
            ng-uba-new-channel	PPJvSpringboot2
            finance-switch-distribution	PPJvSpringboot2
            ng-dml-channel	PPJvSpringboot2
            ng-habaripay-channel	PPJvSpringboot2
            ng-africa365-channel	PPJvSpringboot2
            ng-new-fidelity-channel	PPJvSpringboot2
            tz-fasthub-channel	PPJvSpringboot2
            ng-pos-access-channel	PPJvSpringboot2
            ng-issuer-isw-channel	PPJvSpringboot2
            ng-fdc-channel	PPJvSpringboot2
            ng-dojah-channel	PPJvSpringboot2
            ng-airtel-channel	PPJvSpringboot2
            ng-gtb-channel	PPJvSpringboot2
            ng-zenith-channel	PPJvSpringboot2
            ng-fairmoney-channel	PPJvSpringboot2
            ng-irecharge-channel	PPJvSpringboot2
            gh-pos-gtb-channel	PPJvSpringboot2
            gh-pos-uba-itex-channel	PPJvSpringboot2
            ng-up-channel	PPJvSpringboot2
            ng-parimatch-channel	PPJvSpringboot2
            ng-baxi-channel	PPJvSpringboot2
            ng-coralpay-channel	PPJvSpringboot2
            ng-new-buypower-channel	PPJvSpringboot2
            ng-stanbic-channel	PPJvSpringboot2
            ng-tripsdotcom-channel	PPJvSpringboot2
            ng-nomi-channel	PPJvSpringboot2
            ng-geniex-channel	PPJvSpringboot2
            ng-betcorrect-channel	PPJvSpringboot2
            ng-wgb-channel	PPJvSpringboot2
            ng-wajegame-channel	PPJvSpringboot2
            ng-new-mtn-channel	PPJvSpringboot2
            ng-onexbet-channel	PPJvSpringboot2
            ng-etranzact-channel	PPJvSpringboot2
            ng-kuda-channel	PPJvSpringboot2
            ng-fidelity-channel	PPJvSpringboot2
            ng-ekedc-channel	PPJvSpringboot2
            ng-access-channel	PPJvSpringboot2
            ng-nairabet-channel	PPJvSpringboot2
            ng-uba-channel	PPJvSpringboot2
            paynet-ng-iso-channel	PPJvSpringboot2
            tz-channel	PPJv1
            ke-channel	PPJv1
            paystack	PPJv1
            ng-channel	PPJv1
            ghana	PPJv1
            flutterwave	PPJv1
            ng-pos-zone-channel	PPJvSpringboot2
            multi-ycloud-channel	PPJvSpringboot2
            ng-icad-channel	PPJvSpringboot2
            finance-channels	PPJvSpringboot2
            ng-oasis-channel	PPJvSpringboot2
            tz-vodacom-channel	PPJvSpringboot2
            tz-nbc-channel	PPJvSpringboot2
            tz-airtel-ussd-channel	PPJvSpringboot2
            tz-tigozantel-channel	PPJvSpringboot2
            ng-new-qrios-channel	PPJvSpringboot2
            finance-switch-engine	PPJvSpringboot2
            tz-halotel-channel	PPJvSpringboot2
            finance-switch-adapter	PPJvSpringboot2
            tz-infobip-channel	PPJvSpringboot2
            finance-network-front	PPJvSpringboot2
            finance-switch-order	PPJvSpringboot2
            ng-mono-channel	PPJvSpringboot2
            channel-sms-center	PPJvSpringboot2
            ng-qrios-channel	PPJvSpringboot2
            channel-item-center	PPJvSpringboot2
            ng-withhold-channel	PPJvSpringboot2
            ng-postpay-channel	PPJvSpringboot2
            paynet-switch-center	PPJvSpringboot2
            credit-data-service	PPJv1
            scene-data-product	PPJv1
            scene-directlink-product	PPJv1
            credit-scene-service	PPJv1
            rcp-urule-server	PPJvSpringboot2
            credit-product-center	PPJv1
            bill-pressure-test	PPJv1
            posp-channel-route	PPJv1
            posp-channel-encryption	PPJv1
            posp-channel-companion	PPJv1
            basic-uid-service	PPJv1
            tag-a2-query-service	PPJv1
            tag-calculate-core	PPJv1
            flexi-mng	PPJv1
            finance-account-core	PPJv1
            flexi-bff-product	PPJv1
            offline-pay-product	PPJv1
            betting-product	PPJv1
            flexi-service	PPJv1
            c-bff-product	PPJv1
            marketing-core	PPJv1
            posp-admin	PPJv1
            postpay-schedule-task	PPJv1
            posp-outway	PPJv1
            postloan-risk-control	PPJv1
            posp-encryption	PPJv1
            nile-m-bff	PPJv1
            postpay-marketing	PPJv1
            postpay-bigdata	PPJv1
            m-workflow	PPJv1
            loyalty	PPJv1
            coupon	PPJv1
            validator	PPJv1
            user-portrait	PPJv1
            travel-service	PPJv1
            tecno-order	PPJv1
            red-packet	PPJv1
            tag	PPJv1
            sms	PPJv1
            query	PPJv1
            shopping-order	PPJv1
            push	PPJv1
            settlement	PPJv1
            product-service	PPJv1
            send-money	PPJv1
            self-service	PPJv1
            postpay-virtual-bank	PPJv1
            postpay-order	PPJv1
            risk-control	PPJv1
            postpay-open-api	PPJv1
            refund	PPJv1
            postpay-channel	PPJv1
            posp-companion	PPJv1
            pay-route	PPJv1
            partner-points	PPJv1
            order-support	PPJv1
            open-api	PPJv1
            op-m-front	PPJv1
            okcard-risk-control	PPJv1
            offline-merchant	PPJv1
            notify	PPJv1
            message	PPJv1
            merchant-portal	PPJv1
            merchant	PPJv1
            mail	PPJv1
            m-front	PPJv1
            m-aa	PPJv1
            location-service	PPJv1
            loan	PPJv1
            device	PPJv1
            data-center-c	PPJv1
            commodity-center	PPJv1
            channel-center	PPJv1
            bill	PPJv1
            basic-data	PPJv1
            assignee	PPJv1
            analysis	PPJv1
            airtime	PPJv1
            account-service	PPJv1
            account-management	PPJv1
            platform-merchant	PPJv1
            posp	PPJv1
            cash-agent	PPJv1
            partner-front	PPJv1
            account	PPJv1
            trade	PPJv1
            merchant-trigger-center	PPJv2
            merchant-distribute-center	PPJv2
            finance-stock-product	PPJv2
            call-service	PPJv2
            quota-center	PPJv2
            wallet-micro-route	PPJv2
            settlement-data-service	PPJv2
            unionbank-mng-product	PPJv2
            wallet-base-service	PPJv2
            scene-share-product	PPJv2
            easeid-bff-mng	PPJv2
            finance-flexibff-product	PPJv2
            flexi-savings-product	PPJv2
            finance-switch-manager	PPJv2
            trade-channel-service	PPJv2
            tagv2-event-service	PPJv2
            easeid-kyc-service	PPJv2
            boss-mng	PPJv2
            scene-order-history-service	PPJv2
            cs-bff-product	PPJv2
            finance-invest-mng	PPJv2
            chat-service	PPJv2
            rcp-policy-service	PPJv2
            tagv2-service	PPJv2
            aml-product	PPJv2
            exchange-currency-center	PPJv2
            rcp-content-service	PPJv2
            scene-business-product	PPJv2
            finance-insure-product	PPJv2
            credit-rule-service	PPJvSpringboot2
            credit-rule-engine	PPJv2
            fund-account	PPJv2
            scene-base-service	PPJv2
            finance-fund-product	PPJv2
            knowledgebase-mng	PPJv2
            scene-cashin-product	PPJv2
            work-order-service	PPJv2
            open-pf-service	PPJv2
            file-master	PPJv2
            ussd-product	PPJv2
            mgw-core-office	PPJvSpringboot2
            counter-workitem-mng	PPJv2
            finance-bff-product	PPJv2
            user-center	PPJv2
            charge-service	PPJv2
            social-product	PPJv2
            innovate-bff-product	PPJv2
            scene-mng	PPJv2
            clear-settlement-service	PPJv2
            utc-service	PPJv2
            payment-core	PPJv2
            tmock	PPJv2
            data-bury-mng	PPJv2
            data-buriedpoint-collect	PPJv2
            tag-history-service	PPJv2
            recon-parse-service	PPJv2
            product-order-service	PPJv2
            product-order-mng	PPJv2
            base-biz-mng	PPJv2
            nebula-product	PPJv2
            dsp-job-center	PPJv2
            member-product	PPJv2
            crm-agg-service	PPJv2
            data-normal-spider	PPJv2
            validator-service	PPJv2
            oneloop-reverse-core	PPJv2
            business-bff-product	PPJv2
            merchant-core	PPJv2
            dispute-bff-mng	PPJv2
            compliance-bff-mng	PPJv2
            trade-task-service	PPJv2
            online-agent-product	PPJv2
            crm-customer-mng	PPJv2
            trade-mng	PPJv2
            metersphere	PPJv2
            tax-service	PPJv2
            financial-center	PPJv2
            crm-customer-service	PPJv2
            marketing-service	PPJv2
            open-gw-core	PPJv2
            live-product	PPJv2
            data-portrait-center	PPJv2
            act-check-center	PPJv2
            market-game2-product	PPJv2
            scene-offline-product	PPJv2
            oneloop-gw-service	PPJv2
            fund-guard-mng	PPJv2
            wms-center	PPJv2
            transfer-product	PPJv2
            bill-bff-product	PPJv2
            data-message	PPJv2
            data-meta	PPJv2
            data-alert	PPJv2
            finance-qrc-product	PPJv2
            member-center	PPJv2
            scene-activity-product	PPJv2
            recon-core	PPJv2
            data-monitor	PPJv2
            operation-center	PPJv2
            acm-center	PPJv2
            data-etl	PPJv2
            qa-basic-service	PPJv2
            data-carrier-c	PPJv2
            mgw-core	PPJvSpringboot2
            data-carrier-b	PPJv2
            cashier-center	PPJv2
            finance-saving-product	PPJv2
            business-front	PPJv2
            data-collect	PPJv2
            data-view	PPJv2
            merchant-market	PPJv2
            merchant-aftersale	PPJv2
            trade-center	PPJv2
            shop-center	PPJv2
            product-center	PPJv2
            merchant-crm	PPJv2
            data-center	PPJv2
            risk-center	PPJv2
            permission-center	PPJv2
            payment-center	PPJv2
            settlement-center	PPJv2
            merchant-business	PPJv2
            merchant-workflow	PPJv2
            merchant-message	PPJv2
            merchant-rss	PPJv2
            dingtalk-center	PPJv2
            trade-report-mng	PPJv2
            """;

    /**
     * 清除所有标签
     */
    @Test
    void test1() {
        for (Application application : applicationService.selectAll()) {
            BusinessTag uniqueKey = BusinessTag.builder()
                    .businessType(BusinessTypeEnum.APPLICATION.name())
                    .businessId(application.getId())
                    .tagId(TAG_FRAMEWORK_ID)
                    .build();
            BusinessTag businessTag = businessTagService.getByUniqueKey(uniqueKey);
            if (businessTag != null) {
                businessTagService.deleteById(businessTag.getId());
            }
        }
    }

    @Test
    void test2() {
        Iterable<String> list = Splitter.on("\n")
                .split(appFrameworks);
        list.forEach(x -> {
            System.out.println("正在解析: " + x);
            Iterable<String> v2 = Splitter.on("\t")
                    .split(x);
            List<String> items = StreamSupport.stream(v2.spliterator(), false)
                    .toList();
            Application application = applicationService.getByName(items.getFirst());
            if (application == null) {
                System.out.println("应用不存在: " + items.getFirst());
            } else {
                BusinessTag businessTag = BusinessTag.builder()
                        .businessType(BusinessTypeEnum.APPLICATION.name())
                        .businessId(application.getId())
                        .tagId(TAG_FRAMEWORK_ID)
                        .tagValue(items.getLast())
                        .build();
                try {
                    businessTagService.add(businessTag);
                } catch (Exception ignored) {
                }
            }
        });
    }

    @Test
    void test3() {
        Map<String, String> map = Maps.newHashMap();
        List<ApplicationResourceBaseline> baselines = baselineService.selectAll();
        baselines.forEach(baseline -> {
            if (!map.containsKey(baseline.getApplicationName())) {
                map.put(baseline.getApplicationName(), null);
            }
            if (baseline.getNamespace()
                    .equals("prod") && baseline.getName()
                    .endsWith("-canary")) {
                map.put(baseline.getApplicationName(), baseline.getName());
            }
        });

        map.forEach((key, value) -> {
            System.out.println("app: " + key + ", canary: " + value);
        });

    }

}
