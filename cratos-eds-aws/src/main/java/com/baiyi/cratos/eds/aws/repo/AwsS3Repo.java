package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.s3.model.Bucket;
import com.baiyi.cratos.eds.aws.service.AmazonS3Service;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/19 下午5:11
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsS3Repo {

    public static List<Bucket> listBuckets(EdsAwsConfigModel.Aws aws) {
        return AmazonS3Service.buildAmazonS3(aws)
                .listBuckets();
    }

}
