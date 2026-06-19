package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.Converter;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.generator.MenuTitle;
import com.baiyi.cratos.domain.view.menu.MyMenuVO;
import com.baiyi.cratos.service.MenuService;
import com.baiyi.cratos.service.MenuTitleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/8 下午2:13
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MyMenuWrapper implements Converter<Menu, MyMenuVO.MyMenu> {

    private static final String MENU_TYPE_SUB = "SUB";

    private final MenuService menuService;
    private final MenuTitleService menuTitleService;

    public List<MyMenuVO.MyMenu> wrapToTarget(List<Menu> data, String lang, List<Integer> myMenuIds) {
        if (CollectionUtils.isEmpty(data)) {
            return MyMenuVO.MyMenu.INVALID;
        }
        // 批量加载,避免逐节点 N+1 查询:
        // 1) 一次查出用户可见的全部菜单,内存里按 parentId 分组得到 children 映射
        // 2) 一次查出这些菜单的全部标题,内存里解析出 menuId -> title 映射
        Map<Integer, List<Menu>> childrenByParent = loadChildrenByParent(myMenuIds);
        Map<Integer, String> titleByMenuId = loadTitleByMenuId(myMenuIds, lang);
        return data.stream()
                .map(menu -> buildNode(menu, lang, childrenByParent, titleByMenuId))
                .sorted(Comparator.comparingInt(MyMenuVO.MyMenu::getSeq))
                .collect(Collectors.toList());
    }

    /** 递归组装单个菜单节点及其子树(全部基于已加载的内存映射,无数据库访问)。 */
    private MyMenuVO.MyMenu buildNode(Menu menu,
                                      String lang,
                                      Map<Integer, List<Menu>> childrenByParent,
                                      Map<Integer, String> titleByMenuId) {
        MyMenuVO.MyMenu vo = convert(menu);
        vo.setLang(lang);
        String title = titleByMenuId.get(vo.getMenuId());
        vo.setTitle(StringUtils.hasText(title) ? title : vo.getName());

        List<Menu> childMenus = childrenByParent.get(vo.getMenuId());
        if (!CollectionUtils.isEmpty(childMenus)) {
            List<MyMenuVO.MyMenu> children = childMenus.stream()
                    .map(child -> buildNode(child, lang, childrenByParent, titleByMenuId))
                    .sorted(Comparator.comparingInt(MyMenuVO.MyMenu::getSeq))
                    .collect(Collectors.toList());
            vo.setChildren(children);
        }
        return vo;
    }

    /** 一次查询全部可见菜单,按 parentId 分组(仅子菜单参与建树,等价于原 queryMySubMenu)。 */
    private Map<Integer, List<Menu>> loadChildrenByParent(List<Integer> myMenuIds) {
        return menuService.queryByIds(myMenuIds)
                .stream()
                .filter(menu -> MENU_TYPE_SUB.equals(menu.getMenuType()) && menu.getParentId() != null)
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    /** 一次查询全部菜单标题,解析成 menuId -> title。 */
    private Map<Integer, String> loadTitleByMenuId(List<Integer> myMenuIds, String lang) {
        return menuTitleService.queryByMenuIds(myMenuIds)
                .stream()
                .collect(Collectors.groupingBy(MenuTitle::getMenuId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> resolveTitle(entry.getValue(), lang)));
    }

    /**
     * 与原 getTitle 逻辑保持一致:优先精确匹配当前 lang;否则取 preference 标题;再否则取第一个。
     */
    private String resolveTitle(List<MenuTitle> titles, String lang) {
        return titles.stream()
                .filter(title -> Objects.equals(lang, title.getLang()))
                .findFirst()
                .map(MenuTitle::getTitle)
                .orElseGet(() -> titles.stream()
                        .filter(MenuTitle::getPreference)
                        .findFirst()
                        .orElse(titles.getFirst())
                        .getTitle());
    }

}
