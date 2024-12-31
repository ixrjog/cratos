package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import com.baiyi.cratos.mapper.ApplicationResourceBaselineMemberMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 10:04
 * &#064;Version 1.0
 */
public interface ApplicationResourceBaselineMemberService extends BaseUniqueKeyService<ApplicationResourceBaselineMember, ApplicationResourceBaselineMemberMapper> {

    List<ApplicationResourceBaselineMember> queryByBaselineId(int baselineId);

}
