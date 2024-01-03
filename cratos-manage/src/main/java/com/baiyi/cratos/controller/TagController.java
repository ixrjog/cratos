package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.tag.TagParam;
import com.baiyi.cratos.domain.view.tag.TagVO;
import com.baiyi.cratos.facade.TagFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/2 17:33
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/tag")
@Tag(name = "Tag")
@RequiredArgsConstructor
public class TagController {

    private final TagFacade tagFacade;

    @Operation(summary = "Add tag")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTag(@RequestBody @Valid TagParam.AddTag addTag) {
        tagFacade.addTag(addTag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update tag")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTag(@RequestBody @Valid TagParam.UpdateTag updateTag) {
        tagFacade.updateTag(updateTag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query tag")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TagVO.Tag>> queryTagPage(@RequestBody @Valid TagParam.TagPageQuery pageQuery) {
        return new HttpResult<>(tagFacade.queryTagPage(pageQuery));
    }

}