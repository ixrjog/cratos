package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.exception.GlobalNetworkException;
import com.baiyi.cratos.common.util.IpUtil;
import com.baiyi.cratos.common.util.NetworkUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.facade.GlobalNetworkFacade;
import com.baiyi.cratos.facade.GlobalNetworkPlanningFacade;
import com.baiyi.cratos.service.GlobalNetworkService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.GlobalNetworkDetailsWrapper;
import com.baiyi.cratos.wrapper.GlobalNetworkWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/3 11:32
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalNetworkFacadeImpl implements GlobalNetworkFacade {

    private final GlobalNetworkService globalNetworkService;
    private final GlobalNetworkWrapper globalNetworkWrapper;
    private final GlobalNetworkPlanningFacade globalNetworkPlanningFacade;
    private final GlobalNetworkDetailsWrapper globalNetworkDetailsWrapper;

    @Override
    @PageQueryByTag(ofType = BusinessTypeEnum.GLOBAL_NETWORK)
    public DataTable<GlobalNetworkVO.Network> queryGlobalNetworkPage(
            GlobalNetworkParam.GlobalNetworkPageQuery pageQuery) {
        DataTable<GlobalNetwork> table = globalNetworkService.queryGlobalNetworkPage(pageQuery.toParam());
        return globalNetworkWrapper.wrapToTarget(table);
    }

    @Override
    public void addGlobalNetwork(GlobalNetworkParam.AddGlobalNetwork addGlobalNetwork) {
        GlobalNetwork globalNetwork = addGlobalNetwork.toTarget();
        final String cidrBlock = globalNetwork.getCidrBlock()
                .trim();
        if (!NetworkUtil.isValidIpRange(cidrBlock)) {
            throw new GlobalNetworkException("cidrBlock is invalid.");
        }
        try {
            int resourceTotal = IpUtil.getIpCount(StringUtils.substringAfter(cidrBlock, "/"));
            globalNetwork.setResourceTotal(resourceTotal);
            globalNetworkService.add(globalNetwork);
        } catch (Exception e) {
            throw new GlobalNetworkException("The format of cidrBlock={} is incorrect.", cidrBlock);
        }
    }

    @Override
    public void updateGlobalNetwork(GlobalNetworkParam.UpdateGlobalNetwork updateGlobalNetwork) {
        GlobalNetwork globalNetwork = globalNetworkService.getById(updateGlobalNetwork.getId());
        globalNetwork.setName(updateGlobalNetwork.getName());
        globalNetwork.setMainName(updateGlobalNetwork.getMainName());
        globalNetwork.setComment(updateGlobalNetwork.getComment());
        globalNetworkService.updateByPrimaryKey(globalNetwork);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        globalNetworkPlanningFacade.removeNetwork(id);
        globalNetworkService.deleteById(id);
    }

    @Override
    public GlobalNetworkVO.NetworkDetails queryGlobalNetworkDetails(
            GlobalNetworkParam.QueryGlobalNetworkDetails queryGlobalNetworkDetails) {
        GlobalNetworkVO.NetworkDetails networkDetails = GlobalNetworkVO.NetworkDetails.builder()
                .networkId(queryGlobalNetworkDetails.getId())
                .build();
        globalNetworkDetailsWrapper.wrap(networkDetails);
        return networkDetails;
    }

    @Override
    public List<GlobalNetworkVO.NetworkDetails> getGlobalNetworkAllDetails() {
        List<GlobalNetwork> networks = globalNetworkService.queryByValid();
        return CollectionUtils.isEmpty(networks) ? List.of() : networks.stream()
                .map(this::buildNetworkDetails)
                .toList();
    }

    private GlobalNetworkVO.NetworkDetails buildNetworkDetails(GlobalNetwork globalNetwork) {
        GlobalNetworkVO.NetworkDetails networkDetails = GlobalNetworkVO.NetworkDetails.builder()
                .networkId(globalNetwork.getId())
                .build();
        globalNetworkDetailsWrapper.wrap(networkDetails);
        return networkDetails;
    }

    @Override
    public List<GlobalNetworkVO.Network> checkGlobalNetworkById(int id) {
        GlobalNetwork globalNetwork = globalNetworkService.getById(id);
        if (globalNetwork == null) {
            return List.of();
        }
        return globalNetworkService.queryByValid()
                .stream()
                .filter(e -> id != e.getId() && NetworkUtil.inNetwork(e.getCidrBlock(), globalNetwork.getCidrBlock()))
                .map(globalNetworkWrapper::wrapToTarget)
                .toList();
    }

    @Override
    public List<GlobalNetworkVO.Network> checkGlobalNetworkByCidrBlock(String cidrBlock) {
        final String cidr = cidrBlock.trim();
        if (!NetworkUtil.isValidIpRange(cidr)) {
            throw new GlobalNetworkException("cidrBlock is invalid.");
        }
        return globalNetworkService.queryByValid()
                .stream()
                .filter(e -> NetworkUtil.inNetwork(e.getCidrBlock(), cidr))
                .map(globalNetworkWrapper::wrapToTarget)
                .toList();
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return globalNetworkService;
    }

}