package com.baiyi.cratos.service.kubernetes.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.mapper.KubernetesResourceTemplateMapper;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateService;
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
 * &#064;Date  2024/11/1 10:56
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE)
public class KubernetesResourceTemplateServiceImpl implements KubernetesResourceTemplateService {

    private final KubernetesResourceTemplateMapper kubernetesResourceTemplateMapper;

    @Override
    public DataTable<KubernetesResourceTemplate> queryTemplatePage(
            KubernetesResourceTemplateParam.TemplatePageQueryParam param) {
        Page<KubernetesResourceTemplate> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<KubernetesResourceTemplate> data = kubernetesResourceTemplateMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public KubernetesResourceTemplate getByUniqueKey(@NonNull KubernetesResourceTemplate record) {
        Example example = new Example(KubernetesResourceTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateKey", record.getTemplateKey());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public KubernetesResourceTemplate getByTemplateKey(@NonNull String templateKey) {
        KubernetesResourceTemplate record = KubernetesResourceTemplate.builder()
                .templateKey(templateKey)
                .build();
        return getByUniqueKey(record);
    }

    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        KubernetesResourceTemplateService.super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:KUBERNETESRESOURCETEMPLATE:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
