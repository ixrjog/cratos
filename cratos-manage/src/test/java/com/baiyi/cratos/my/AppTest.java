package com.baiyi.cratos.my;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.BusinessTagService;
import com.google.api.client.util.Lists;
import com.google.common.base.Splitter;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/25 17:03
 * &#064;Version 1.0
 */
public class AppTest extends BaseUnit {

    private static final String text = """
            scene-data-product	钱包
            scene-business-product	钱包
            scene-base-service	钱包
            social-product	钱包
            innovate-bff-product	钱包
            tag-history-service	钱包
            tag-a2-query-service	钱包
            tag-calculate-core	钱包
            online-agent-product	钱包
            live-product	钱包
            scene-offline-product	钱包
            oneloop-gw-service	钱包
            transfer-product	钱包
            bill-bff-product	钱包
            offline-pay-product	钱包
            betting-product	钱包
            c-bff-product	钱包
            finance-qrc-product	钱包
            travel-service	钱包
            tag	钱包
            push	钱包
            order-support	钱包
            message	钱包
            mail	钱包
            location-service	钱包
            device	钱包
            airtime	钱包
            send-money	钱包
            tagv2-service	钱包
            credit-scene-service	信贷
            credit-product-center	信贷
            credit-rule-service	信贷
            oneloop-reverse-core	信贷
            ng-postpay-channel	信贷
            flexi-mng	信贷
            flexi-bff-product	信贷
            flexi-service	信贷
            postpay-schedule-task	信贷
            postloan-risk-control	信贷
            postpay-marketing	信贷
            postpay-bigdata	信贷
            postpay-order	信贷
            postpay-open-api	信贷
            postpay-channel	信贷
            okcard-risk-control	信贷
            loan	信贷
            postpay-virtual-bank	信贷
            business-bff-product	商户
            merchant-core	商户
            oneloop-product	商户
            data-etl	商户
            nile-m-bff	商户
            merchant-market	商户
            partner-points	商户
            op-m-front	商户
            offline-merchant	商户
            merchant-portal	商户
            merchant	商户
            platform-merchant	商户
            posp	商户
            cash-agent	商户
            partner-front	商户
            trade-center	商户
            shop-center	商户
            payment-center	商户
            settlement-center	商户
            merchant-business	商户
            merchant-message	商户
            merchant-trigger-center	商户
            merchant-distribute-center	商户
            finance-network-front	金融网络
            finance-switch-order	金融网络
            finance-switch-distribution	金融网络
            ng-gtb-channel	金融网络
            ng-etranzact-channel	金融网络
            ng-kuda-channel	金融网络
            ng-access-channel	金融网络
            ng-channel	金融网络
            channel-center	金融网络
            ng-pos-polaris-channel	金融网络(pos)
            finance-switch-adapter	金融网络(pos)
            ng-pos-access-channel	金融网络(pos)
            paynet-ng-iso-channel	金融网络(pos)
            ng-pos-upsl-channel	金融网络(pos)
            finance-switch-order	金融网络(pos)
            finance-switch-distribution	金融网络(pos)
            ng-issuer-isw-channel	金融网络(pos)
            flexi-savings-product	理财域
            finance-bff-product	理财域
            finance-insure-product	理财域
            finance-fund-product	理财域
            finance-invest-mng	理财域
            user-center	理财域
            finance-account-core	理财域
            finance-saving-product	理财域
            query	理财域
            m-front	理财域
            m-aa	理财域
            permission-center	理财域
            dingtalk-center	理财域
            finance-flexibff-product	理财域
            cs-bff-product	营运管理
            chat-service	营运管理
            clear-settlement-service	营运管理
            crm-customer-service	营运管理
            recon-core	营运管理
            acm-center	营运管理
            settlement	营运管理
            account-service	营运管理
            account	营运管理
            exchange-currency-center	营运管理
            open-pf-service	营运管理
            work-order-service	交易支付
            charge-service	交易支付
            utc-service	交易支付
            payment-core	交易支付
            dispute-bff-mng	交易支付
            tax-service	交易支付
            cashier-center	交易支付
            product-service	交易支付
            refund	交易支付
            bill	交易支付
            trade	交易支付
            merchant-workflow	交易支付
            rcp-content-service	营销
            member-product	营销
            compliance-bff-mng	营销
            marketing-service	营销
            market-game2-product	营销
            member-center	营销
            scene-activity-product	营销
            marketing-core	营销
            loyalty	营销
            coupon	营销
            validator	营销
            red-packet	营销
            shopping-order	营销
            risk-control	营销
            commodity-center	营销
            mgw-core	营销
            aml-product	营销
            base-biz-mng	营销
            ng-palmpayinnertransfer-channel	渠道
            ng-pos-zone-channel	渠道
            ng-palmpay-blooms-channel	渠道
            ng-blooms-server-channel	渠道
            ng-nibss-blooms-channel	渠道
            ng-opay-channel	渠道
            ng-paripesa-channel	渠道
            ng-pos-kimono-channel	渠道
            ng-pos-nibss-channel	渠道
            ng-mobifin-channel	渠道
            ng-moniepoint-channel	渠道
            ng-easywin-channel	渠道
            ng-betbuzz-channel	渠道
            ng-demmyglobal-channel	渠道
            ng-betway-channel	渠道
            ng-card-paystack-channel	渠道
            ng-card-uba-channel	渠道
            ng-bangbet-channel	渠道
            ng-ilot-channel	渠道
            ng-palmpay-server-channel	渠道
            ng-wgb-direct-channel	渠道
            ng-firs-channel	渠道
            ng-ikedc-channel	渠道
            ng-easypay-channel	渠道
            ng-new-ekedc-channel	渠道
            ng-glo-channel	渠道
            ng-new-qrios-channel	渠道
            ng-nibss-channel	渠道
            ng-interswitch-channel	渠道
            ng-sterling-channel	渠道
            ng-betking-channel	渠道
            ng-uba-new-channel	渠道
            ng-habaripay-channel	渠道
            ng-new-fidelity-channel	渠道
            ng-zenith-channel	渠道
            ng-up-channel	渠道
            ng-parimatch-channel	渠道
            ng-betcorrect-channel	渠道
            ng-new-mtn-channel	渠道
            ng-uba-channel	渠道
            merchant-rss	架构
            rcp-urule-server	架构
            basic-data	钱包
            """;

