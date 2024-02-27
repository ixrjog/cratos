package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.mapper.EdsAssetMapper;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/26 15:25
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EdsAssetServiceImpl implements EdsAssetService {

    private final EdsAssetMapper edsAssetMapper;

    @Override
    public EdsAsset getByUniqueKey(EdsAsset edsAsset) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", edsAsset.getInstanceId())
                .andEqualTo("assetType", edsAsset.getAssetType())
                .andEqualTo("assetKey", edsAsset.getAssetKey());
        return edsAssetMapper.selectOneByExample(example);
    }

    @Override
    public List<EdsAsset> queryInstanceAssets(Integer instanceId, String assetType) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId)
                .andEqualTo("assetType", assetType);
        return edsAssetMapper.selectByExample(example);
    }

}
