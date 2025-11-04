package com.baiyi.cratos.eds.harbor.service;


import com.baiyi.cratos.eds.harbor.model.HarborProject;
import com.baiyi.cratos.eds.harbor.model.HarborRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/15 下午6:08
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface HarborService {

    @GetExchange("/projects")
    List<HarborProject.Project> listProjects(@RequestParam Map<String, String> param);

    @GetExchange("/projects/{projectName}/repositories")
    List<HarborRepository.Repository> listRepositories(@PathVariable String projectName,
                                                       @RequestParam Map<String, String> param);

}
