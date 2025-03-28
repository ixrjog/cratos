package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.google.api.client.util.Lists;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/28 18:20
 * &#064;Version 1.0
 */
public class WorkOrderFacade2Test extends BaseUnit {

    @Resource
    public WorkOrderFacade workOrderFacade;
    @Resource
    public WorkOrderTicketFacade workOrderTicketFacade;
    @Resource
    public WorkOrderTicketEntryFacade workOrderTicketEntryFacade;

    @Test
    void test1() {
        // 创建工单
        SessionUtils.setUsername("baiyi");
        WorkOrderTicketParam.CreateTicket createTicket = WorkOrderTicketParam.CreateTicket.builder()
                .workOrderKey("APPLICATION_PERMISSION")
                .build();
        WorkOrderTicketVO.TicketDetails details = workOrderTicketFacade.createTicket(createTicket);
        int ticketId = details.getTicket()
                .getId();
        String ticketNo = details.getTicketNo();

        // 新增授权
        List<UserPermissionBusinessParam.RoleMember> roleMembers = makeRoleMember();
        UserPermissionBusinessParam.BusinessPermission detail = UserPermissionBusinessParam.BusinessPermission.builder()
                .businessId(2)
                .name("kili")
                .roleMembers(roleMembers)
                .build();
        WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry = WorkOrderTicketParam.AddApplicationPermissionTicketEntry.builder()
                .ticketId(ticketId)
                .detail(detail)
                .build();
        workOrderTicketEntryFacade.addApplicationPermissionTicketEntry(addTicketEntry);

        // 提交工单
        List<WorkOrderTicketParam.NodeApprover> nodeApprovers = List.of(WorkOrderTicketParam.NodeApprover.builder()
                .nodeName("TeamLeader")
                .username("baiyi")
                .build());
        WorkOrderTicketParam.SubmitTicket submitTicket = WorkOrderTicketParam.SubmitTicket.builder()
                .ticketNo(ticketNo)
                .nodeApprovers(nodeApprovers)
                .applyRemark("这是测试")
                .build();
        workOrderTicketFacade.submitTicket(submitTicket);

    }

    private List<UserPermissionBusinessParam.RoleMember> makeRoleMember() {
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
        return roleMembers;
    }

}