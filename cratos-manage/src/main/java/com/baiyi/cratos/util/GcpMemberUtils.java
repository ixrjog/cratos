package com.baiyi.cratos.util;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.googlecloud.model.GcpMemberModel;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/23 18:22
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class GcpMemberUtils {

    private static final String[] TABLE_FIELD_NAME = {"Project Name", "Project ID", "Member", "PalmPay User"};

    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final EdsAssetService edsAssetService;
    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
    private final BusinessTagFacade businessTagFacade;


    public void printAllGcpMemberTable() {
        List<EdsInstance> instances = edsInstanceQueryHelper.queryInstance(EdsInstanceTypeEnum.GCP);
        instances.forEach(instance -> printGcpMemberTable(instance.getId()));
    }

    public void printGcpMemberTable(int instanceId) {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(instanceId, EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsInstanceProviderHolder<EdsConfigs.Gcp, GcpMemberModel.Member> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, GcpMemberModel.Member>) edsProviderHolderFactory.createHolder(
                instanceId, EdsAssetTypeEnum.GCP_MEMBER.name());
        PrettyTable prettyTable = PrettyTable.fieldNames(TABLE_FIELD_NAME);
        EdsInstanceAssetProvider<EdsConfigs.Gcp, GcpMemberModel.Member> provider = holder.getProvider();
        for (EdsAsset asset : assets) {
            try {
                GcpMemberModel.Member member = provider.loadAsset(asset.getOriginalModel());
                if (member.getType()
                        .equals("user")) {
                    SimpleBusiness simpleBusiness = SimpleBusiness.builder()
                            .businessType(BusinessTypeEnum.EDS_ASSET.name())
                            .businessId(asset.getId())
                            .build();
                    BusinessTag businessTag = businessTagFacade.getBusinessTag(
                            simpleBusiness, SysTagKeys.USERNAME.getKey());
                    String username = businessTag == null ? "--" : businessTag.getTagValue();
                    addTableRow(
                            prettyTable, holder.getInstance()
                                    .getConfig(), member.getName(), username
                    );
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        System.out.println(prettyTable);
    }

    private void addTableRow(PrettyTable table, EdsConfigs.Gcp gcp, String member, String username) {
        table.addRow(
                gcp.getProject()
                        .getName(), gcp.getProject()
                        .getId(), member, username
        );
    }

}
