package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.view.ssh.SshInstanceVO;
import com.baiyi.cratos.service.SshSessionInstanceCommandService;
import com.baiyi.cratos.service.SshSessionInstanceService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.builder.ResourceCountBuilder;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.baiyi.cratos.domain.enums.BusinessTypeEnum.SSH_COMMAND;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午11:47
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.SSH_INSTANCE)
public class SshInstanceWrapper extends BaseDataTableConverter<SshInstanceVO.Instance, SshSessionInstance> implements IBusinessWrapper<SshInstanceVO.HasSessionInstances, SshInstanceVO.Instance> {

    private final SshSessionInstanceService instanceService;

    private final SshSessionInstanceCommandService commandService;

    @Override
    public void wrap(SshInstanceVO.Instance vo) {
        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(makeResourceCountForCommand(vo))
                .build();
        vo.setResourceCount(resourceCount);
    }

    private Map<String, Integer> makeResourceCountForCommand(SshInstanceVO.Instance instance) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(SSH_COMMAND.name(), commandService.selectCountByInstanceId(instance.getId()));
        return resourceCount;
    }

    @Override
    public void businessWrap(SshInstanceVO.HasSessionInstances hasSessionInstances) {
        List<SshSessionInstance> instances = instanceService.queryBySessionId(hasSessionInstances.getSessionId());
        if (CollectionUtils.isEmpty(instances)) {
            return;
        }
        hasSessionInstances.setSessionInstances(instances.stream()
                .map(this::wrapToTarget)
                .toList());
    }

}