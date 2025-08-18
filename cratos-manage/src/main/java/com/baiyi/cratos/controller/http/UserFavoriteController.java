package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.param.http.user.UserFavoriteParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.domain.view.tag.TagGroupVO;
import com.baiyi.cratos.facade.UserFavoriteFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/1 11:15
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/user/favorite")
@Tag(name = "User Favorite")
@RequiredArgsConstructor
public class UserFavoriteController {

    private final UserFavoriteFacade userFavoriteFacade;

    @Operation(summary = "Query favorite applications")
    @GetMapping(value = "/my/application/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ApplicationVO.Application>> getMyFavoriteApplication() {
        return HttpResult.ofBody(userFavoriteFacade.getMyFavoriteApplication());
    }

    @Operation(summary = "Add application to my favorites")
    @PostMapping(value = "/application/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplicationFavorite(
            @RequestBody @Valid UserFavoriteParam.AddUserFavorite addUserFavorite) {
        userFavoriteFacade.favorite(BusinessTypeEnum.APPLICATION.name(), addUserFavorite.getBusinessId());
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Remove application from my favorites")
    @DeleteMapping(value = "/application/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> removeApplicationFavorite(
            @RequestBody @Valid UserFavoriteParam.RemoveUserFavorite removeUserFavorite) {
        userFavoriteFacade.unfavorite(BusinessTypeEnum.APPLICATION.name(), removeUserFavorite.getBusinessId());
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query favorite groups")
    @GetMapping(value = "/my/group/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TagGroupVO.TagGroup>> getMyFavoriteGroup() {
        return HttpResult.ofBody(userFavoriteFacade.getMyFavoriteGroup());
    }

    @Operation(summary = "Add application to my favorites")
    @PostMapping(value = "/group/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGroupFavorite(@RequestBody @Valid UserFavoriteParam.AddUserFavorite addUserFavorite) {
        userFavoriteFacade.favorite(addUserFavorite.getName(), BusinessTypeEnum.TAG_GROUP.name(),
                addUserFavorite.getName()
                        .hashCode());
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Remove group from my favorites")
    @DeleteMapping(value = "/group/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> removeGroupFavorite(
            @RequestBody @Valid UserFavoriteParam.RemoveUserFavorite removeUserFavorite) {
        userFavoriteFacade.unfavorite(BusinessTypeEnum.TAG_GROUP.name(), removeUserFavorite.getName()
                .hashCode());
        return HttpResult.SUCCESS;
    }

}
