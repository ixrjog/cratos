package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.business.PermissionBusinessServiceFactory;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.param.http.server.ServerAccountParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.mapper.ServerAccountMapper;
import com.baiyi.cratos.query.ServerAccountQuery;
import com.baiyi.cratos.service.ServerAccountService;
import com.baiyi.cratos.service.factory.SupportBusinessServiceFactory;
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

    private static final String DISPLAY_NAME_TPL = "{}[sudo={}:u={}]";

    @Override
    public ServerAccount getByUniqueKey(@NonNull ServerAccount record) {
        Example example = new Example(ServerAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public ServerAccount getByName(String name) {
        return getByUniqueKey(ServerAccount.builder()
                .name(name)
                .build());
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

    @Override
    public DataTable<PermissionBusinessVO.PermissionBusiness> queryUserPermissionBusinessPage(
            UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery) {
        ServerAccountParam.ServerAccountPageQuery param = ServerAccountParam.ServerAccountPageQuery.builder()
                .queryName(pageQuery.getQueryName())
                .page(pageQuery.getPage())
                .length(pageQuery.getLength())
                .build();
        DataTable<ServerAccount> dataTable = this.queryServerAccountPage(param);
        return new DataTable<>(dataTable.getData()
                .stream()
                .map(this::toPermissionBusiness)
                .toList(), dataTable.getTotalNum());
    }

    @Override
    public List<ServerAccount> queryUserPermissionServerAccounts(
            ServerAccountQuery.QueryUserPermissionServerAccountParam param) {
        return serverAccountMapper.queryUserPermissionServerAccounts(param);
    }

    @Override
    public PermissionBusinessVO.PermissionBusiness toPermissionBusiness(ServerAccount recode) {
        String displayName = StringFormatter.arrayFormat(DISPLAY_NAME_TPL, recode.getName(), recode.getSudo(),
                recode.getUsername());
        return PermissionBusinessVO.PermissionBusiness.builder()
                .name(recode.getName())
                .displayName(displayName)
                .businessType(getBusinessType())
                .businessId(recode.getId())
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        SupportBusinessServiceFactory.register(this);
        PermissionBusinessServiceFactory.register(this);
    }

}
