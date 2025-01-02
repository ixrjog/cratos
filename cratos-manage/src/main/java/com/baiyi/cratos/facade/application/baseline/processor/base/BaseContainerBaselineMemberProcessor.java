package com.baiyi.cratos.facade.application.baseline.processor.base;

import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import com.baiyi.cratos.facade.application.baseline.ContainerBaselineMemberProcessor;
import com.baiyi.cratos.facade.application.baseline.factory.BaselineMemberProcessorFactory;
import com.baiyi.cratos.service.ApplicationResourceBaselineMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 15:16
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseContainerBaselineMemberProcessor implements ContainerBaselineMemberProcessor, InitializingBean {

    private final ApplicationResourceBaselineMemberService baselineMemberService;

    protected void save(ApplicationResourceBaselineMember baselineMember) {
        ApplicationResourceBaselineMember dbBaselineMember = baselineMemberService.getByUniqueKey(baselineMember);
        if (dbBaselineMember == null) {
            baselineMemberService.add(baselineMember);
        } else {
            baselineMember.setId(dbBaselineMember.getId());
            baselineMemberService.updateByPrimaryKey(dbBaselineMember);
        }
    }

    @Override
    public void afterPropertiesSet() {
        BaselineMemberProcessorFactory.register(this);
    }

}
