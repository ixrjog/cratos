package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.MenuTitle;
import com.baiyi.cratos.mapper.MenuTitleMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:07
 * @Version 1.0
 */
public interface MenuTitleService extends BaseUniqueKeyService<MenuTitle>, BaseService<MenuTitle, MenuTitleMapper> {

    List<MenuTitle> queryByMenuId(int menuId);

}
