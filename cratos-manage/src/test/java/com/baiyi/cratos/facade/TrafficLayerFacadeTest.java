package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.facade.proxy.TrafficLayerProxy;
import com.baiyi.cratos.service.TrafficLayerDomainService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

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
            """;

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
            String business = fields.length > 1 ? fields[1].trim() : "--";
            String owner = fields.length > 2 ? fields[2].trim() : "--";
            String tl = fields.length > 3 ? fields[3].trim() : "--";
            TrafficLayerDomain uk = TrafficLayerDomain.builder()
                    .domain(domain)
                    .build();
            TrafficLayerDomain trafficLayerDomain = trafficLayerDomainService.getByUniqueKey(uk);
            if (trafficLayerDomain == null) {
                System.out.printf("域名不存在: %-50s | 业务: %-20s | 负责人: %-10s | TL: %-10s%n", domain, business,
                        owner, tl);
            }
            String comment = StringUtils.hasText(
                    trafficLayerDomain.getComment()) ? trafficLayerDomain.getComment() + " | " : "";
            comment += StringFormatter.arrayFormat("Business: {} | Owner: {} | TL: {}",business,owner,tl);
            trafficLayerDomain.setComment(comment);
            trafficLayerDomainService.updateByPrimaryKey(trafficLayerDomain);
            // 录入
        }
    }

}
