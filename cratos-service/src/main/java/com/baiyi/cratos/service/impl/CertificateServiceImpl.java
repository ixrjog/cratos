package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.certificate.CertificateParam;
import com.baiyi.cratos.mapper.CertificateMapper;
import com.baiyi.cratos.service.CertificateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:11
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CERTIFICATE)
public class CertificateServiceImpl implements CertificateService {

    private final CertificateMapper certificateMapper;

    @Override
    public CertificateMapper getMapper() {
        return certificateMapper;
    }

    @Override
    public void deleteByCertificateId(String certificateId) {
        Example example = new Example(Certificate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("certificateId", certificateId);
        certificateMapper.deleteByExample(example);
    }

    @Override
    public DataTable<Certificate> queryCertificatePage(CertificateParam.CertificatePageQuery pageQuery) {
        Page<Certificate> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Certificate> data = certificateMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    // 删除用证书关联的业务标签、凭据
    @DeleteBoundBusiness(businessId = "#id", types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_CREDENTIAL})
    public void deleteById(int id) {
        certificateMapper.deleteByPrimaryKey(id);
    }

}