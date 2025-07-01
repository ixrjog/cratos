package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.view.application.ApplicationVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/1 11:22
 * &#064;Version 1.0
 */
public interface UserFavoriteFacade {

    List<ApplicationVO.Application> getMyFavoriteApplication();

    boolean isUserFavorited(String username, String businessType, int businessId);

    void unfavorite(String businessType, int businessId);

    void favorite(String businessType, int businessId);

}
