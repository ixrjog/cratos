package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.AssetMaturity;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.param.asset.AssetMaturityParam;
import com.baiyi.cratos.mapper.AssetMaturityMapper;
import com.baiyi.cratos.service.AssetMaturityService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午1:44
 * &#064;Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssetMaturityServiceImpl implements AssetMaturityService {

    private final AssetMaturityMapper assetMaturityMapper;

    @Override
    public AssetMaturity getByUniqueKey(AssetMaturity record) {
        Example example = new Example(AssetMaturity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public List<AssetMaturity> queryByLessThanExpiry(Date date) {
        Example example = new Example(Domain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThan("expiry", date)
                .andEqualTo("valid", true);
        return assetMaturityMapper.selectByExample(example);
    }

    @Override
    public DataTable<AssetMaturity> queryAssetMaturityPage(AssetMaturityParam.AssetMaturityPageQuery pageQuery) {
        Page<AssetMaturity> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<AssetMaturity> data = assetMaturityMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}
