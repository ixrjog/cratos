package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.param.http.env.EnvParam;
import com.baiyi.cratos.mapper.EnvMapper;
import com.baiyi.cratos.service.EnvService;
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
 * @Author baiyi
 * @Date 2024/3/19 14:27
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.ENV)
public class EnvServiceImpl implements EnvService {

    private final EnvMapper envMapper;

    @Override
    public DataTable<Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery) {
        Page<Env> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Env> data = envMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Env getByUniqueKey(@NonNull Env record) {
        Example example = new Example(Env.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envName", record.getEnvName());
        return envMapper.selectOneByExample(example);
    }

    @Override
    public Env getByEnvName(@NonNull String envName) {
        Env uniqueKey = Env.builder()
                .envName(envName)
                .build();
        return getByUniqueKey(uniqueKey);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:ENV:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
