package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.cr.model.v20181201.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.cratos.eds.aliyun.client.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/19 上午9:59
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunAcrRepo {

    private final AliyunClient aliyunClient;

    public interface Query {
        int PAGE_SIZE = 30;
        String NORMAL = "NORMAL";
    }

    // Region

    public List<ListInstanceRegionResponse.RegionsItem> listRegion(
            EdsAliyunConfigModel.Aliyun aliyun) throws ClientException {
        ListInstanceRegionRequest request = new ListInstanceRegionRequest();
        ListInstanceRegionResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response.getRegions();
    }

    // Instance

    /**
     * {
     * "defaultRepoType":"PRIVATE",
     * "namespaceStatus":"NORMAL",
     * "namespaceId":"crn-g0h399e0ayt6ax00",
     * "autoCreateRepo":true,
     * "instanceId":"cri-4v9b8l2gc3en0x34",
     * "namespaceName":"daily"
     * }
     *
     * @param regionId
     * @param aliyun
     * @param instanceId
     * @return
     * @throws ClientException
     */
    public List<ListNamespaceResponse.NamespacesItem> listNamespace(String regionId, EdsAliyunConfigModel.Aliyun aliyun,
                                                                    String instanceId) throws ClientException {
        ListNamespaceRequest request = new ListNamespaceRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setNamespaceStatus(Query.NORMAL);
        request.setPageSize(Query.PAGE_SIZE);
        int total = 0;
        int pageNo = 1;
        List<ListNamespaceResponse.NamespacesItem> namespacesItems = Lists.newArrayList();
        while (total == 0 || total == namespacesItems.size()) {
            request.setPageNo(pageNo);
            ListNamespaceResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            List<ListNamespaceResponse.NamespacesItem> nowData = response.getNamespaces();
            if (CollectionUtils.isEmpty(nowData)) {
                break;
            } else {
                namespacesItems.addAll(nowData);
            }
            if (total == 0) {
                total = Integer.parseInt(response.getTotalCount());
            }
            pageNo++;
        }
        return namespacesItems;
    }

    /**
     * @param regionId
     * @param aliyun
     * @return
     * @throws ClientException
     */
    public List<ListInstanceResponse.InstancesItem> listInstance(String regionId,
                                                                 EdsAliyunConfigModel.Aliyun aliyun) throws ClientException {
        System.out.println(regionId);
        ListInstanceRequest request = new ListInstanceRequest();
        request.setSysRegionId(regionId);
        request.setPageSize(Query.PAGE_SIZE);
        int total = 0;
        int pageNo = 1;
        List<ListInstanceResponse.InstancesItem> instancesItems = Lists.newArrayList();
        while (total == 0 || total == instancesItems.size()) {
            request.setPageNo(pageNo);
            ListInstanceResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            List<ListInstanceResponse.InstancesItem> nowData = response.getInstances();
            if (CollectionUtils.isEmpty(nowData)) {
                break;
            } else {
                instancesItems.addAll(nowData);
            }
            total = response.getTotalCount();
            pageNo++;
        }
        return instancesItems;
    }

    public GetInstanceResponse getInstance(String regionId, EdsAliyunConfigModel.Aliyun aliyun,
                                           String instanceId) throws ClientException {
        GetInstanceRequest request = new GetInstanceRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        return aliyunClient.getAcsResponse(regionId, aliyun, request);
    }

    public List<GetInstanceEndpointResponse.Endpoints> getInstanceEndpoint(String regionId,
                                                                           EdsAliyunConfigModel.Aliyun aliyun,
                                                                           String instanceId) throws ClientException {
        GetInstanceEndpointRequest request = new GetInstanceEndpointRequest();
        request.setEndpointType("internet");
        request.setInstanceId(instanceId);
        request.setSysRegionId(regionId);
        GetInstanceEndpointResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getDomains();
    }


    // Repository
    public List<ListRepositoryResponse.RepositoriesItem> listRepository(String regionId,
                                                                        EdsAliyunConfigModel.Aliyun aliyun,
                                                                        String instanceId) throws ClientException {
        ListRepositoryRequest request = new ListRepositoryRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoStatus(Query.NORMAL);
        request.setPageSize(Query.PAGE_SIZE);
        int total = 0;
        int pageNo = 1;

        List<ListRepositoryResponse.RepositoriesItem> repositoriesItems = Lists.newArrayList();
        while (total == 0 || total == repositoriesItems.size()) {
            request.setPageNo(pageNo);
            ListRepositoryResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            List<ListRepositoryResponse.RepositoriesItem> nowData = response.getRepositories();
            if (CollectionUtils.isEmpty(nowData)) {
                break;
            } else {
                repositoriesItems.addAll(nowData);
            }
            if (total == 0) {
                total = Integer.parseInt(response.getTotalCount());
            }
            pageNo++;
        }
        return repositoriesItems;
    }

    public String getRepositoryId(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceId,
                                  String repoNamespaceName, String repoName) throws ClientException {
        GetRepositoryRequest request = new GetRepositoryRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoNamespaceName(repoNamespaceName);
        request.setRepoName(repoName);
        GetRepositoryResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getRepoId();
    }

    // Image

    /**
     * 查询仓库镜像
     *
     * @param regionId
     * @param aliyun
     * @param instanceId
     * @param repoId
     * @return
     * @throws ClientException
     */
    public List<ListRepoTagResponse.ImagesItem> listImage(String regionId, EdsAliyunConfigModel.Aliyun aliyun,
                                                          String instanceId, String repoId) throws ClientException {
        ListRepoTagRequest request = new ListRepoTagRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoId(repoId);
        request.setPageSize(Query.PAGE_SIZE);
        int total = 0;
        int pageNo = 1;

        List<ListRepoTagResponse.ImagesItem> imagesItems = Lists.newArrayList();
        while (total == 0 || total == imagesItems.size()) {
            request.setPageNo(pageNo);
            ListRepoTagResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            List<ListRepoTagResponse.ImagesItem> nowData = response.getImages();
            if (CollectionUtils.isEmpty(nowData)) {
                break;
            } else {
                imagesItems.addAll(nowData);
            }
            if (total == 0) {
                total = Integer.parseInt(response.getTotalCount());
            }
            pageNo++;
        }
        return imagesItems;
    }

    /**
     * 查询仓库镜像
     *
     * @param regionId
     * @param aliyun
     * @param instanceId
     * @param repoId
     * @param size
     * @return
     * @throws ClientException
     */
    public List<ListRepoTagResponse.ImagesItem> listImage(String regionId, EdsAliyunConfigModel.Aliyun aliyun,
                                                          String instanceId, String repoId,
                                                          int size) throws ClientException {
        ListRepoTagRequest request = new ListRepoTagRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoId(repoId);
        request.setPageSize(Math.min(size, Query.PAGE_SIZE));
        ListRepoTagResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getImages();
    }

    public GetRepoTagResponse getImage(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceId,
                                       String repoId, String tag) throws ClientException {
        GetRepoTagRequest request = new GetRepoTagRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoId(repoId);
        request.setTag(tag);
        return aliyunClient.getAcsResponse(regionId, aliyun, request);
    }

}
