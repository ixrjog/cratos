package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.crystal.CrystalServerVO;
import com.baiyi.cratos.domain.view.server.ServerAccountVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.facade.crystal.CrystalAssetTypeOptionsFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/9 15:08
 * &#064;Version 1.0
 */
public interface CrystalFacade extends InitializingBean {

    OptionsVO.Options getInstanceAssetTypeOptions(String instanceType);

    List<ServerAccountVO.ServerAccount> getServerAccountOptions(int size);

    DataTable<CrystalServerVO.AssetServer> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQuery assetPageQuery);

    default void afterPropertiesSet() {
        CrystalAssetTypeOptionsFactory.register(EdsInstanceTypeEnum.ALIYUN, EdsAssetTypeEnum.ALIYUN_ECS);
        CrystalAssetTypeOptionsFactory.register(EdsInstanceTypeEnum.AWS, EdsAssetTypeEnum.AWS_EC2);
        CrystalAssetTypeOptionsFactory.register(EdsInstanceTypeEnum.KUBERNETES, EdsAssetTypeEnum.KUBERNETES_NODE);
    }

}
