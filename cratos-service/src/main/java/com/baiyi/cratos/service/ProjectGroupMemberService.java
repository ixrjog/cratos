package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.ProjectGroupMember;
import com.baiyi.cratos.mapper.ProjectGroupMemberMapper;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

public interface ProjectGroupMemberService extends BaseValidService<ProjectGroupMember, ProjectGroupMemberMapper> {

    List<ProjectGroupMember> queryByGroupId(int groupId);
}
