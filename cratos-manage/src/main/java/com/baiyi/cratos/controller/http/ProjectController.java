package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import com.baiyi.cratos.domain.view.project.ProjectVO;
import com.baiyi.cratos.facade.project.ProjectFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
@Tag(name = "Project Config")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectFacade projectFacade;

    @Operation(summary = "Pagination query project")
    @PostMapping(value = "/config/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ProjectVO.Project>> queryProjectPage(@RequestBody @Valid ProjectParam.ProjectPageQuery pageQuery) {
        return HttpResult.of(projectFacade.queryProjectPage(pageQuery));
    }

    @Operation(summary = "Add project")
    @PostMapping(value = "/config/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addProject(@RequestBody @Valid ProjectParam.AddProject addProject) {
        projectFacade.addProject(addProject);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update project")
    @PutMapping(value = "/config/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateProject(@RequestBody @Valid ProjectParam.UpdateProject updateProject) {
        projectFacade.updateProject(updateProject);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete project by id")
    @DeleteMapping(value = "/config/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteProjectById(@RequestParam int id) {
        projectFacade.deleteProjectById(id);
        return HttpResult.SUCCESS;
    }

}
