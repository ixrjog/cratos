package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.mapper.EdsAssetIndexMapper;
import com.baiyi.cratos.service.EdsAssetIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/27 14:34
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class EdsAssetIndexServiceImpl implements EdsAssetIndexService {

    private final EdsAssetIndexMapper edsAssetIndexMapper;

    @Override
    public EdsAssetIndex getByUniqueKey(EdsAssetIndex edsAssetIndex) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", edsAssetIndex.getInstanceId())
                .andEqualTo("assetId", edsAssetIndex.getAssetId())
                .andEqualTo("name", edsAssetIndex.getName());
        return edsAssetIndexMapper.selectOneByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByAssetId(int assetId) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return edsAssetIndexMapper.selectByExample(example);
    }

    @Override
    public int selectCountByAssetId(int assetId) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return edsAssetIndexMapper.selectCountByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByName(String name) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return edsAssetIndexMapper.selectByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByValue(String value) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("value", value);
        return edsAssetIndexMapper.selectByExample(example);
    }

}
