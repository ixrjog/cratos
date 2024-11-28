package com.baiyi.cratos.eds;

import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.eds.aws.repo.AwsEcrRepo;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/27 14:38
 * &#064;Version 1.0
 */
public class EcrRepoTest extends BaseEdsTest<EdsAwsConfigModel.Aws> {

    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void test1() {
        EdsAwsConfigModel.Aws aws = getConfig(3);
        List<Repository> list = AwsEcrRepo.describeRepositories("ap-east-1", aws);

        for (Repository repository : list) {
            try {
                AwsEcrRepo.deleteRepository("ap-east-1", aws, repository.getRegistryId(), repository.getRepositoryName());
                System.out.println(repository.getRepositoryName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }

    @Test
    void test2() {
        EdsAwsConfigModel.Aws aws = getConfig(3);
        EdsInstanceParam.AssetPageQuery pageQuery = EdsInstanceParam.AssetPageQuery.builder()
                // AWS PalmPay
                .instanceId(94)
                .assetType(EdsAssetTypeEnum.AWS_ECR_REPOSITORY.name())
                .page(1)
                .length(5000)
                .build();
        DataTable<EdsAsset> dataTable = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        for (EdsAsset asset : dataTable.getData()) {
            if (asset.getAssetKey()
                    .contains("eu-west-1")) {
                //AwsEcrRepo.deleteRepository("eu-west-1", aws, asset.getName());
                System.out.println(asset.getName());
            }

        }
    }

}
