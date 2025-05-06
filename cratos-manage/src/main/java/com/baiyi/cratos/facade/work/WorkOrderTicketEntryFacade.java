package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:32
 * &#064;Version 1.0
 */
public interface WorkOrderTicketEntryFacade {

    void addApplicationPermissionTicketEntry(WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry);

    void addApplicationElasticScalingTicketEntry(
            WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry addTicketEntry);

    void addComputerPermissionTicketEntry(WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry);

    void addRevokeUserPermissionTicketEntry(WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry addTicketEntry);

    void addGitLabProjectPermissionTicketEntry(
            WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry addTicketEntry);

    void addGitLabGroupPermissionTicketEntry(WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry addTicketEntry);

    void deleteById(int id);

    void setValidById(int id);

    void deleteTicketEntry(WorkOrderTicketParam.DeleteTicketEntry deleteTicketEntry);

    void deleteByTicketId(int ticketId);

}
