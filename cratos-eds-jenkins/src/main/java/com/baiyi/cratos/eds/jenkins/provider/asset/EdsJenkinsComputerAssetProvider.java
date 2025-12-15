package com.baiyi.cratos.eds.jenkins.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.jenkins.model.JenkinsComputerModel;
import com.baiyi.cratos.eds.jenkins.sdk.model.Computer;
import com.baiyi.cratos.eds.jenkins.sdk.model.ComputerWithDetails;
import com.baiyi.cratos.eds.jenkins.sdk.server.JenkinsServer;
import com.baiyi.cratos.eds.jenkins.sdk.server.JenkinsServerBuilder;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/16 09:56
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.JENKINS, assetTypeOf = EdsAssetTypeEnum.JENKINS_COMPUTER)
public class EdsJenkinsComputerAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Jenkins, JenkinsComputerModel.Computer> {

    public EdsJenkinsComputerAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                           CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                           EdsAssetIndexFacade edsAssetIndexFacade,
                                           UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                           EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<JenkinsComputerModel.Computer> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Jenkins> instance) throws EdsQueryEntitiesException {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(instance.getConfig())) {
            Map<String, Computer> computerMap = jenkinsServer.getComputers();
            if (CollectionUtils.isEmpty(computerMap)) {
                return List.of();
            }
            List<ComputerWithDetails> computerWithDetails = Lists.newArrayListWithCapacity(computerMap.size());
            for (Computer computer : computerMap.values()) {
                ComputerWithDetails details = computer.details();
                if (details != null) {
                    computerWithDetails.add(details);
                }
            }
            return toComputer(computerWithDetails);
        } catch (Exception e) {
            log.warn("Failed to list Jenkins computers: {}", e.getMessage(), e);
            throw new EdsQueryEntitiesException("Failed to list Jenkins computers: {}", e.getMessage());
        }
    }

    private List<JenkinsComputerModel.Computer> toComputer(List<ComputerWithDetails> computerWithDetails) {
        if (CollectionUtils.isEmpty(computerWithDetails)) {
            return List.of();
        }
        return computerWithDetails.stream()
                .map(e -> JenkinsComputerModel.Computer.builder()
                        .displayName(e.getDisplayName())
                        .actions(e.getActions())
                        .executors(e.getExecutors())
                        .assignedLabels(e.getAssignedLabels())
                        .idle(e.getIdle())
                        .jnlp(e.getJnlp())
                        .launchSupported(e.getLaunchSupported())
                        .manualLaunchAllowed(e.getManualLaunchAllowed())
                        .monitorData(e.getMonitorData())
                        .numExecutors(e.getNumExecutors())
                        .offline(e.getOffline())
                        .offlineCause(e.getOfflineCause())
                        .offlineCauseReason(e.getOfflineCauseReason())
                        .oneOffExecutors(e.getOneOffExecutors())
                        .temporarilyOffline(e.getTemporarilyOffline())
                        .build())
                .toList();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Jenkins> instance,
                                  JenkinsComputerModel.Computer entity) throws EdsAssetConversionException {
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getDisplayName())
                .nameOf(entity.getDisplayName())
                .build();
    }

}
