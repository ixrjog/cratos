package com.baiyi.cratos.domain.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/5/12 10:11 上午
 * @Version 1.0
 */
@Data
public class BaseVO {

    @Schema(description = "Create time")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "Update time")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}