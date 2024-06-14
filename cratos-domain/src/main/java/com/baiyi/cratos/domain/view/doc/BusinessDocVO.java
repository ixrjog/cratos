package com.baiyi.cratos.domain.view.doc;

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
 * @Date 2024/1/9 09:58
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class BusinessDocVO {

    public interface HasBusinessDocs extends BaseBusiness.HasBusiness {

        void setBusinessDocs(List<BusinessDoc> businessDocs);

        // List<BusinessDoc> getBusinessDocs();

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class BusinessDoc extends BaseVO implements BaseBusiness.HasBusiness, Serializable {

        @Serial
        private static final long serialVersionUID = -6331911356346269736L;

        private Integer id;

        private String businessType;

        private Integer businessId;

        private String documentType;

        private String name;

        private Integer seq;

        private String author;

        private String lastEditor;

        private String comment;

        private String content;

    }

}
