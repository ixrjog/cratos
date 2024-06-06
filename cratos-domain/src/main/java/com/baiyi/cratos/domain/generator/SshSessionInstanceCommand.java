package com.baiyi.cratos.domain.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：ssh_session_instance_command
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ssh_session_instance_command")
public class SshSessionInstanceCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例表ID
     */
    @Column(name = "ssh_session_instance_id")
    private Integer sshSessionInstanceId;

    /**
     * 提示符
     */
    private String prompt;

    /**
     * 是否格式化
     */
    @Column(name = "is_formatted")
    private Boolean isFormatted;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 输入
     */
    private String input;

    /**
     * 输入格式化
     */
    @Column(name = "input_formatted")
    private String inputFormatted;

    /**
     * 输出
     */
    private String output;
}