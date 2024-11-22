package com.baiyi.cratos.domain.param.http.doc;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.annotation.ApiModelPropertyPro;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessDocument;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/9 16:37
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class BusinessDocParam {

    @Data
    @Schema
    public static class AddBusinessDoc implements IToTarget<BusinessDocument>, HasSessionUser {
        private Integer id;
        @Schema(description = "Obtain from the enumeration class 'BusinessTypeEnum'")
        @ApiModelPropertyPro(value = BusinessTypeEnum.class)
        private String businessType;
        private Integer businessId;
        private String documentType;
        private String name;
        private Integer seq;
        private String comment;
        private String content;
        private String author;
        @Override
        public void setSessionUser(String username) {
            this.author = username;
        }
    }

    @Data
    @Schema
    public static class UpdateBusinessDoc implements IToTarget<BusinessDocument>, HasSessionUser {
        private Integer id;
        @Schema(description = "Obtain from the enumeration class 'BusinessTypeEnum'")
        @ApiModelPropertyPro(value = BusinessTypeEnum.class)
        private String businessType;
        private Integer businessId;
        private String documentType;
        private String name;
        private Integer seq;
        private String comment;
        private String content;
        private String lastEditor;
        @Override
        public void setSessionUser(String username) {
            this.lastEditor = username;
        }
    }

}
