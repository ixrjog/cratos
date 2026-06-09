package com.baiyi.cratos.facade.security.impl;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.domain.generator.ApiScanResult;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.security.ApiScanVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesPodRepo;
import com.baiyi.cratos.eds.security.apirisk.test.generic.GenericCallService;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.facade.security.ApiScanFacade;
import com.baiyi.cratos.service.ApiScanResultService;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/5 11:30
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiScanFacadeImpl implements ApiScanFacade {

    private final ApplicationService applicationService;
    private final ApplicationResourceService applicationResourceService;
    private final EdsAssetService edsAssetService;
    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final KubernetesPodRepo kubernetesPodRepo;
    private final GenericCallService genericCallService;
    private final RedisUtil redisUtil;

    private static final String DEFAULT_PORT = "8080";
    private static final String SCAN_CONFIG_KEY = "SECURITY:API_SCAN:CONFIG";

    private final ApiScanResultService apiScanResultService;

    @Override
    public void scan(ApiScanVO.ScanConfig scanConfig) {
        String scanBatch = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        List<Application> applications = applicationService.selectAll();
        for (Application application : applications) {
            scanApplication(scanConfig, application, scanBatch);
        }
        log.info("Scan batch {} completed", scanBatch);
    }

    @Override
    public void scan(ApiScanVO.ScanConfig scanConfig, String applicationName) {
        String scanBatch = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Application application = applicationService.getByName(applicationName);
        scanApplication(scanConfig, application, scanBatch);
        log.info("Scan batch {} completed", scanBatch);
    }

    private void scanApplication(ApiScanVO.ScanConfig scanConfig, Application application, String scanBatch) {
        List<ApplicationResource> applicationResources = applicationResourceService.queryApplicationResource(
                application.getName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), "prod");
        if (CollectionUtils.isEmpty(applicationResources)) {
            return;
        }
        applicationResources.stream()
                .map(resource -> edsAssetService.getById(resource.getBusinessId()))
                .filter(Objects::nonNull)
                .filter(asset -> asset.getInstanceId() == 101 || asset.getInstanceId() == 118)
                .forEach(asset -> {
                    String podIP = getPodIP(asset);
                    if (!StringUtils.hasText(podIP)) {
                        return;
                    }
                    ApplicationConfigModel.Config appConfig = ApplicationConfigModel.loadAs(application.getConfig());
                    String contextPath = Optional.ofNullable(appConfig)
                            .map(ApplicationConfigModel.Config::getApplication)
                            .map(ApplicationConfigModel.App::getContextPath)
                            .orElse(application.getName());
                    String port = Optional.ofNullable(appConfig)
                            .map(ApplicationConfigModel.Config::getApplication)
                            .map(ApplicationConfigModel.App::getPort)
                            .orElse(DEFAULT_PORT);
                    // Scan with root context
                    scanTarget(application.getName(), asset.getName(), podIP, "", port, scanConfig, scanBatch);
                    // Scan with context
                    scanTarget(application.getName(), asset.getName(), podIP, contextPath, port, scanConfig, scanBatch);
                });
    }

    public void scanTarget(String appName, String deploymentName, String serviceIP, String contextPath, String port,
                           ApiScanVO.ScanConfig scanConfig, String scanBatch) {
        if (scanConfig.getScanConfig() == null) {
            return;
        }
        scanConfig.getScanConfig()
                .stream()
                .filter(group -> group.getRules() != null)
                .forEach(group -> group.getRules()
                        .forEach(rule -> {
                            List<String> methods = rule.getMethods() != null ? rule.getMethods() : List.of("GET");
                            methods.forEach(method -> {
                                String rulePath = rule.getPath()
                                        .startsWith("/") ? rule.getPath() : "/" + rule.getPath();
                                String path = StringUtils.hasText(
                                        contextPath) ? "/" + contextPath + rulePath : rulePath;
                                String url = StringFormatter.arrayFormat("http://{}:{}/{}", serviceIP, port, path);
                                try {
                                    GenericCall.Response response = genericCallService.callDynamicApiWithResponse(
                                                    url, method, Map.of(), null, null)
                                            .block(Duration.ofSeconds(10));
                                    if (response != null && isVulnerable(response.getStatusCode(), group.getExpect())) {
                                        ApiScanResult result = apiScanResultService.getByUniqueKey(
                                                ApiScanResult.builder()
                                                        .appName(appName)
                                                        .path(path)
                                                        .build());
                                        if (result != null) {
                                            result.setDeploymentName(deploymentName);
                                            result.setPodIP(serviceIP);
                                            result.setStatusCode(response.getStatusCode());
                                            result.setResp(response.getBody());
                                            result.setRespSize(response.getBody() != null ? response.getBody()
                                                    .length() : 0);
                                            result.setGroupName(group.getName());
                                            result.setSeverity(group.getSeverity());
                                            result.setCategory(group.getCategory());
                                            result.setScanBatch(scanBatch);
                                            result.setValid(true);
                                            apiScanResultService.updateByPrimaryKey(result);
                                        } else {
                                            result = ApiScanResult.builder()
                                                    .appName(appName)
                                                    .deploymentName(deploymentName)
                                                    .podIP(serviceIP)
                                                    .path(path)
                                                    .method(method)
                                                    .statusCode(response.getStatusCode())
                                                    .resp(response.getBody())
                                                    .respSize(response.getBody() != null ? response.getBody()
                                                            .length() : 0)
                                                    .groupName(group.getName())
                                                    .severity(group.getSeverity())
                                                    .category(group.getCategory())
                                                    .scanBatch(scanBatch)
                                                    .valid(true)
                                                    .build();
                                            apiScanResultService.add(result);
                                        }
                                        log.warn(
                                                "SCAN HIT: app={}, url={}, status={}, group={}", appName, url,
                                                response.getStatusCode(), group.getName()
                                        );
                                    }
                                } catch (Exception e) {
                                    // Timeout or connection refused - not vulnerable
                                    log.error(e.getMessage(), e);
                                }
                            });
                        }));
    }

    private boolean isVulnerable(int statusCode, ApiScanVO.ScanExpect expect) {
        if (expect == null || expect.getStatusNot() == null) {
            return statusCode == 200;
        }
        return expect.getStatusNot()
                .contains(statusCode);
    }

    @SuppressWarnings("unchecked")
    private String getPodIP(EdsAsset asset) {
        try {
            EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> providerHolder = (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment>) edsProviderHolderFactory.createHolder(
                    asset.getInstanceId(), asset.getAssetType());
            Deployment deployment = providerHolder.getProvider()
                    .loadAsset(asset.getOriginalModel());
            int replicas = Optional.ofNullable(deployment)
                    .map(Deployment::getSpec)
                    .map(DeploymentSpec::getReplicas)
                    .orElse(0);
            if (replicas == 0) {
                return null;
            }
            List<Pod> pods = kubernetesPodRepo.list(
                    providerHolder.getInstance()
                            .getConfig(), deployment.getMetadata()
                            .getNamespace(), deployment.getMetadata()
                            .getName()
            );
            if (CollectionUtils.isEmpty(pods)) {
                return null;
            }
            return pods.getFirst()
                    .getStatus()
                    .getPodIP();
        } catch (Exception e) {
            log.debug("Failed to get pod IP for asset {}: {}", asset.getId(), e.getMessage());
            return null;
        }
    }

    @Override
    public ApiScanVO.ScanConfig loadScanConfig() {
        Object obj = redisUtil.get(SCAN_CONFIG_KEY);
        if (obj instanceof String yaml && org.springframework.util.StringUtils.hasText(yaml)) {
            return ApiScanVO.ScanConfig.loadAs(yaml);
        }
        return null;
    }

    @Override
    public String getScanConfigYaml() {
        Object obj = redisUtil.get(SCAN_CONFIG_KEY);
        return obj instanceof String yaml ? yaml : "";
    }

    @Override
    public void saveScanConfig(String configYaml) {
        redisUtil.set(SCAN_CONFIG_KEY, configYaml);
    }

    @Override
    public com.baiyi.cratos.domain.DataTable<ApiScanResult> queryScanResults(
            com.baiyi.cratos.domain.param.http.security.ApiScanParam.ScanResultPageQuery pageQuery) {
        com.github.pagehelper.Page<ApiScanResult> pageResult = com.github.pagehelper.PageHelper.startPage(
                pageQuery.getPage(), pageQuery.getLength());
        tk.mybatis.mapper.entity.Example example = new tk.mybatis.mapper.entity.Example(ApiScanResult.class);
        tk.mybatis.mapper.entity.Example.Criteria criteria = example.createCriteria();
        if (org.springframework.util.StringUtils.hasText(pageQuery.getQueryName())) {
            criteria.andLike("appName", "%" + pageQuery.getQueryName() + "%");
        }
        if (org.springframework.util.StringUtils.hasText(pageQuery.getQueryPath())) {
            criteria.andLike("path", "%" + pageQuery.getQueryPath() + "%");
        }
        example.setOrderByClause("create_time desc");
        java.util.List<ApiScanResult> data = apiScanResultService.getMapper()
                .selectByExample(example);
        return new com.baiyi.cratos.domain.DataTable<>(data, pageResult.getTotal());
    }


}
