package com.baiyi.cratos.eds.dingtalk;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkDepartmentModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkUserModel;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkDepartmentParam;
import com.baiyi.cratos.eds.dingtalk.repo.DingtalkDepartmentRepo;
import com.baiyi.cratos.eds.dingtalk.repo.DingtalkUserRepo;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DINGTALK_MANAGER_USER_ID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/12 16:48
 * &#064;Version 1.0
 */
public class EdsDingtalkTest extends BaseEdsTest<EdsConfigs.Dingtalk> {

    @Resource
    private DingtalkDepartmentRepo dingtalkDepartmentRepo;

    @Resource
    private DingtalkUserRepo dingtalkUserRepo;

    @Resource
    private EdsAssetService assetService;

    @Resource
    private EdsAssetIndexService assetIndexService;

    @Test
    void test2() {
        EdsConfigs.Dingtalk dingtalk = getConfig(43);
        DingtalkDepartmentModel.GetDepartmentResult result = dingtalkDepartmentRepo.get(dingtalk,
                DingtalkDepartmentParam.GetDepartment.builder()
                        .build());
        System.out.println(result);
    }

    @Test
    void test3() {
        EdsConfigs.Dingtalk dingtalk = getConfig(17);
        DingtalkUserModel.GetUser result = dingtalkUserRepo.getUser(dingtalk, "2432404506-2088177608");
        System.out.println(result);
    }

    @Test
    void test4() {
        EdsConfigs.Dingtalk dingtalk = getConfig(43);
        String result = dingtalkUserRepo.getUserTest(dingtalk, "52336726782698");
        System.out.println(result);
    }

    @Test
    void test5() {
        List<EdsAsset> assets = assetService.queryInstanceAssets(107, EdsAssetTypeEnum.DINGTALK_USER.name());
        for (EdsAsset asset : assets) {
            EdsAssetIndex index = assetIndexService.getByAssetIdAndName(asset.getId(), DINGTALK_MANAGER_USER_ID);
            if (index == null) {
                String msg = "UserId={}, Name={}, 没有找到对应的主管记录!";
                System.out.println(StringFormatter.arrayFormat(msg, asset.getAssetId(), asset.getName()));
            }
        }
    }

}
