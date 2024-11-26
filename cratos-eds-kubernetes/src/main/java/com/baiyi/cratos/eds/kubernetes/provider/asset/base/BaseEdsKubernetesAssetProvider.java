package com.baiyi.cratos.eds.kubernetes.provider.asset.base;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseHasNamespaceEdsAssetProvider;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/3/7 15:41
 * @Version 1.0
 */
@Slf4j
public abstract class BaseEdsKubernetesAssetProvider<A extends HasMetadata> extends BaseHasNamespaceEdsAssetProvider<EdsKubernetesConfigModel.Kubernetes, A> {

    private final KubernetesNamespaceRepo kubernetesNamespaceRepo;

    public BaseEdsKubernetesAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                          UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
        this.kubernetesNamespaceRepo = kubernetesNamespaceRepo;
    }

    @Override
    protected Set<String> listNamespace(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        List<Namespace> namespaces = kubernetesNamespaceRepo.list(instance.getEdsConfigModel());
        if (CollectionUtils.isEmpty(namespaces)) {
            return Sets.newHashSet();
        }
        return namespaces.stream()
                .map(e -> e.getMetadata()
                        .getName())
                .collect(Collectors.toSet());
    }

    protected String getAssetId(String namespace, String name) {
        return Joiner.on(":")
                .join(namespace, name);
    }

    protected String getAssetId(HasMetadata hasMetadata) {
        return Joiner.on(":")
                .skipNulls()
                .join(getNamespace(hasMetadata), getName(hasMetadata));
    }

    protected String getName(HasMetadata hasMetadata) {
        return hasMetadata.getMetadata()
                .getName();
    }

    protected String getNamespace(HasMetadata hasMetadata) {
        return hasMetadata.getMetadata()
                .getNamespace();
    }

    protected Date getCreationTime(HasMetadata hasMetadata) {
        return toUTCDate(hasMetadata.getMetadata()
                .getCreationTimestamp());
    }

    protected String getMetadataLabel(HasMetadata hasMetadata, String key) {
        Optional<Map<String, String>> optionalLabels = Optional.of(hasMetadata)
                .map(HasMetadata::getMetadata)
                .map(ObjectMeta::getLabels);
        return optionalLabels.map(stringStringMap -> stringStringMap.getOrDefault(key, null))
                .orElse(null);
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, A entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(getAssetId(entity))
                .nameOf(getName(entity))
                .kindOf(entity.getKind())
                .createdTimeOf(getCreationTime(entity))
                .build();
    }

    private Date toUTCDate(String time) {
        return com.baiyi.cratos.common.util.TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}