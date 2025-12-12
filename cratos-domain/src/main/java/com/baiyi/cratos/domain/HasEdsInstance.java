package com.baiyi.cratos.domain;

import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 15:53
 * &#064;Version 1.0
 */
public interface HasEdsInstance extends HasEdsInstanceId {

    void setEdsInstance(EdsInstanceVO.EdsInstance edsInstance);

}
