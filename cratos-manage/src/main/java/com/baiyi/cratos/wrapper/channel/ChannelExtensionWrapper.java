package com.baiyi.cratos.wrapper.channel;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.generator.ChannelExtension;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.domain.view.channel.ChannelExtensionVO;
import com.baiyi.cratos.service.CertificateService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/28 10:42
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelExtensionWrapper extends BaseDataTableConverter<ChannelExtensionVO.Extension, ChannelExtension> implements BaseWrapper<ChannelExtensionVO.Extension> {

    private final CertificateService certificateService;

    @Override
    public void wrap(ChannelExtensionVO.Extension vo) {
        // 插入证书对象
        if (BusinessTypeEnum.CERTIFICATE.name()
                .equals(vo.getBusinessType())) {
            Certificate certificate = certificateService.getById(vo.getBusinessId());
            if (certificate != null) {
                CertificateVO.Certificate certificateVO = BeanCopierUtils.copyProperties(
                        certificate, CertificateVO.Certificate.class);
                vo.setExtObj(certificateVO);
            }
        }
    }

}