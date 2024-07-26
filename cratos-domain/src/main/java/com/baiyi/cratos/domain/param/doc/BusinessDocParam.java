package com.baiyi.cratos.domain.param.doc;

import com.baiyi.cratos.domain.ISetSessionUser;
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
    public static class AddBusinessDoc implements IToTarget<BusinessDocument>, ISetSessionUser {
        private Integer id;
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
    public static class UpdateBusinessDoc implements IToTarget<BusinessDocument>, ISetSessionUser {
        private Integer id;
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
