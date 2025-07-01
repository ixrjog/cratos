package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.UserFavorite;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.facade.UserFavoriteFacade;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.UserFavoriteService;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.SupportBusinessService;
import com.baiyi.cratos.service.factory.SupportBusinessServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/1 11:22
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserFavoriteFacadeImpl implements UserFavoriteFacade {

    private final UserFavoriteService userFavoriteService;
    private final ApplicationService applicationService;

    @Override
    public List<ApplicationVO.Application> getMyFavoriteApplication() {
        List<Integer> businessIds = userFavoriteService.queryUserFavoriteBusinessIds(SessionUtils.getUsername(),
                BusinessTypeEnum.APPLICATION.name());
        if (CollectionUtils.isEmpty(businessIds)) {
            return List.of();
        }
        return businessIds.stream()
                .map(applicationService::getById)
                .filter(Objects::nonNull)
                .map(app -> BeanCopierUtil.copyProperties(app, ApplicationVO.Application.class))
                .toList();
    }

    /**
     * 收藏
     *
     * @param businessType
     * @param businessId
     */
    @Override
    public void favorite(String businessType, int businessId) {
        boolean isExist = isUserFavorited(SessionUtils.getUsername(), businessType, businessId);
        if (isExist) {
            UserFavorite userFavorite = userFavoriteService.getByUniqueKey(UserFavorite.builder()
                    .username(SessionUtils.getUsername())
                    .businessType(businessType)
                    .businessId(businessId)
                    .build());
            userFavorite.setSeq(userFavorite.getSeq() + 1);
            userFavoriteService.updateByPrimaryKey(userFavorite);
        } else {
            // add new favorite
            UserFavorite userFavorite = UserFavorite.builder()
                    .username(SessionUtils.getUsername())
                    .businessType(businessType)
                    .businessId(businessId)
                    .seq(0)
                    .build();
            SupportBusinessService supportBusinessService = SupportBusinessServiceFactory.getService(businessType);
            if (supportBusinessService == null) {
                throw new IllegalArgumentException("Unsupported business type: " + businessType);
            } else {
                //noinspection rawtypes
                if (supportBusinessService instanceof BaseService baseService) {
                    Object businessObject = baseService.getById(businessId);
                    if (businessObject instanceof Application application) {
                        userFavorite.setName(application.getName());
                    }
                }
            }
            userFavoriteService.add(userFavorite);
        }
    }

    @Override
    public boolean isUserFavorited(String username, String businessType, int businessId) {
        UserFavorite uk = UserFavorite.builder()
                .username(username)
                .businessType(businessType)
                .businessId(businessId)
                .build();
        return Objects.nonNull(userFavoriteService.getByUniqueKey(uk));
    }

    /**
     * 取消收藏
     *
     * @param businessType
     * @param businessId
     */
    @Override
    public void unfavorite(String businessType, int businessId) {
        UserFavorite uk = UserFavorite.builder()
                .username(SessionUtils.getUsername())
                .businessType(businessType)
                .businessId(businessId)
                .build();
        UserFavorite userFavorite = userFavoriteService.getByUniqueKey(uk);
        if (Objects.nonNull(userFavorite)) {
            userFavoriteService.deleteById(userFavorite.getId());
        }
    }

}
