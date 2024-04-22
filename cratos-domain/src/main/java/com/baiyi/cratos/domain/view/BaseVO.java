package com.baiyi.cratos.domain.view;

import com.baiyi.cratos.domain.constant.Global;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/5/12 10:11 上午
 * @Version 1.0
 */
@Data
public class BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7289480709427236439L;

    @Schema(description = "Create time")
    @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
    private Date createTime;

    @Schema(description = "Update time")
    @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
    private Date updateTime;

}