package com.baiyi.cratos.eds.crt.service;

import com.baiyi.cratos.eds.crt.model.CrtSh;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/1/14 17:46
 * &#064;Version 1.0
 */
@HttpExchange(contentType = "application/json")
public interface CrtShService {

    @GetExchange("/?q={domain}&output=json")
    List<CrtSh.CertificateLog> queryCertificateLogs(@PathVariable String domain);

}
