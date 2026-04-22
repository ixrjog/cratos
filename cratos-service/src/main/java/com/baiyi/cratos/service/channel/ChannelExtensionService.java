package com.baiyi.cratos.service.channel;

import com.baiyi.cratos.domain.generator.ChannelExtension;
import com.baiyi.cratos.mapper.ChannelExtensionMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 18:20
 * &#064;Version 1.0
 */
public interface ChannelExtensionService extends BaseUniqueKeyService<ChannelExtension, ChannelExtensionMapper>, BaseValidService<ChannelExtension, ChannelExtensionMapper> {

    List<ChannelExtension> queryByChannelId(int channelId);

}
