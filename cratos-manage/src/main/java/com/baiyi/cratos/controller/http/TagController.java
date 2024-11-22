package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.tag.TagParam;
import com.baiyi.cratos.domain.view.tag.TagVO;
import com.baiyi.cratos.facade.TagFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public HttpResult<Boolean> updateTag(@RequestBody @Valid TagParam.UpdateTag updateTag) {
        tagFacade.updateTag(updateTag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update tag valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setTagValidById(@RequestParam int id) {
        tagFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query tag")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TagVO.Tag>> queryTagPage(@RequestBody @Valid TagParam.TagPageQuery pageQuery) {
        return new HttpResult<>(tagFacade.queryTagPage(pageQuery));
    }

    @Operation(summary = "Query tag by business type")
    @PostMapping(value = "/business/type/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TagVO.Tag>> queryTagByBusinessType(@RequestBody @Valid BusinessParam.QueryByBusinessType getByBusinessType) {
        return new HttpResult<>(tagFacade.queryTagByBusinessType(getByBusinessType));
    }

    @Operation(summary = "Delete tag by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTagById(@RequestParam int id) {
        tagFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}