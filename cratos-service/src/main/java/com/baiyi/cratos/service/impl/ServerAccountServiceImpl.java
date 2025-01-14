package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.param.http.server.ServerAccountParam;
import com.baiyi.cratos.mapper.ServerAccountMapper;
import com.baiyi.cratos.service.ServerAccountService;
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
 * @Date 2024/3/22 15:48
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.SERVER_ACCOUNT)
public class ServerAccountServiceImpl implements ServerAccountService {

    private final ServerAccountMapper serverAccountMapper;

    @Override
    public ServerAccount getByUniqueKey(@NonNull ServerAccount record) {
        Example example = new Example(ServerAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public DataTable<ServerAccount> queryServerAccountPage(ServerAccountParam.ServerAccountPageQuery pageQuery) {
        Page<ServerAccount> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ServerAccount> data = serverAccountMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:SERVERACCOUNT:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
