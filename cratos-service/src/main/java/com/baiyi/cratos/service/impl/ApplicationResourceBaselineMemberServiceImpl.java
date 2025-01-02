package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import com.baiyi.cratos.mapper.ApplicationResourceBaselineMemberMapper;
import com.baiyi.cratos.service.ApplicationResourceBaselineMemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 10:05
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApplicationResourceBaselineMemberServiceImpl implements ApplicationResourceBaselineMemberService {

    private final ApplicationResourceBaselineMemberMapper applicationResourceBaselineMemberMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:APPLICATION_RESOURCE_BASELINE_MEMBER:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public ApplicationResourceBaselineMember getByUniqueKey(@NonNull ApplicationResourceBaselineMember record) {
        Example example = new Example(ApplicationResourceBaselineMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baselineId", record.getBaselineId())
                .andEqualTo("baselineType", record.getBaselineType());
        return applicationResourceBaselineMemberMapper.selectOneByExample(example);
    }

    @Override
    public List<ApplicationResourceBaselineMember> queryByBaselineId(int baselineId) {
        Example example = new Example(ApplicationResourceBaselineMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baselineId", baselineId);
        return applicationResourceBaselineMemberMapper.selectByExample(example);
    }

    @Override
    public void deleteByBaselineId(int baselineId) {
        List<ApplicationResourceBaselineMember> members = this.queryByBaselineId(baselineId);
        if (!CollectionUtils.isEmpty(members)) {
            for (ApplicationResourceBaselineMember member : members) {
                this.deleteById(member.getId());
            }
        }
    }

    @Override
    public boolean hasNonStandardBaselineMember(int baselineId) {
        Example example = new Example(ApplicationResourceBaselineMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baselineId", baselineId)
                .andEqualTo("standard", false);
        return applicationResourceBaselineMemberMapper.selectCountByExample(example) > 0;
    }

}
