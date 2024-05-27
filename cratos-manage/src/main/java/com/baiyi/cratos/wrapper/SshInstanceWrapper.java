package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.view.ssh.SshInstanceVO;
import com.baiyi.cratos.service.SshSessionInstanceService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午11:47
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.SSH_INSTANCE)
public class SshInstanceWrapper extends BaseDataTableConverter<SshInstanceVO.Instance, SshSessionInstance> implements IBusinessWrapper<SshInstanceVO.HasSessionInstances, SshInstanceVO.Instance> {

    private final SshSessionInstanceService sshSessionInstanceService;

    @Override
    public void wrap(SshInstanceVO.Instance instance) {
        // This is a good idea
    }

    @Override
    public void businessWrap(SshInstanceVO.HasSessionInstances hasSessionInstances) {
        List<SshSessionInstance> instances = sshSessionInstanceService.queryBySessionId(
                hasSessionInstances.getSessionId());
        if (CollectionUtils.isEmpty(instances)) {
            return;
        }
        hasSessionInstances.setSessionInstances(instances.stream()
                .map(this::wrapToTarget)
                .toList());
    }

}