package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsConfigParam;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:07
 * @Version 1.0
 */
public interface EdsFacade {

    DataTable<EdsInstanceVO.EdsInstance> queryEdsInstancePage(EdsInstanceParam.InstancePageQuery pageQuery);

    EdsInstanceVO.EdsInstance getEdsInstanceById(int instanceId);

    void registerEdsInstance(EdsInstanceParam.RegisterInstance registerEdsInstance);

    void updateEdsInstance(EdsInstanceParam.UpdateInstance updateEdsInstance);

    DataTable<EdsConfigVO.EdsConfig> queryEdsConfigPage(EdsConfigParam.EdsConfigPageQuery pageQuery);

    EdsConfigVO.EdsConfig getEdsConfigById(int configId);

    void addEdsConfig(EdsConfigParam.AddEdsConfig addEdsConfig);

    void updateEdsConfig(EdsConfigParam.UpdateEdsConfig updateEdsConfig);

    void setEdsConfigValidById(int id);

    void deleteEdsConfigById(int id);

    void importEdsInstanceAsset(EdsInstanceParam.ImportInstanceAsset importInstanceAsset);

    EdsInstanceProviderHolder<?, ?> buildHolder(Integer instanceId, String assetType);

    DataTable<EdsAssetVO.Asset> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQuery assetPageQuery);

    void deleteEdsAssetById(int id);

    List<EdsInstance> queryValidEdsInstanceByType(String edsType);

    EdsAssetVO.AssetToBusiness<?> getToBusinessTarget(Integer assetId);

    void deleteEdsInstanceAsset(EdsInstanceParam.DeleteInstanceAsset deleteInstanceAsset);

    List<EdsAssetVO.Index> queryAssetIndexByAssetId(int assetId);

    EdsAssetVO.Asset queryAssetByUniqueKey(EdsInstanceParam.QueryAssetByUniqueKey queryAssetByUniqueKey);

}
