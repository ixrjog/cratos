package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.CratosInstance;
import com.baiyi.cratos.domain.param.http.cratos.CratosInstanceParam;
import com.baiyi.cratos.mapper.CratosInstanceMapper;
import com.baiyi.cratos.service.CratosInstanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 11:06
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CRATOS_INSTANCE)
public class CratosInstanceServiceImpl implements CratosInstanceService {

    private final CratosInstanceMapper cratosInstanceMapper;

    @Override
    public DataTable<CratosInstance> queryCratosInstancePage(
            CratosInstanceParam.RegisteredInstancePageQueryParam param) {
        Page<CratosInstance> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<CratosInstance> data = cratosInstanceMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public CratosInstance getByHostIp(String hostIp) {
        CratosInstance record = CratosInstance.builder()
                .hostIp(hostIp)
                .build();
        return getByUniqueKey(record);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CRATOS_INSTANCE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public CratosInstance getByUniqueKey(@NonNull CratosInstance record) {
        Example example = new Example(CratosInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("hostIp", record.getHostIp());
        return cratosInstanceMapper.selectOneByExample(example);
    }

    @Override
    public void add(CratosInstance cratosInstance) {
        cratosInstanceMapper.insert(cratosInstance);
    }

}
