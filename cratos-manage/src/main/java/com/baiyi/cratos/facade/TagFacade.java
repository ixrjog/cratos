package com.baiyi.cratos.facade;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.tag.TagParam;
import com.baiyi.cratos.domain.view.tag.TagVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 17:39
 * @Version 1.0
 */
public interface TagFacade extends HasSetValid {

    void addTag(TagParam.AddTag addTag);

    void updateTag(TagParam.UpdateTag updateTag);

    DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery);

    void deleteById(int id);

    List<TagVO.Tag> queryTagByBusinessType(BusinessParam.QueryByBusinessType getByBusinessType);

}