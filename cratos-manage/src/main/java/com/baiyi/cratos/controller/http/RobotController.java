package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.robot.RobotParam;
import com.baiyi.cratos.domain.view.robot.RobotVO;
import com.baiyi.cratos.facade.RobotFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 17:16
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/robot/")
@Tag(name = "Robot")
@RequiredArgsConstructor
public class RobotController {

    private final RobotFacade robotFacade;

    @Operation(summary = "Pagination query robot")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RobotVO.Robot>> queryRobotPage(
            @RequestBody @Valid RobotParam.RobotPageQuery pageQuery) {
        return new HttpResult<>(robotFacade.queryRobotPage(pageQuery));
    }

    @Operation(summary = "Query robot by username")
    @GetMapping(value = "/username/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<RobotVO.Robot>> queryRobotByUsername(@RequestParam @Valid String username) {
        return new HttpResult<>(robotFacade.queryRobotByUsername(username));
    }

    @Operation(summary = "Add robot(Admin)")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<RobotVO.RobotToken> addRobot(@RequestBody @Valid RobotParam.AddRobot addRobot) {
        return new HttpResult<>(robotFacade.addRobot(addRobot));
    }

    @Operation(summary = "Apply for robot")
    @PostMapping(value = "/apply", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<RobotVO.RobotToken> applyRobot(@RequestBody @Valid RobotParam.ApplyRobot applyRobot) {
        return new HttpResult<>(robotFacade.applyRobot(applyRobot));
    }

    @Operation(summary = "Revoke robot")
    @PutMapping(value = "/revoke", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeRobot(@RequestBody @Valid RobotParam.RevokeRobot revokeRobot) {
        robotFacade.revokeRobot(revokeRobot);
        return HttpResult.SUCCESS;
    }

}
