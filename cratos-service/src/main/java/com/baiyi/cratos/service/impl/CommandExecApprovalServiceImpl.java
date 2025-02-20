package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.common.enums.CommandExecApprovalTypeEnum;
import com.baiyi.cratos.domain.generator.CommandExecApproval;
import com.baiyi.cratos.mapper.CommandExecApprovalMapper;
import com.baiyi.cratos.service.CommandExecApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/17 18:19
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandExecApprovalServiceImpl implements CommandExecApprovalService {

    private final CommandExecApprovalMapper commandExecApprovalMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:COMMAND_EXEC:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public CommandExecApproval queryUnapprovedRecord(int commandExecId, String username) {
        Example example = new Example(CommandExecApproval.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("commandExecId", commandExecId)
                .andEqualTo("username", username)
                .andEqualTo("approvalType", CommandExecApprovalTypeEnum.APPROVER.name())
                .andEqualTo("approvalCompleted", false);
        return commandExecApprovalMapper.selectOneByExample(example);
    }

    @Override
    public boolean hasUnfinishedApprovals(int commandExecId) {
        Example example = new Example(CommandExecApproval.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("commandExecId", commandExecId)
                .andEqualTo("approvalType", CommandExecApprovalTypeEnum.APPROVER.name())
                .andEqualTo("approvalCompleted", false);
        return commandExecApprovalMapper.selectCountByExample(example) > 0;
    }

    @Override
    public List<CommandExecApproval> queryApprovals(int commandExecId, String approvalType) {
        Example example = new Example(CommandExecApproval.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("commandExecId", commandExecId)
                .andEqualTo("approvalType", approvalType);
        return commandExecApprovalMapper.selectByExample(example);
    }

    @Override
    public List<CommandExecApproval> queryApprovals(int commandExecId) {
        Example example = new Example(CommandExecApproval.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("commandExecId", commandExecId);
        return commandExecApprovalMapper.selectByExample(example);
    }

}
