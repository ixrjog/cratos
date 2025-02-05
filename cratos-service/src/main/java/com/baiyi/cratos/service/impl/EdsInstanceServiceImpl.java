package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.mapper.EdsInstanceMapper;
import com.baiyi.cratos.service.EdsInstanceService;
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
 * @Date 2024/2/5 16:53
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
public class EdsInstanceServiceImpl implements EdsInstanceService {

    private final EdsInstanceMapper edsInstanceMapper;

    @Override
    public DataTable<EdsInstance> queryEdsInstancePage(EdsInstanceParam.InstancePageQueryParam param) {
        Page<EdsInstance> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<EdsInstance> data = edsInstanceMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public EdsInstance getByUniqueKey(@NonNull EdsInstance record) {
        Example example = new Example(EdsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceName", record.getInstanceName());
        return edsInstanceMapper.selectOneByExample(example);
    }

    @Override
    public EdsInstance getByName(String instanceName) {
        EdsInstance uniqueKey = EdsInstance.builder()
                .instanceName(instanceName)
                .build();
        return getByUniqueKey(uniqueKey);
    }

    @Override
    public int selectCountByConfigId(Integer configId) {
        Example example = new Example(EdsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("configId", configId);
        return edsInstanceMapper.selectCountByExample(example);
    }

    @Override
    public List<EdsInstance> queryValidEdsInstanceByType(String edsType) {
        Example example = new Example(EdsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("edsType", edsType)
                .andEqualTo("valid", true);
        return edsInstanceMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:EDSINSTANCE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        EdsInstanceService.super.deleteById(id);
    }

}
