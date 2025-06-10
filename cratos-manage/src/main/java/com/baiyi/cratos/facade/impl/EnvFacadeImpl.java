package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.env.EnvParam;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.EnvService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.EnvWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.VERY_SHORT;

/**
 * @Author baiyi
 * @Date 2024/3/19 14:16
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EnvFacadeImpl implements EnvFacade {

    private final EnvService envService;
    private final EnvWrapper envWrapper;
    private final TagService tagService;
    private final BusinessTagService businessTagService;

    @Override
    public DataTable<EnvVO.Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery) {
        DataTable<Env> table = envService.queryEnvPage(pageQuery);
        return envWrapper.wrapToTarget(table);
    }

    @Override
    public List<Env> queryEnv(String groupValue) {
        Tag groupTag = tagService.getByTagKey(SysTagKeys.GROUP);
        if (Objects.isNull(groupTag)) {
            return List.of();
        }
        List<BusinessTag> businessTags = businessTagService.queryByBusinessTypeAndTagId(BusinessTypeEnum.ENV.name(),
                        groupTag.getId())
                .stream()
                .filter(e -> e.getTagValue()
                        .equalsIgnoreCase(groupValue))
                .toList();
        if (CollectionUtils.isEmpty(businessTags)) {
            return List.of();
        }
        return businessTags.stream()
                .map(businessTag -> envService.getById(businessTag.getBusinessId()))
                .sorted(Comparator.comparing(Env::getSeq))
                .toList();
    }

    @Override
    public List<EnvVO.Env> queryEnvByGroupValue(EnvParam.QueryEnvByGroupValue queryEnvByGroupValue) {
        return queryEnv(queryEnvByGroupValue.getGroupValue()).stream()
                .map(envWrapper::wrapToTarget)
                .sorted(Comparator.comparing(EnvVO.Env::getSeq))
                .toList();
    }

    @Override
    public void updateEnv(EnvParam.UpdateEnv updateEnv) {
        Env env = updateEnv.toTarget();
        Env dbEnv = envService.getById(env.getId());
        if (dbEnv != null) {
            env.setEnvName(dbEnv.getEnvName());
            envService.updateByPrimaryKey(env);
        }
    }

    @Override
    public void addEnv(EnvParam.AddEnv addEnv) {
        Env env = addEnv.toTarget();
        if (envService.getByUniqueKey(env) == null) {
            envService.add(env);
        }
    }

    @Override
    @CacheEvict(cacheNames = VERY_SHORT, key = "'DOMAIN:ENVMAP'")
    public Map<String, Env> getEnvMap() {
        return envService.selectAll()
                .stream()
                .collect(Collectors.toMap(Env::getEnvName, a -> a, (k1, k2) -> k1));
    }

    @Override
    public List<Env> querySorted() {
        return envService.selectAll()
                .stream()
                .filter(Env::getValid)
                .sorted(Comparator.comparing(Env::getSeq))
                .toList();
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return envService;
    }

}
