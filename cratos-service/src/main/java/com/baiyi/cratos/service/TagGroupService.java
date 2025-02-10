package com.baiyi.cratos.service;

import com.baiyi.cratos.business.PermissionBusinessService;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.Tag;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 10:35
 * &#064;Version 1.0
 */
public interface TagGroupService extends PermissionBusinessService<BusinessTag>, InitializingBean {

    Tag getTagGroup();

}
