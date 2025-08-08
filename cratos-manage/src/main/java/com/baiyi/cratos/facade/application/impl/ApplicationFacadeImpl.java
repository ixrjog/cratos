package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.model.ApplicationModel;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.facade.ApplicationFacade;
import com.baiyi.cratos.facade.UserPermissionFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.application.ApplicationResourceWrapper;
import com.baiyi.cratos.wrapper.application.ApplicationWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
    private final ApplicationResourceFacade applicationResourceFacade;
    private final ApplicationResourceWrapper applicationResourceWrapper;
    private final UserPermissionFacade userPermissionFacade;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.APPLICATION)
    public DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<Application> table = applicationService.queryApplicationPage(pageQuery.toParam());
        return applicationWrapper.wrapToTarget(table);
    }

    @Override
    public ApplicationVO.Application getApplicationByName(ApplicationParam.GetApplication getApplication) {
        Application application = applicationService.getByName(getApplication.getName());
        if (Objects.isNull(application)) {
            throw new NullPointerException("Application not found");
        }
        return applicationWrapper.wrapToTarget(application);
    }

    @Override
    public void addApplication(ApplicationParam.AddApplication addApplication) {
        Application application = addApplication.toTarget();
        applicationService.add(application);
        // 异步处理
        ((ApplicationFacadeImpl) AopContext.currentProxy()).asyncScanApplicationResource(application.getName());
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
    public Application createApplication(ApplicationModel.CreateFrontEndApplication createFrontEndApplication) {
        ApplicationConfigModel.Repository repository = ApplicationConfigModel.Repository.builder()
                .type("gitLab")
                .sshUrl(createFrontEndApplication.getRepository()
                        .getSshUrl())
                .build();
        ApplicationConfigModel.Config config = ApplicationConfigModel.Config.builder()
                .repository(repository)
                .build();
        Application application = Application.builder()
                .name(createFrontEndApplication.getApplicationName())
                .valid(true)
                .config(config.dump())
                .comment(createFrontEndApplication.getComment())
                .build();
        applicationService.add(application);
        return application;
    }

    @Async
    public void asyncScanApplicationResource(String name) {
        ApplicationParam.ScanResource scanResource = ApplicationParam.ScanResource.builder()
                .name(name)
                .build();
        this.scanApplicationResource(scanResource);
    }

    @Override
    public void scanApplicationResource(ApplicationParam.ScanResource scanResource) {
        applicationResourceFacade.scan(scanResource.getName());
    }

    @Override
    @Async
    public void scanAllApplicationResource() {
        applicationResourceFacade.scanAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        // 删除应用绑定的资源
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .businessId(id)
                .build();
        applicationResourceFacade.deleteByBusiness(byBusiness);
        // 删除授权
        userPermissionFacade.revokeByBusiness(byBusiness);
        applicationService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return applicationService;
    }

}
