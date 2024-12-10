package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：business_commit
 * 表注释：记录
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_commit")
public class BusinessCommit implements HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

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

    private String name;

    @Column(name = "commit_id")
    private String commitId;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "commit_message")
    private String commitMessage;

    @Column(name = "commit_content")
    private String commitContent;
}