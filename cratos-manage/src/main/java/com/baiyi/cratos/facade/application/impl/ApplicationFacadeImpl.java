package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.facade.application.ApplicationFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.application.ApplicationResourceWrapper;
import com.baiyi.cratos.wrapper.application.ApplicationWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 11:16
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationFacadeImpl implements ApplicationFacade {

    private final ApplicationService applicationService;
    private final ApplicationWrapper applicationWrapper;
    private final ApplicationResourceFacade resourceFacade;
    private final ApplicationResourceWrapper applicationResourceWrapper;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.APPLICATION)
    public DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<Application> table = applicationService.queryApplicationPage(pageQuery.toParam());
        return applicationWrapper.wrapToTarget(table);
    }

    @Override
    public void addApplication(ApplicationParam.AddApplication addApplication) {
        Application application = addApplication.toTarget();
        applicationService.add(application);
    }

    @Override
    public void updateApplication(ApplicationParam.UpdateApplication updateApplication) {
        Application application = applicationService.getById(updateApplication.getId());
        if (application != null) {
            application.setConfig(updateApplication.getConfig());
            application.setValid(updateApplication.getValid());
            application.setComment(updateApplication.getComment());
            applicationService.updateByPrimaryKey(application);
        }
    }

    @Override
    public void scanApplicationResource(ApplicationParam.ScanResource scanResource) {
        //applicationResourceWrapper.clean(scanResource);
        resourceFacade.scan(scanResource.getName());
    }

    @Override
    @Async
    public void scanAllApplicationResource() {
        resourceFacade.scanAll();
    }

    @Override
    public void deleteById(int id) {
        applicationService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return applicationService;
    }

}
