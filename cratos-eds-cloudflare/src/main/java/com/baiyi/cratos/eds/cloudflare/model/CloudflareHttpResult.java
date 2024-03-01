package com.baiyi.cratos.eds.cloudflare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/1 16:44
 * @Version 1.0
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloudflareHttpResult<T> {

    private T result;

    @Schema(description = "是否成功")
    private boolean success;

    private List<Message> messages;

    private int code;

    @Data
    public static class Message {

      private int code;
      private String message;
      private String type;

    }

}