package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.annotation.DomainEncrypt;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import com.baiyi.cratos.mapper.CredentialMapper;
import com.baiyi.cratos.service.CredentialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:39
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CREDENTIAL)
public class CredentialServiceImpl implements CredentialService {

    private final CredentialMapper credentialMapper;

    @Override
    public DataTable<Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery) {
        Page<Certificate> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Credential> data = credentialMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

//    @Override
//    @DomainEncrypt
//    public void add(Credential credential){
//        credentialMapper.insert(credential);
//    }

    @Override
    @DomainEncrypt
    public void updateByPrimaryKey(Credential credential) {
        credentialMapper.updateByPrimaryKey(credential);
    }

    @Override
    @DomainEncrypt
    public void updateByPrimaryKeySelective(Credential credential) {
        credentialMapper.updateByPrimaryKeySelective(credential);
    }

    @Override
    // 删除用证书关联的业务标签、凭据
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG})
    public void deleteById(int id) {
        credentialMapper.deleteByPrimaryKey(id);
    }

}