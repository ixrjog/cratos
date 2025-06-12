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

}
