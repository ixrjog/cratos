package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.view.ssh.SshSessionVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午11:35
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SshSessionWrapper extends BaseDataTableConverter<SshSessionVO.Session, SshSession> implements IBaseWrapper<SshSessionVO.Session> {

    @Override
    //@BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.CREDENTIAL})
    public void wrap(SshSessionVO.Session session) {
    }

}