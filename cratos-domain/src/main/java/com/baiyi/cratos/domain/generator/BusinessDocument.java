package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "business_document")
public class BusinessDocument implements BaseBusiness.HasBusiness, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 5931340313604570353L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 文档类型
     */
    @Column(name = "document_type")
    private String documentType;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer seq;

    private String author;

    @Column(name = "last_editor")
    private String lastEditor;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 文档内容
     */
    private String content;
}