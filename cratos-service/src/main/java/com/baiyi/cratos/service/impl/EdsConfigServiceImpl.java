package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.param.eds.EdsConfigParam;
import com.baiyi.cratos.mapper.EdsConfigMapper;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.factory.SupportBusinessServiceFactory;
import com.baiyi.cratos.service.factory.credential.CredentialHolderFactory;
import com.baiyi.cratos.service.factory.credential.ICredentialHolder;
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
 * @Date 2024/2/5 17:57
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.EDS_CONFIG)
public class EdsConfigServiceImpl implements EdsConfigService, ICredentialHolder {

    private final EdsConfigMapper edsConfigMapper;

    @Override
    public DataTable<EdsConfig> queryEdsConfigPage(EdsConfigParam.EdsConfigPageQuery pageQuery) {
        Page<EdsConfig> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<EdsConfig> data = edsConfigMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<EdsConfig> queryByEdsType(String edsType) {
        Example example = new Example(EdsConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("edsType", edsType);
        return edsConfigMapper.selectByExample(example);
    }

    @Override
    public EdsConfig getByUniqueKey(@NonNull EdsConfig record) {
        Example example = new Example(EdsConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return edsConfigMapper.selectOneByExample(example);
    }

    @Override
    public int countByCredentialId(int credentialId) {
        Example example = new Example(EdsConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("credentialId", credentialId);
        return edsConfigMapper.selectCountByExample(example);
    }

    @Override
    public void afterPropertiesSet() {
        SupportBusinessServiceFactory.register(this);
        CredentialHolderFactory.register(this);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:EDSCONFIG:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
