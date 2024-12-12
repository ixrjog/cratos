package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.constants.SchedulerLockNameConstants;
import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.facade.RbacFacade;
import com.baiyi.cratos.facade.UserPermissionFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.facade.application.resource.scanner.ResourceScannerFactory;
import com.baiyi.cratos.facade.rbac.RbacUserRoleFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 14:42
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationResourceFacadeImpl implements ApplicationResourceFacade {

    private final ApplicationService applicationService;
    private final ApplicationResourceService resourceService;
    private final UserPermissionFacade userPermissionFacade;
    private final RbacFacade rbacFacade;
    private final RbacUserRoleFacade rbacUserRoleFacade;

    @Override
    public void scan(String applicationName) {
        // 扫描资产
        Application application = applicationService.getByName(applicationName);
        if (application == null) {
            return;
        }
        doScan(application);
    }

    private void doScan(Application application) {
        ApplicationConfigModel.Config config = ApplicationConfigModel.loadAs(application);
        log.info("Scan application {} resources.", application.getName());
        ResourceScannerFactory.scanAndBindAssets(application, config);
    }

    @Override
    @SchedulerLock(name = SchedulerLockNameConstants.SCAN_ALL_APPLICATION_RESOURCES_TASK, lockAtMostFor = "3m", lockAtLeastFor = "3m")
    public void scanAll() {
        int page = 1;
        ApplicationParam.ApplicationPageQueryParam param = ApplicationParam.ApplicationPageQueryParam.builder()
                .page(page)
                .build();
        while (true) {
            DataTable<Application> table = applicationService.queryApplicationPage(param);
            if (CollectionUtils.isEmpty(table.getData())) {
                break;
            }
            table.getData()
                    .forEach(this::doScan);
            param.setPage(++page);
        }
    }

    @Override
    public void deleteById(int id) {
        resourceService.deleteById(id);
    }

    @Override
    public void deleteByBusiness(BaseBusiness.HasBusiness byBusiness) {
        List<ApplicationResource> resources = resourceService.queryByBusiness(byBusiness);
        if (!CollectionUtils.isEmpty(resources)) {
            for (ApplicationResource resource : resources) {
                resourceService.deleteById(resource.getId());
            }
        }
    }

    @Override
    public OptionsVO.Options getNamespaceOptions() {
        return OptionsVO.toOptions(resourceService.getNamespaceOptions());
    }

    @Override
    @SetSessionUserToParam
    public OptionsVO.Options getMyApplicationResourceNamespaceOptions(
            ApplicationParam.GetMyApplicationResourceNamespaceOptions getMyApplicationResourceNamespaceOptions) {
        // Ops
        if (rbacUserRoleFacade.hasAccessLevel(getMyApplicationResourceNamespaceOptions.getSessionUser(),
                AccessLevel.OPS)) {
            return getNamespaceOptions();
        }
        List<String> namespaces = resourceService.getNamespaceOptions();
        if (CollectionUtils.isEmpty(namespaces) || StringUtils.hasText(
                getMyApplicationResourceNamespaceOptions.getSessionUser())) {
            return OptionsVO.NO_OPTIONS_AVAILABLE;
        }
        Application application = applicationService.getByName(
                getMyApplicationResourceNamespaceOptions.getApplicationName());
        if (application == null) {
            return OptionsVO.NO_OPTIONS_AVAILABLE;
        }
        final SimpleBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .businessId(application.getId())
                .build();
        List<OptionsVO.Option> optionList = namespaces.stream()
                .map(namespace -> OptionsVO.Option.builder()
                        .label(namespace)
                        .value(namespace)
                        .disabled(!userPermissionFacade.contains(
                                getMyApplicationResourceNamespaceOptions.getSessionUser(), business, namespace))
                        .build())
                .toList();
        return getMyApplicationResourceNamespaceOptions(getMyApplicationResourceNamespaceOptions.getSessionUser(),
                namespaces, business);
    }

    private OptionsVO.Options getMyApplicationResourceNamespaceOptions(String username, List<String> namespaces,
                                                                       BaseBusiness.HasBusiness business) {
        return OptionsVO.Options.builder()
                .options(namespaces.stream()
                        .map(namespace -> OptionsVO.Option.builder()
                                .label(namespace)
                                .value(namespace)
                                .disabled(userPermissionFacade.contains(username, business, namespace))
                                .build())
                        .toList())
                .build();
    }

}
