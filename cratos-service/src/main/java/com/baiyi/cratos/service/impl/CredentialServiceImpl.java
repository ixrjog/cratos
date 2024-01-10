package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
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
public class CredentialServiceImpl implements CredentialService {

    private final CredentialMapper credentialMapper;

    @Override
    public CredentialMapper getMapper() {
        return credentialMapper;
    }

    @Override
    public DataTable<Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery) {
        Page<Certificate> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Credential> data = credentialMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}