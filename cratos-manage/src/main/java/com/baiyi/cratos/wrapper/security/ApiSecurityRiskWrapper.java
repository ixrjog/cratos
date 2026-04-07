package com.baiyi.cratos.wrapper.security;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.annotation.Sensitive;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ApiSecurityRisk;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.security.ApiSecurityRiskVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/7 10:53
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.API_SECURITY_RISK)
public class ApiSecurityRiskWrapper extends BaseDataTableConverter<ApiSecurityRiskVO.Risk, ApiSecurityRisk> implements BaseWrapper<ApiSecurityRiskVO.Risk> {

    private final UserService userService;

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG})
    @Sensitive
    public void wrap(ApiSecurityRiskVO.Risk vo) {
        if (StringUtils.hasText(vo.getAnalyst())) {
            User analystUser = userService.getByUsername(vo.getAnalyst());
            if (analystUser != null) {
                vo.setAnalystUser(BeanCopierUtils.copyProperties(analystUser, UserVO.User.class));
            }
        }
        if (StringUtils.hasText(vo.getSecurityOfficer())) {
            User securityOfficerUser = userService.getByUsername(vo.getAnalyst());
            if (securityOfficerUser != null) {
                vo.setSecurityOfficerUser(BeanCopierUtils.copyProperties(securityOfficerUser, UserVO.User.class));
            }
        }
        if (StringUtils.hasText(vo.getContactPerson())) {
            User contactPersonUser = userService.getByUsername(vo.getAnalyst());
            if (contactPersonUser != null) {
                vo.setContactPersonUser(BeanCopierUtils.copyProperties(contactPersonUser, UserVO.User.class));
            }
        }
    }

}