package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.server.ServerAccountParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.crystal.CrystalServerVO;
import com.baiyi.cratos.domain.view.server.ServerAccountVO;
import com.baiyi.cratos.enums.RemoteManagementProtocolEnum;
import com.baiyi.cratos.facade.CrystalFacade;
import com.baiyi.cratos.facade.crystal.CrystalAssetTypeOptionsFactory;
import com.baiyi.cratos.facade.server.ServerAccountFacade;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.wrapper.CrystalWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/9 15:08
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CrystalFacadeImpl implements CrystalFacade {

    private final EdsAssetService edsAssetService;
    private final CrystalWrapper crystalWrapper;
    private final ServerAccountFacade serverAccountFacade;
    private static final int SIZE = 20;

    @Override
    public OptionsVO.Options getInstanceAssetTypeOptions(String instanceType) {
        return CrystalAssetTypeOptionsFactory.getOptions(instanceType);
    }

    @Override
    public List<ServerAccountVO.ServerAccount> getServerAccountOptions(int size) {
        ServerAccountParam.ServerAccountPageQuery pageQuery = ServerAccountParam.ServerAccountPageQuery.builder()
                .protocol(RemoteManagementProtocolEnum.SSH.name())
                .valid(true)
                .page(1)
                .length(size == 0 ? SIZE : size)
                .build();
        DataTable<ServerAccountVO.ServerAccount> table = serverAccountFacade.queryServerAccountPage(pageQuery);
        return table.getData();
    }

    @Override
    public DataTable<CrystalServerVO.AssetServer> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQuery pageQuery) {
        DataTable<EdsAsset> table = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        return crystalWrapper.wrapToTarget(table);
    }

}
