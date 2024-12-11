package com.baiyi.cratos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/10 14:47
 * &#064;Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCommited implements Commited {

    private String name;
    @Builder.Default
    private String commitId = UUID.randomUUID().toString();
    private String commitContent;
    private String commitMessage;

}
