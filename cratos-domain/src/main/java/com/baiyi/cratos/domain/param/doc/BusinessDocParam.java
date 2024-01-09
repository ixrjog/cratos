package com.baiyi.cratos.domain.param.doc;

import com.baiyi.cratos.domain.generator.BusinessDocument;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/1/9 16:37
 * @Version 1.0
 */
public class BusinessDocParam {

    @Data
    @Schema
    public static class AddBusinessDoc implements IToTarget<BusinessDocument> {

        private Integer id;

        private String businessType;

        private Integer businessId;

        private String documentType;

        private String name;

        private Integer seq;

        private String comment;

        private Date createTime;

        private Date updateTime;

        private String content;

    }

    @Data
    @Schema
    public static class UpdateBusinessDoc implements IToTarget<BusinessDocument> {

        private Integer id;

        private String businessType;

        private Integer businessId;

        private String documentType;

        private String name;

        private Integer seq;

        private String comment;

        private Date createTime;

        private Date updateTime;

        private String content;

    }

}