    @Resource
    private ApplicationService applicationService;
    @Resource
    private BusinessTagFacade businessTagFacade;
    @Resource
    private BusinessTagService businessTagService;

    @Test
    void test2() {
        Iterable<String> list = Splitter.on("\n")
                .split(text);
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
                BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                        .businessType(BusinessTypeEnum.APPLICATION.name())
                        .businessId(application.getId())
                        .tagId(39)
                        .tagValue(items.getLast())
                        .build();
                businessTagFacade.saveBusinessTag(saveBusinessTag);
            }
        });
    }

    /**
     * public BusinessTag getByUniqueKey(@NonNull BusinessTag record) {
     * Example example = new Example(BusinessTag.class);
     * Example.Criteria criteria = example.createCriteria();
     * criteria.andEqualTo("businessType", record.getBusinessType())
     * .andEqualTo("businessId", record.getBusinessId())
     * .andEqualTo("tagId", record.getTagId());
     * return businessTagMapper.selectOneByExample(example);
     * }
     */

    @Test
    void test3() {
        List<BusinessTag> businessTags = businessTagService.queryByBusinessTypeAndTagId(
                BusinessTypeEnum.APPLICATION.name(), 39);
        // Convert to map with tagValue as key and List<BusinessTag> as value
        Map<String, List<BusinessTag>> tagValueMap = businessTags.stream()
                .collect(Collectors.groupingBy(BusinessTag::getTagValue));
        tagValueMap.forEach((businessName, tags) -> {
            List<BusinessTag> levelTags = Lists.newArrayList();
            tags.forEach(tag -> {
                BusinessTag uniqueKey = BusinessTag.builder()
                        .businessType(BusinessTypeEnum.APPLICATION.name())
                        .businessId(tag.getBusinessId())
                        .tagId(37)
                        .build();
                BusinessTag levelTag = businessTagService.getByUniqueKey(uniqueKey);
                if (Objects.nonNull(levelTag)) {
                    levelTags.add(levelTag);
                }
            });
            Map<String, List<BusinessTag>> levelTagMap = levelTags.stream()
                    .collect(Collectors.groupingBy(BusinessTag::getTagValue));
            System.out.println("businessName: " + businessName);
            levelTagMap.forEach((k, v) -> System.out.println("level: " + k + ", count: " + v.size()));
            System.out.print("\n");
        });
    }

}
