package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.NetworkUtils;
import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.service.GlobalNetworkPlanningService;
import com.baiyi.cratos.service.GlobalNetworkSubnetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/6 14:34
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalNetworkDetailsWrapper {

    private final GlobalNetworkWrapper globalNetworkWrapper;

    private final GlobalNetworkPlanningService globalNetworkPlanningService;

    private final GlobalNetworkSubnetService globalNetworkSubnetService;

    public final static String[] SUBNET_TABLE_FIELD_NAME = {"Main Name", "Main Type", "Name", "Subnet KmsKey", "Region", "Zone", "CIDR Block", "Resource", "Comment"};

    public void wrap(GlobalNetworkVO.NetworkDetails networkDetails) {
        globalNetworkWrapper.businessWrap(networkDetails);
        List<GlobalNetworkPlanning> plannings = globalNetworkPlanningService.queryByNetworkId(
                networkDetails.getNetworkId());
        if (CollectionUtils.isEmpty(plannings)) {
            return;
        }
        List<GlobalNetworkVO.Subnet> subnets = globalNetworkSubnetService.queryByValid()
                .stream()
                .map(e -> BeanCopierUtils.copyProperties(e, GlobalNetworkVO.Subnet.class))
                .sorted()
                .collect(Collectors.toList());
        networkDetails.setPlanningDetails(plannings.stream()
                .map(e -> toPlanningDetails(e, subnets))
                .sorted()
                .collect(Collectors.toList()));
    }

    private GlobalNetworkVO.PlanningDetails toPlanningDetails(GlobalNetworkPlanning planning,
                                                              List<GlobalNetworkVO.Subnet> allSubnets) {
        GlobalNetworkVO.PlanningDetails planningDetails = BeanCopierUtils.copyProperties(planning,
                GlobalNetworkVO.PlanningDetails.class);
        PrettyTable subnetTable = PrettyTable.fieldNames(SUBNET_TABLE_FIELD_NAME);
        allSubnets.stream()
                .filter(e -> NetworkUtils.inNetwork(planning.getCidrBlock(), e.getCidrBlock()))
                .forEach(e -> subnetTable.addRow(e.getMainName(), e.getMainType(), e.getName(), e.getSubnetKey(),
                        e.getRegion(), e.getZone(), e.getCidrBlock(), e.getResourceTotal(), e.getComment()));
        planningDetails.setSubnetTable(subnetTable.toString());
        return planningDetails;
    }

}
