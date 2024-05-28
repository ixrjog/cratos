package com.baiyi.cratos.ssh.core.model;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/28 上午11:42
 * &#064;Version 1.0
 */
@Data
@Builder
public class PodAssetModel {

    private Pod pod;

    private String instanceId;

    public String acqNamespace(){
        return Optional.ofNullable(pod)
                .map(Pod::getMetadata)
                .map(ObjectMeta::getNamespace)
                .orElse("");
    }

    public String acqName(){
        return Optional.ofNullable(pod)
                .map(Pod::getMetadata)
                .map(ObjectMeta::getName)
                .orElse("");
    }

}
