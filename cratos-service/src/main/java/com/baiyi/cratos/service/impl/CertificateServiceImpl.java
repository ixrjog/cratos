package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
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

}