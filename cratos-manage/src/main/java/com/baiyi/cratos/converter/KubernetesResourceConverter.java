package com.baiyi.cratos.converter;

import com.baiyi.cratos.domain.generator.ApplicationResource;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/3 11:18
 * &#064;Version 1.0
 */
public interface KubernetesResourceConverter<Resource> {

    List<Resource> toResourceVO(List<ApplicationResource> resources);

}
