package com.baiyi.cratos.domain.view.tag;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/5 09:51
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class BusinessTagVO {

    public interface HasBusinessTags extends BaseBusiness.HasBusiness {
        void setBusinessTags(List<BusinessTagVO.BusinessTag> businessTags);
        List<BusinessTagVO.BusinessTag> getBusinessTags();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class BusinessTag extends BaseVO implements BaseBusiness.HasBusiness, TagVO.HasTag, Serializable {
        @Serial
        private static final long serialVersionUID = 1803833169421392342L;
        private Integer id;
        private String businessType;
        private Integer businessId;
        private Integer tagId;
        private String tagValue;
        @Schema(description = "HasTag")
        private TagVO.Tag tag;
    }

}