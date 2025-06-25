package com.baiyi.cratos.my;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsInstanceProviderException;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.facade.ApplicationFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.HTTPGetAction;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_REPLICAS;

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
    @Autowired
    private EdsAssetIndexService edsAssetIndexService;

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
            List<ApplicationResource> resources = applicationResourceService.queryByParam(app.getName(),
                    "ACK-CHANNEL-PROD", "KUBERNETES_DEPLOYMENT", "prod");
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
                    Optional<Container> containerOptional = KubeUtils.findAppContainerOf(deployment);
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
                            continue;
                        }
                        if ("/actuator/health/liveness".equals(path)) {
                            // Framework: PPJv2
                            saveBusinessTag.setTagValue("PPJv2");
                            businessTagFacade.saveBusinessTag(saveBusinessTag);
                            continue;
                        }
                        // non-standard
                        saveBusinessTag.setTagValue("PPJvNon-standard");
                        businessTagFacade.saveBusinessTag(saveBusinessTag);
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

    public final static String[] APP_TABLE_FIELD_NAME = {"Application Name", "Deployment Name", "Replicas"};

    @Test
    void test1() {
        List<Application> apps = applicationService.selectAll();
        Map<Integer, EdsInstanceProviderHolder<?, Deployment>> holders = Maps.newHashMap();

        PrettyTable appTable = PrettyTable.fieldNames(APP_TABLE_FIELD_NAME);
        int total = 0;
        for (Application app : apps) {
            // applicationName instanceName resource_type namespace
            if (app.getName()
                    .endsWith("-h5")) {
                continue;
            }

            SimpleBusiness hasBusiness = SimpleBusiness.builder()
                    .businessType(BusinessTypeEnum.APPLICATION.name())
                    .businessId(app.getId())
                    .build();

            BusinessTag businessTag = businessTagFacade.getBusinessTag(hasBusiness, SysTagKeys.LEVEL.getKey());


            if (Objects.nonNull(businessTag) && "A1".equals(businessTag.getTagValue())) {
                // A1应用
                System.out.println(app.getName());

                List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(app.getName(),
                        EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), "prod");

                if (!CollectionUtils.isEmpty(resources)) {
                    for (ApplicationResource resource : resources) {
                        EdsAsset edsAsset = edsAssetService.getById(resource.getBusinessId());
                        if(edsAsset.getInstanceId() != 101) {
                            continue;
                        }

                        EdsAssetIndex replicasIndex = edsAssetIndexService.getByAssetIdAndName(resource.getBusinessId(),
                                KUBERNETES_REPLICAS);
                        if (Objects.nonNull(replicasIndex)) {
                            total = total + Integer.parseInt(replicasIndex.getValue());
                            appTable.addRow(app.getName(), resource.getName(), replicasIndex.getValue());
                            System.out.println(resource.getName() + " replicas=" + replicasIndex.getValue());
                        }
                    }
                }
            }
        }

        System.out.println("replicas total: " + total);
        System.out.println(appTable.toString());

    }

}
