package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.menu.MenuParam;
import com.baiyi.cratos.domain.view.menu.MenuVO;
import com.baiyi.cratos.domain.view.menu.MyMenuVO;
import com.baiyi.cratos.domain.view.menu.RoleMenuVO;
import com.baiyi.cratos.facade.MenuFacade;
import com.baiyi.cratos.facade.MyMenuFacade;
import com.baiyi.cratos.facade.RoleMenuFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuFacade menuFacade;

    private final MyMenuFacade myMenuFacade;

    private final RoleMenuFacade roleMenuFacade;

    @Operation(summary = "Get nav menu")
    @GetMapping(value = "/nav/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<MenuVO.NavMenu> getNavMenu() {
        return new HttpResult<>(menuFacade.getNavMenu());
    }

    @Operation(summary = "Get menu by id")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<MenuVO.Menu> getMenuById(@RequestParam @Valid int menuId) {
        return new HttpResult<>(menuFacade.getMenuById(menuId));
    }

    @Operation(summary = "Add menu")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addMenu(@RequestBody @Valid MenuParam.AddMenu addMenu) {
        menuFacade.addMenu(addMenu);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update menu")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateMenu(@RequestBody @Valid MenuParam.UpdateMenu updateMenu) {
        menuFacade.updateMenu(updateMenu);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete menu by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteMenuById(@RequestParam int menuId) {
        menuFacade.deleteMenuById(menuId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query my menu")
    @PostMapping(value = "/my/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MyMenuVO.MyMenu>> queryMyMenu(@RequestBody @Valid MenuParam.QueryMyMenu queryMyMenu) {
        return new HttpResult<>(myMenuFacade.queryMyMenu(queryMyMenu));
    }

    @Operation(summary = "Query user menu (TEST)")
    @PostMapping(value = "/user/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MyMenuVO.MyMenu>> queryUserMenu(@RequestBody @Valid MenuParam.QueryUserMenu queryUserMenu) {
        return new HttpResult<>(myMenuFacade.queryUserMenu(queryUserMenu.getUsername(), queryUserMenu.getLang()));
    }

    @Operation(summary = "Get role menu by id")
    @GetMapping(value = "/role/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<RoleMenuVO.RoleMenu> getRoleMenuByRoleId(@RequestParam int roleId, @Valid String lang) {
        return new HttpResult<>(roleMenuFacade.getRoleMenu(roleId, lang));
    }

}
