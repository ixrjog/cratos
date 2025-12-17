package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.IOUtils;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.model.ApplicationReplicasModel;
import com.baiyi.cratos.domain.model.LdapUserGroupModel;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.facade.ApplicationFacade;
import com.baiyi.cratos.service.ApplicationService;
import com.google.api.client.util.Lists;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.baiyi.cratos.workorder.enums.TicketState.COMPLETED;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 15:00
 * &#064;Version 1.0
 */
public class WorkOrderFacadeTest extends BaseUnit {

    @Resource
    private WorkOrderFacade workOrderFacade;

    @Resource
    private WorkOrderTicketFacade workOrderTicketFacade;

    @Resource
    private WorkOrderTicketEntryFacade workOrderTicketEntryFacade;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationFacade applicationFacade;

    @Test
    void test1() {
        WorkOrderVO.Menu menu = workOrderFacade.getWorkOrderMenu();
        System.out.println(menu);
    }

    @Test
    void test2() {
        SessionUtils.setUsername("baiyi");
        WorkOrderTicketParam.CreateTicket createTicket = WorkOrderTicketParam.CreateTicket.builder()
                .workOrderKey("APPLICATION_PERMISSION")
                .build();
        workOrderTicketFacade.createTicket(createTicket);
    }

    @Test
    void test3() {
        List<UserPermissionBusinessParam.RoleMember> roleMembers = Lists.newArrayList();
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("dev")
                                .checked(false)
                                .build());
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("daily")
                                .checked(false)
                                .build());
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("sit")
                                .checked(false)
                                .build());
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("pre")
                                .checked(false)
                                .build());
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("prod")
                                .checked(false)
                                .expiredTime(ExpiredUtils.generateExpirationTime(90, TimeUnit.DAYS))
                                .build());
        UserPermissionBusinessParam.BusinessPermission detail = UserPermissionBusinessParam.BusinessPermission.builder()
                .businessId(2)
                .name("kili")
                .roleMembers(roleMembers)
                .build();
        WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry = WorkOrderTicketParam.AddApplicationPermissionTicketEntry.builder()
                .ticketId(3)
                .detail(detail)
                .build();
        workOrderTicketEntryFacade.addApplicationPermissionTicketEntry(addTicketEntry);
    }

    @Test
    void test31() {
        List<UserPermissionBusinessParam.RoleMember> roleMembers = Lists.newArrayList();
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("dev")
                                .checked(false)
                                .build());
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("daily")
                                .checked(false)
                                .build());
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("sit")
                                .checked(false)
                                .build());
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("pre")
                                .checked(false)
                                .build());
        roleMembers.add(UserPermissionBusinessParam.RoleMember.builder()
                                .role("prod")
                                .checked(false)
                                .expiredTime(ExpiredUtils.generateExpirationTime(90, TimeUnit.DAYS))
                                .build());
        UserPermissionBusinessParam.BusinessPermission detail = UserPermissionBusinessParam.BusinessPermission.builder()
                .name("tms-newpos")
                .roleMembers(roleMembers)
                .build();
        WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry = WorkOrderTicketParam.AddComputerPermissionTicketEntry.builder()
                .ticketId(3)
                .detail(detail)
                .build();
        workOrderTicketEntryFacade.addComputerPermissionTicketEntry(addTicketEntry);
    }

    @Test
    void test4() {
        WorkOrderTicketVO.TicketDetails details = workOrderTicketFacade.makeTicketDetails("y64gn54l");
        System.out.println(details);
    }

