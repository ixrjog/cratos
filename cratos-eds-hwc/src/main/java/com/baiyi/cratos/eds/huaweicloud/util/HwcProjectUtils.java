package com.baiyi.cratos.eds.huaweicloud.util;

import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/18 14:43
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class HwcProjectUtils {

    private static final String NO_PROJECT_ID = null;

    public static String findProjectId(String regionId, EdsHwcConfigModel.Hwc huaweicloud) {
        if (!CollectionUtils.isEmpty(huaweicloud.getProjects())) {
            Optional<EdsHwcConfigModel.Project> optionalProject = huaweicloud.getProjects()
                    .stream()
                    .filter(e -> regionId.equals(e.getName()))
                    .findFirst();
            if (optionalProject.isPresent()) {
                return optionalProject.get()
                        .getId();
            }
        }
        return NO_PROJECT_ID;
    }

}
