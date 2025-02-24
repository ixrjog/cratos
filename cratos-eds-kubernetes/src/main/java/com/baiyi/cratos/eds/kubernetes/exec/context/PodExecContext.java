package com.baiyi.cratos.eds.kubernetes.exec.context;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer exitCode;
    @Builder.Default
    @Schema(description = "Seconds.")
    private Long maxWaitingTime = 60L;

    public String[] toExec() {
//        return CommandParser.parseCommand(command)
//                .toArray(new String[0]);
        return new String[]{"sh", "-c", command};
    }

    public String getOutMsg() {
        return this.out.toString();
    }

    public String getErrorMsg() {
        return this.error.toString();
    }

    public boolean getSuccess() {
        if (exitCode == null) {
            return false;
        }
        return 0 == exitCode;
    }

}
