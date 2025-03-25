package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.google.api.client.util.Lists;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 15:00
 * &#064;Version 1.0
 */
public class WorkOrderFacadeTest extends BaseUnit {

    @Resource
    public WorkOrderFacade workOrderFacade;

    @Resource
    public WorkOrderTicketFacade workOrderTicketFacade;

    @Resource
    public WorkOrderTicketEntryFacade workOrderTicketEntryFacade;

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
                .expiredTime(ExpiredUtil.generateExpirationTime(90, TimeUnit.DAYS))
                .build());
        UserPermissionBusinessParam.BusinessPermission detail = UserPermissionBusinessParam.BusinessPermission.builder()
                .businessId(2)
                .name("kili")
                .roleMembers(roleMembers)
                .build();
        WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry = WorkOrderTicketParam.AddApplicationPermissionTicketEntry.builder()
                .ticketId(1)
                .detail(detail)
                .build();
        workOrderTicketEntryFacade.addApplicationPermissionTicketEntry(addTicketEntry);
    }

    @Test
    void test4() {
        WorkOrderTicketVO.TicketDetails details = workOrderTicketFacade.getTicket("iaw9e998");
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
        SessionUtils.setUsername("baiyi");
        WorkOrderTicketParam.MyTicketPageQuery pageQuery = WorkOrderTicketParam.MyTicketPageQuery.builder()
                .mySubmitted(true)
                .length(10)
                .page(1)
                .build();
        DataTable<WorkOrderTicketVO.Ticket> dataTable = workOrderTicketFacade.queryMyTicketPage(pageQuery);
        System.out.println(dataTable);
    }

}
