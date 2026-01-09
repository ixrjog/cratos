package com.baiyi.cratos.eds.cloudflare.service;

import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/1/9 13:48
 * &#064;Version 1.0
 */
public interface CloudFlareIPsService {

    /**
     * 获取IPv4回源列表
     * @return
     */
    @GetExchange("/ips-v4")
    List<String> getIpsV4();

    /**
     * 获取IPv6回源列表
     * @return
     */
    @GetExchange("/ips-v6")
    List<String> getIpsV6();

}
