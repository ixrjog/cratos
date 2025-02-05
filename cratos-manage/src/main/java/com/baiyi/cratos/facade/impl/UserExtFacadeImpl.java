package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.UserExtFacade;
import com.baiyi.cratos.facade.UserFacade;
import com.baiyi.cratos.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 15:51
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserExtFacadeImpl implements UserExtFacade {

    private final UserFacade userFacade;
    private final TagService tagService;
    private static final String EXTERNAL_USER_TAG = "ExternalUser";

    @SuppressWarnings("unchecked")
    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.USER)
    public DataTable<UserVO.User> queryExtUserPage(UserExtParam.UserExtPageQuery pageQuery) {
        Tag extUserTag = tagService.getByTagKey(EXTERNAL_USER_TAG);
        if (Objects.isNull(extUserTag)) {
            return DataTable.NO_DATA;
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(extUserTag.getId())
                .build();
        UserParam.UserPageQuery userPageQuery = UserParam.UserPageQuery.builder()
                .queryByTag(queryByTag)
                .page(pageQuery.getPage())
                .length(pageQuery.getLength())
                .queryName(pageQuery.getQueryName())
                .build();
        return userFacade.queryUserPage(userPageQuery);
    }

}
