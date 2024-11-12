package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceParam;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceFacade;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateFacade;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateMemberFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/1 13:54
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/kubernetes/resource")
@Tag(name = "Kubernetes Resource")
@RequiredArgsConstructor
public class KubernetesResourceController {

    private final KubernetesResourceTemplateFacade templateFacade;

    private final KubernetesResourceTemplateMemberFacade templateMemberFacade;

    private final KubernetesResourceFacade resourceFacade;

    @Operation(summary = "Query kubernetes resource options")
    @GetMapping(value = "/kind/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getResourceKindOptions() {
        return new HttpResult<>(KubernetesResourceKindEnum.toOptions());
    }

    @Operation(summary = "Pagination query kubernetes resource template")
    @PostMapping(value = "/template/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesResourceTemplateVO.Template>> queryTemplatePage(
            @RequestBody @Valid KubernetesResourceTemplateParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(templateFacade.queryTemplatePage(pageQuery));
    }

    @Operation(summary = "Get kubernetes resource template")
    @GetMapping(value = "/template/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesResourceTemplateVO.Template> getTemplateById(int id) {
        return new HttpResult<>(templateFacade.getTemplateById(id));
    }

    @Operation(summary = "Copy kubernetes resource template")
    @PostMapping(value = "/template/copy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesResourceTemplateVO.Template> copyTemplate(
            @RequestBody @Valid KubernetesResourceTemplateParam.CopyTemplate copyTemplate) {
        return new HttpResult<>(templateFacade.copyTemplate(copyTemplate));
    }

    @Operation(summary = "Create kubernetes resource by template")
    @PostMapping(value = "/template/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createResourceByTemplate(
            @RequestBody @Valid KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate) {
        templateFacade.createResourceByTemplate(createResourceByTemplate);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add kubernetes resource template")
    @PostMapping(value = "/template/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTemplate(
            @RequestBody @Valid KubernetesResourceTemplateParam.AddTemplate addTemplate) {
        templateFacade.addTemplate(addTemplate);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update kubernetes resource template")
    @PutMapping(value = "/template/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTemplate(
            @RequestBody @Valid KubernetesResourceTemplateParam.UpdateTemplate updateTemplate) {
        templateFacade.updateTemplate(updateTemplate);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update kubernetes resource template valid")
    @PutMapping(value = "/template/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setTemplateValidById(@RequestParam int id) {
        templateFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete kubernetes resource template by id")
    @DeleteMapping(value = "/template/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTemplateById(@RequestParam int id) {
        templateFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    // Member
    @Operation(summary = "Pagination query kubernetes resource template")
    @PostMapping(value = "/member/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesResourceTemplateVO.Member>> queryMemberPage(
            @RequestBody @Valid KubernetesResourceTemplateParam.MemberPageQuery pageQuery) {
        return new HttpResult<>(templateMemberFacade.queryMemberPage(pageQuery));
    }

    @Operation(summary = "Add kubernetes resource template member")
    @PostMapping(value = "/member/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addMember(@RequestBody @Valid KubernetesResourceTemplateParam.AddMember addMember) {
        templateMemberFacade.addMember(addMember);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update kubernetes resource template member")
    @PutMapping(value = "/member/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateMember(
            @RequestBody @Valid KubernetesResourceTemplateParam.UpdateMember updateMember) {
        templateMemberFacade.updateMember(updateMember);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update kubernetes resource template member valid")
    @PutMapping(value = "/member/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setMemberValidById(@RequestParam int id) {
        templateMemberFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete kubernetes resource template member by id")
    @DeleteMapping(value = "/member/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteMemberById(@RequestParam int id) {
        templateMemberFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    // Resource
    @Operation(summary = "Pagination query kubernetes resource")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesResourceVO.Resource>> queryResourcePage(
            @RequestBody @Valid KubernetesResourceParam.ResourcePageQuery pageQuery) {
        return new HttpResult<>(resourceFacade.queryResourcePage(pageQuery));
    }

    @Operation(summary = "Delete kubernetes resource by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteResourceById(@RequestParam int id) {
        resourceFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
