package com.baiyi.cratos.eds.harbor.service;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/15 下午6:08
 * &#064;Version 1.0
 */

import com.baiyi.cratos.eds.harbor.model.HarborProject;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

@HttpExchange(accept = "application/json")
public interface HarborProjectService {

    @GetExchange("/projects")
    List<HarborProject.Project> listProjects(@RequestHeader("authorization") String basicToken,
                                             @RequestParam Map<String, String> param);

}
