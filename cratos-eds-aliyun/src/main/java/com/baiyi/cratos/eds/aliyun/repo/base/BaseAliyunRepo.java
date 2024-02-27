package com.baiyi.cratos.eds.aliyun.repo.base;

import com.baiyi.cratos.eds.aliyun.client.AliyunClient;
import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2024/2/26 11:30
 * @Version 1.0
 */
public abstract class BaseAliyunRepo {

    @Resource
    protected AliyunClient aliyunClient;

}
