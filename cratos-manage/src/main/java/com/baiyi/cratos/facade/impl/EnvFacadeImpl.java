package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.param.http.env.EnvParam;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.service.EnvService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.EnvWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    @Override
    public DataTable<EnvVO.Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery) {
        DataTable<Env> table = envService.queryEnvPage(pageQuery);
        return envWrapper.wrapToTarget(table);
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
