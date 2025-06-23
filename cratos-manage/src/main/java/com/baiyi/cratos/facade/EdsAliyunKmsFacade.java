package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.kms.AliyunKmsParam;
import com.baiyi.cratos.domain.view.aliyun.AliyunKmsVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/23 10:04
 * &#064;Version 1.0
 */
public interface EdsAliyunKmsFacade {

    DataTable<AliyunKmsVO.Secret> queryAliyunKmsSecretPage(AliyunKmsParam.SecretPageQuery pageQuery);

}
