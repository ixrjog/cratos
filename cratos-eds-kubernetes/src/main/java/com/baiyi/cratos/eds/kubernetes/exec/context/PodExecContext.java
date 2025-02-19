package com.baiyi.cratos.eds.kubernetes.exec.context;

import com.baiyi.cratos.common.util.CommandParser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 09:59
 * &#064;Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PodExecContext {

    @Builder.Default
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    @Builder.Default
    private ByteArrayOutputStream error = new ByteArrayOutputStream();
    private String command;
    @Builder.Default
    @Schema(description = "Seconds.")
    private Long maxWaitingTime = 60L;

    public String[] toExec() {
        return CommandParser.parseCommand(command)
                .toArray(new String[0]);
    }

    public String getOutMsg() {
        return this.out.toString();
    }

    public String getErrorMsg() {
        return this.error.toString();
    }

    public boolean getSuccess() {
        return !StringUtils.hasText(getErrorMsg());
    }

}
