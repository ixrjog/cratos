package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.domain.model.LdapUserGroupModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:32
 * &#064;Version 1.0
 */
public interface WorkOrderTicketEntryFacade {

    List<EdsInstanceVO.EdsInstance> queryDataWorksInstanceTicketEntry();

    List<EdsInstanceVO.EdsInstance> queryRocketMqInstanceTicketEntry();

    List<EdsAssetVO.Asset> queryApplicationResourceDeploymentTicketEntry(
            WorkOrderTicketParam.QueryApplicationResourceDeploymentTicketEntry queryApplicationResourceDeploymentTicketEntry);

    List<LdapUserGroupModel.Role> queryLdapRolePermissionTicketEntry(
            WorkOrderTicketParam.QueryLdapRolePermissionTicketEntry queryLdapRolePermissionTicketEntry);

    void addApplicationPermissionTicketEntry(WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry);

    void addApplicationProdPermissionTicketEntry(
            WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry);

    void addApplicationTestPermissionTicketEntry(
            WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry);

    void addApplicationElasticScalingTicketEntry(
            WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry addTicketEntry);

    void addApplicationDeploymentElasticScalingTicketEntry(
            WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry addTicketEntry);

    void addComputerPermissionTicketEntry(WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry);

    void addRevokeUserPermissionTicketEntry(WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry addTicketEntry);

    void addLdapRolePermissionTicketEntry(WorkOrderTicketParam.AddLdapRolePermissionTicketEntry addTicketEntry);

    void addGitLabProjectPermissionTicketEntry(
            WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry addTicketEntry);

    void addGitLabGroupPermissionTicketEntry(WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry addTicketEntry);

    void addAliyunDataWorksInstanceTicketEntry(
            WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry addTicketEntry);

    void addCreateAliyunRamUserTicketEntry(WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry addTicketEntry);

    void addResetAliyunRamUserTicketEntry(WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry addTicketEntry);

    void addResetAwsIamUserTicketEntry(WorkOrderTicketParam.AddResetAwsIamUserTicketEntry addTicketEntry);

    void addCreateAwsTransferSftpUserTicketEntry(
            WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry addTicketEntry);

    void addAwsIamPolicyPermissionTicketEntry(WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry addTicketEntry);

    void addAliyunRamPolicyPermissionTicketEntry(
            WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry addTicketEntry);

    void addCreateFrontEndApplicationTicketEntry(
            WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry addTicketEntry);

    void addApplicationDeletePodTicketEntry(WorkOrderTicketParam.AddApplicationDeletePodTicketEntry addTicketEntry);

    void addDeploymentPodTicketEntry(WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry addTicketEntry);

    void addResetAlimailUserTicketEntry(WorkOrderTicketParam.AddResetAlimailUserTicketEntry addTicketEntry);

    void addCreateAliyunOnsTopicTicketEntry(WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry addTicketEntry);

    void addCreateAliyunOnsConsumerGroupTicketEntry(
            WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry addTicketEntry);


    void deleteById(int id);

    void deleteAllByTicketId(int ticketId);

    void setValidById(int id);

    void deleteTicketEntry(WorkOrderTicketParam.DeleteTicketEntry deleteTicketEntry);

    void deleteByTicketId(int ticketId);

    OptionsVO.Options getLdapGroupOptions();

}
