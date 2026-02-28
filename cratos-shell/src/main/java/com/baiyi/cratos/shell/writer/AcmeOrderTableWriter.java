package com.baiyi.cratos.shell.writer;

import com.baiyi.cratos.common.table.PrettyTable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/27 14:23
 * &#064;Version 1.0
 */
public class AcmeOrderTableWriter {

    private Integer id;
    private String domain;
    private String domains;
    private String orderUrl;
    private String status;
    private String createTime;
    private String expires;

    private PrettyTable table;

    public AcmeOrderTableWriter withTable(PrettyTable table) {
        this.table = table;
        return this;
    }

    public AcmeOrderTableWriter withId(Integer id) {
        this.id = id;
        return this;
    }

    public AcmeOrderTableWriter withDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public AcmeOrderTableWriter withDomains(String domains) {
        this.domains = domains;
        return this;
    }

    public AcmeOrderTableWriter withOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
        return this;
    }

    public AcmeOrderTableWriter withStatus(String status) {
        this.status = status;
        return this;
    }

    public AcmeOrderTableWriter withCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public AcmeOrderTableWriter withExpires(String expires) {
        this.expires = expires;
        return this;
    }

    public void addRow() {
        this.table.addRow(this.id, domain, this.domains, this.orderUrl, this.status, this.createTime, this.expires);
    }

    public static AcmeOrderTableWriter newBuilder() {
        return new AcmeOrderTableWriter();
    }

}
