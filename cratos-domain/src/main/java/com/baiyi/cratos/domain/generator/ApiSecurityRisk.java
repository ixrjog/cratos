package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：api_security_risk
 * 表注释：API安全风险管理
 */
@Data
@Table(name = "api_security_risk")
public class ApiSecurityRisk implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -3603066100105372781L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String riskNo;

    /**
     * 风险描述
     */
    @Column(name = "risk_description")
    private String riskDescription;

    /**
     * 接口地址
     */
    @Column(name = "api_endpoint")
    private String apiEndpoint;

    /**
     * 文档地址
     */
    @Column(name = "doc_url")
    private String docUrl;

    /**
     * 风险等级: CRITICAL/HIGH/MEDIUM/LOW
     */
    @Column(name = "risk_level")
    private String riskLevel;

    /**
     * 分析人(关联用户)
     */
    private String analyst;

    /**
     * 对接人
     */
    @Column(name = "contact_person")
    private String contactPerson;

    private String securityOfficer;

    /**
     * 处理跟进群
     */
    @Column(name = "follow_up_group")
    private String followUpGroup;

    /**
     * 进度
     */
    private String progress;

    /**
     * 发现时间
     */
    @Column(name = "discovered_time")
    private Date discoveredTime;

    @Column(name = "created_by")
    private String createdBy;

    /**
     * 预计完结时间
     */
    @Column(name = "expected_time")
    private Date expectedTime;

    /**
     * 是否有效
     */
    private Boolean valid;

    /**
     * 是否完成
     */
    private Boolean completed;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 备注
     */
    private String comment;

    /**
     * 分析说明
     */
    @Column(name = "analysis_desc")
    private String analysisDesc;
}