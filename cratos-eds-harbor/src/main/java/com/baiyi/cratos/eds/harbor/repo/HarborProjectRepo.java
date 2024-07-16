package com.baiyi.cratos.eds.harbor.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.core.config.EdsHarborConfigModel;
import com.baiyi.cratos.eds.harbor.model.HarborProject;
import com.baiyi.cratos.eds.harbor.service.HarborProjectService;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/16 上午11:07
 * &#064;Version 1.0
 */
public class HarborProjectRepo {

    private static HarborProjectService createHarborProjectService(EdsHarborConfigModel.Harbor harbor) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(harbor.acqUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(HarborProjectService.class);
    }

    public static List<HarborProject.Project> listProjects(EdsHarborConfigModel.Harbor harbor) {
        HarborProjectService harborProjectService = createHarborProjectService(harbor);
        int page = 1;
        int size = 10;
        Map<String, String> param = DictBuilder.newBuilder()
                .put("page", page)
                .put("size", size)
                .build();
        List<HarborProject.Project> result = Lists.newArrayList();
        while (true) {
            List<HarborProject.Project> projects = harborProjectService.listProjects(harbor.getCred()
                    .toBasic(), param);
            if (CollectionUtils.isEmpty(projects)) {
                return result;
            }
            if (projects.size() < size) {
                result.addAll(projects);
                return result;
            }
            page++;
            param.put("page", String.valueOf(page));
        }
    }

}
