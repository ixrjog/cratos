package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.model.Cert;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.repo.base.BaseCloudflareRepo;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareCertService;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/4 14:37
 * @Version 1.0
 */
@Component
public class CloudflareCertRepo extends BaseCloudflareRepo {

    @Resource
    private CloudflareCertService cloudflareCertService;

    public CloudflareHttpResult<List<Cert.Result>> listCertificatePacks(EdsCloudflareConfigModel.Cloudflare cloudflare, String zoneId) {
        return cloudflareCertService.listCertificatePacks(generateBearer(cloudflare), zoneId);
    }

}
