package com.baiyi.cratos.my;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsInstanceProviderException;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.facade.BusinessTagFacade;
import com.baiyi.cratos.facade.application.ApplicationFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.HTTPGetAction;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 15:43
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
public class ApplicationTest extends BaseUnit {

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationResourceFacade applicationResourceFacade;

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private EdsAssetService edsAssetService;

    @Resource
    private EdsInstanceProviderHolderBuilder holderBuilder;

    @Resource
    private BusinessTagFacade businessTagFacade;

    private static final String RESOURCE_TPL = "resource: instanceName={}, name={}, displayName={}, businessType={}";

    @Test
    void test() {
        List<Application> apps = applicationService.selectAll();
        Map<Integer, EdsInstanceProviderHolder<?, Deployment>> holders = Maps.newHashMap();
        for (Application app : apps) {
            // applicationName instanceName resource_type namespace
            if (app.getName()
                    .endsWith("-h5")) {
                continue;
            }
            List<ApplicationResource> resources = applicationResourceService.queryByParam(app.getName(), "ACK-CHANNEL-PROD",
                    "KUBERNETES_DEPLOYMENT", "prod");
            if (!resources.isEmpty()) {
                ApplicationResource resource = resources.getFirst();
                EdsAsset edsAsset = edsAssetService.getById(resource.getBusinessId());
                if (edsAsset == null) {
                    System.out.println("asset is null, id=" + resources.getFirst()
                            .getId() + StringFormatter.arrayFormat(RESOURCE_TPL, resource.getInstanceName(),
                            resource.getName(), resource.getDisplayName(), resource.getBusinessType()));
                    continue;
                }
                try {
                    EdsInstanceProviderHolder<?, Deployment> holder = getHolder(edsAsset.getInstanceId(), holders);
                    Deployment deployment = holder.getProvider()
                            .assetLoadAs(edsAsset.getOriginalModel());
                    Optional<Container> containerOptional = KubeUtil.findAppContainerOf(deployment);
                    if (containerOptional.isPresent()) {
                        String path = containerOptional.map(Container::getLivenessProbe)
                                .map(Probe::getHttpGet)
                                .map(HTTPGetAction::getPath)
                                .orElse(null);
                        System.out.println(app.getName() + " LivenessProbe HttpGet Path: " + path);
                        BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                                // Framework
                                .tagId(27)
                                .businessType(BusinessTypeEnum.APPLICATION.name())
                                .businessId(app.getId())
                                .build();
                        if ("/health".equals(path)) {
                            // Framework: PPJv1
                            saveBusinessTag.setTagValue("PPJv1");
                            businessTagFacade.saveBusinessTag(saveBusinessTag);
                        }
                        if ("/actuator/health/liveness".equals(path)) {
                            // Framework: PPJv2
                            saveBusinessTag.setTagValue("PPJv2");
                            businessTagFacade.saveBusinessTag(saveBusinessTag);
                        }
                    }
                } catch (EdsInstanceProviderException e) {
                    // 类型不符
                }

            } else {
                System.out.println(app.getName() + ": no resources.");
            }
        }
    }

    private EdsInstanceProviderHolder<?, Deployment> getHolder(int instanceId,
                                                               Map<Integer, EdsInstanceProviderHolder<?, Deployment>> holders) {
        if (holders.containsKey(instanceId)) {
            return holders.get(instanceId);
        }
        EdsInstanceProviderHolder<?, Deployment> holder = (EdsInstanceProviderHolder<?, Deployment>) holderBuilder.newHolder(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        holders.put(instanceId, holder);
        return holder;
    }

}