package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.tag.TagParam;
import com.baiyi.cratos.domain.view.tag.TagVO;

/**
 * @Author baiyi
 * @Date 2024/1/2 17:39
 * @Version 1.0
 */
public interface TagFacade {

    void addTag(TagParam.AddTag addTag);

    void updateTag(TagParam.UpdateTag updateTag);

    DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery);

    void deleteById(int tagId);

    void setTagValidById(int tagId);

}