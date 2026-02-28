package com.baiyi.cratos.shell.writer;

import com.baiyi.cratos.common.table.PrettyTable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/27 10:55
 * &#064;Version 1.0
 */
public class AcmeDomainTableWriter {

    private Integer id;
    private String name;
    private String domain;
    private String domains;
    private String dnsResolverInstance;
    private String account;
    private String dcvDelegation;

    private PrettyTable table;

    public void addRow() {
        this.table.addRow(
                this.id, this.name, domain, this.domains, this.dnsResolverInstance, this.account, this.dcvDelegation);
    }

    public static AcmeDomainTableWriter newBuilder() {
        return new AcmeDomainTableWriter();
    }

    public AcmeDomainTableWriter withId(Integer id) {
        this.id = id;
        return this;
    }

    public AcmeDomainTableWriter withName(String name) {
        this.name = name;
        return this;
    }

    public AcmeDomainTableWriter withDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public AcmeDomainTableWriter withDomains(String domains) {
        this.domains = domains;
        return this;
    }

    public AcmeDomainTableWriter withDnsResolverInstance(String dnsResolverInstance) {
        this.dnsResolverInstance = dnsResolverInstance;
        return this;
    }

    public AcmeDomainTableWriter withAccount(String account) {
        this.account = account;
        return this;
    }

    public AcmeDomainTableWriter withDcvDelegation(String dcvDelegation) {
        this.dcvDelegation = dcvDelegation;
        return this;
    }

    public AcmeDomainTableWriter withTable(PrettyTable table) {
        this.table = table;
        return this;
    }

}
