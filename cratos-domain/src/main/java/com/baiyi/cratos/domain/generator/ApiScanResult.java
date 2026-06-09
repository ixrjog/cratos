package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api_scan_result")
public class ApiScanResult implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "deployment_name")
    private String deploymentName;

    @Column(name = "pod_ip")
    private String podIP;

    private String path;

    private String method;

    @Column(name = "status_code")
    private Integer statusCode;

    private String resp;

    @Column(name = "resp_size")
    private Integer respSize;

    @Column(name = "group_name")
    private String groupName;

    private String severity;

    private String category;

    @Column(name = "scan_batch")
    private String scanBatch;

    private Boolean valid;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}
