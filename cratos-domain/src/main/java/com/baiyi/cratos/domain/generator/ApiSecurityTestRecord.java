package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.annotation.EncryptedDomain;
import com.baiyi.cratos.domain.annotation.FieldEncrypt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@EncryptedDomain
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "api_security_test_record")
public class ApiSecurityTestRecord implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "request_host")
    private String requestHost;

    @Column(name = "origin_server")
    private String originServer;

    @Column(name = "signature_algorithm")
    private String signatureAlgorithm;

    @Column(name = "private_key_type")
    private String privateKeyType;

    @FieldEncrypt
    @Column(name = "request_headers")
    private String requestHeaders;

    @FieldEncrypt
    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "response_status")
    private Integer responseStatus;

    @FieldEncrypt
    @Column(name = "response_headers")
    private String responseHeaders;

    @FieldEncrypt
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "elapsed_ms")
    private Long elapsedMs;

    private Boolean success;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;
}