//    @Test
//    void test5() {
//        SessionUtils.setUsername("baiyi");
//        WorkOrderTicketParam.SubmitTicket submitTicket = WorkOrderTicketParam.SubmitTicket.builder()
//                .applyRemark("Test Ticket")
//                .ticketId(1)
//                .build();
//        workOrderTicketFacade.submitTicket(submitTicket);
//    }

    @Test
    void test6() {
        SessionUtils.setUsername("xiuyuan");
        WorkOrderTicketParam.MyTicketPageQuery pageQuery = WorkOrderTicketParam.MyTicketPageQuery.builder()
                .mySubmitted(true)
                .length(10)
                .page(1)
                .build();
        DataTable<WorkOrderTicketVO.Ticket> dataTable = workOrderTicketFacade.queryMyTicketPage(pageQuery);
        System.out.println(dataTable);
    }

    @Test
    void test7() {
        WorkOrderTicketParam.SimpleTicketNo simpleTicketNo = WorkOrderTicketParam.SimpleTicketNo.builder()
                .ticketNo("welmynju")
                .build();
        workOrderTicketFacade.doNextStateOfTicket(simpleTicketNo);
    }

    @Test
    void test8() {
        SessionUtils.setUsername("baiyi");
        WorkOrderTicketParam.CreateTicket createTicket = WorkOrderTicketParam.CreateTicket.builder()
                .workOrderKey("APPLICATION_ELASTIC_SCALING")
                .build();
        workOrderTicketFacade.createTicket(createTicket);
    }

    @Test
    void test9() {
        ApplicationVO.Application application = applicationFacade.getApplicationByName(
                ApplicationParam.GetApplication.builder()
                        .name("leo-demo")
                        .build());
        ApplicationReplicasModel.ApplicationConfig config = ApplicationReplicasModel.ApplicationConfig.builder()
                .expectedReplicas(10)
                .build();
        ApplicationReplicasModel.ApplicationConfigurationChange detail = ApplicationReplicasModel.ApplicationConfigurationChange.builder()
                .application(application)
                .config(config)
                .namespace("prod")
                .build();
        WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry addTicketEntry = WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry.builder()
                .detail(detail)
                .ticketId(207)
                .build();
        workOrderTicketEntryFacade.addApplicationElasticScalingTicketEntry(addTicketEntry);
    }

    @Test
    void test10() {
        workOrderTicketFacade.adminDeleteTicketById(210);
        workOrderTicketFacade.adminDeleteTicketById(211);
    }

    @Test
    void test11() {
        OptionsVO.Options options = workOrderTicketEntryFacade.getLdapGroupOptions();
        System.out.println(options);
    }

    @Test
    void test12() {
        WorkOrderTicketParam.QueryLdapRolePermissionTicketEntry query = WorkOrderTicketParam.QueryLdapRolePermissionTicketEntry.builder()
                .group("nexus")
                .build();
        List<LdapUserGroupModel.Role> list = workOrderTicketEntryFacade.queryLdapRolePermissionTicketEntry(query);
        System.out.println(list);
    }

    @Test
    void test13() {
        IOUtils.createFile("/Users/liangjian/", "workworder.txt", "");
        WorkOrderTicketParam.TicketPageQuery pageQuery = WorkOrderTicketParam.TicketPageQuery.builder()
                .page(1)
                .length(10)
                .ticketState(COMPLETED.name())
                .build();
        int total = 3391;
        int size = 0;
        while (true) {
            DataTable<WorkOrderTicketVO.Ticket> dataTable = workOrderTicketFacade.queryTicketPage(pageQuery);
            for (WorkOrderTicketVO.Ticket e : dataTable.getData()) {
                IOUtils.appendFile(
                        StringFormatter.format("#### No: {}\n", e.getTicketNo()),
                        "/Users/liangjian/workworder.txt"
                );
                IOUtils.appendFile(
                        StringFormatter.arrayFormat(
                                "Name: {} Applicant: {} CompletedAt: {}\n", e.getWorkOrder()
                                        .getI18nData()
                                        .getLangMap()
                                        .get("zh-cn")
                                        .getDisplayName(), e.getApplicant()
                                        .getDisplayName(), e.getCompletedAt()
                        ), "/Users/liangjian/workworder.txt"
                );
                IOUtils.appendFile(
                        e.getTicketAbstract()
                                .getMarkdown() + "\n\n", "/Users/liangjian/workworder.txt"
                );
            }
            size = size + dataTable.getData()
                    .size();
            pageQuery.setPage(pageQuery.getPage() + 1);
            System.out.println(size);
            if (size >= total) {
                break;
            }
        }
    }

}
