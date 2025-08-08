package com.baiyi.cratos.shell.writer;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.EdsAsset;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/10 09:57
 * &#064;Version 1.0
 */
public class ComputerTableWriter {

    private Integer id;
    private EdsAsset asset;
    private String cloud;
    private String group;
    private String env;
    private String serverAccounts;
    private String permission;
    private String serverName;
    private String proxyIP;
    private boolean isShort;

    private PrettyTable table;

    public static ComputerTableWriter newBuilder() {
        return new ComputerTableWriter();
    }

    public ComputerTableWriter withId(Integer id) {
        this.id = id;
        return this;
    }

    public ComputerTableWriter withAsset(EdsAsset edsAsset) {
        this.asset = edsAsset;
        return this;
    }

    public ComputerTableWriter withCloud(String cloud) {
        this.cloud = cloud;
        return this;
    }

    public ComputerTableWriter withGroup(String group) {
        this.group = group;
        return this;
    }

    public ComputerTableWriter withEnv(String env) {
        this.env = env;
        return this;
    }

    public ComputerTableWriter withServerAccounts(String serverAccounts) {
        this.serverAccounts = serverAccounts;
        return this;
    }

    public ComputerTableWriter withPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public ComputerTableWriter withTable(PrettyTable table) {
        this.table = table;
        return this;
    }

    public ComputerTableWriter withServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public ComputerTableWriter withProxyIP(String proxyIP) {
        this.proxyIP = proxyIP;
        return this;
    }

    public ComputerTableWriter withShort(boolean isShort) {
        this.isShort = isShort;
        return this;
    }

    public void addRow() {
        final String instanceId = this.asset.getAssetId();
        final String region = asset.getRegion();
        final String type = asset.getAssetType();
        final String ip = asset.getAssetKey();
        if (isShort) {
            // {"ID", "Group", "Env", "Name", "IP", "Proxy", "Open Account", "Permission"};
            this.table.addRow(this.id, this.group, this.env, this.serverName, ip, proxyIP, this.serverAccounts,
                    this.permission);
        } else {
            // {"ID", "Cloud", "Instance ID", "Type", "Region", "Group", "Env", "Name", "IP", "Proxy", "Open Account", "Permission"};
            this.table.addRow(this.id, this.cloud, instanceId, type, region, this.group, this.env, this.serverName, ip,
                    proxyIP, this.serverAccounts, this.permission);
        }
    }

}
