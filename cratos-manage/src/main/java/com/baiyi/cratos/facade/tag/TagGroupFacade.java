package com.baiyi.cratos.facade.tag;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.tag.TagGroupParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/2 14:53
 * &#064;Version 1.0
 */
public interface TagGroupFacade {

    OptionsVO.Options getGroupOptions(TagGroupParam.GetGroupOptions getGroupOptions);

    OptionsVO.Options getMyGroupOptions(TagGroupParam.GetMyGroupOptions getMyGroupOptions);

    DataTable<EdsAssetVO.Asset> queryGroupAssetPage(TagGroupParam.GroupAssetPageQuery pageQuery);

    DataTable<EdsAssetVO.Asset> queryMyGroupAssetPage(TagGroupParam.MyGroupAssetPageQuery pageQuery);

}
